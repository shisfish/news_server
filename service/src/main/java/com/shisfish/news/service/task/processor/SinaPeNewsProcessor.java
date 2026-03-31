package com.shisfish.news.service.task.processor;

import com.shisfish.news.common.constant.Constants;
import com.shisfish.news.common.exception.CustomException;
import com.shisfish.news.common.utils.string.StringUtils;
import com.shisfish.news.dao.domain.biz.News;
import com.shisfish.news.service.task.config.target.PeNewsProperties;
import com.shisfish.news.service.task.pipeline.NewsPipeline;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Autowired;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.scheduler.BloomFilterDuplicateRemover;
import us.codecraft.webmagic.scheduler.QueueScheduler;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.Selectable;

import java.util.List;
import java.util.Objects;

/**
 * @Author: shisfish
 * @Date: 2023/8/16
 * @Description: 爬取新闻页面处理器页面
 * @Version: 1.0.0
 */
@Slf4j
//@Component
public class SinaPeNewsProcessor implements PageProcessor {
    @Autowired
    private NewsPipeline pipeline;

    /**
     * 解析页面
     */
    @Override
    public void process(Page page) {
        try {
            News news = new News();
            Html html = page.getHtml();
            // 拿到新浪nba专栏所有a标签请求连接并且发送请求 获取到的详情页连接
            List<String> urls = page.getHtml().$(PeNewsProperties.TARGET_URL_CSS_SELECTOR).links().all();
            page.addTargetRequests(urls);
            // 获取所有详情页列表---- 文本内容
            List<Selectable> selectableList = page.getHtml().$(PeNewsProperties.DETAIL_SELECT_CSS_SELECTOR).nodes();
            // 遍历详情页列表 解析出每一个信息 存到  News
            selectableList.stream().filter(Objects::nonNull)
                    .forEach(selectable -> {
                        log.info("正在使用的详情页链接->{}", selectable.links().toString());
                        // 爬下来的默认通过审核
                        news.setStatus(Constants.AuditFlag.AUDIT_PASSED);
                        // 设置作者id 管理员 id = 1
                        news.setUserId(1L);
                        // 设置新闻来源
                        news.setNewsSource(selectable.links().toString());
                        // 设置新闻标题 selectable.$(".main-title").toString()
                        news.setNewsTitle(Jsoup.parse(html.$(PeNewsProperties.NEWS_TITLE_CSS_SELECTOR).toString()).text());
                        // 体育新闻栏目 id=4
                        news.setNewsTypeId(PeNewsProperties.NEWS_TYPE_ID);
                        // 设置新闻内容
                        news.setNewsContent(selectable.$(PeNewsProperties.NEWS_CONTENT_CSS_SELECTOR).toString());
                        // 设置新闻封面
                        Selectable src = selectable.$(PeNewsProperties.NEWS_IMAGE_CSS_SELECTOR, "src");
                        log.info("src->>>>>>{}", src);
                        if (StringUtils.isNotEmpty(src.toString())) {
                            news.setNewsImage(src.toString());
                        }
                        // 设置新闻属性 热点区新闻 2
                        news.setNewsAttr("2");
                    });
            // 把结果保存起来
            page.putField(PeNewsProperties.FIELD_KEY, news);

        } catch (Exception e) {
            throw new CustomException("爬取新闻解析错误: " + e.getMessage());
        }

    }

    @Override
    public Site getSite() {
        return Site.me()
                //设置编码
                .setCharset("utf8")
                // 间隔时间
                .setSleepTime(200 * 1000)
                //设置超时时间
                .setTimeOut(10 * 1000)
                //设置重试的间隔时间
                .setRetrySleepTime(3000)
                //设置重试的次数;
                .setRetryTimes(3);
    }

    // 执行爬虫
    //initialDelay当任务启动后，等等多久执行方法
    //fixedDelay每隔多久执行方法
    // @Scheduled(cron = "0 0/20 8,9,10 * * ?")
    // @Scheduled(cron = "0 0/10 15,16,17 * * ?")
    // @Scheduled(cron = "0 0/10 13,14,15 * * ?")
//    @Scheduled(cron = "0 0/50 17,18 * * ?")
    public void runSpiderProcess() {
        log.info("正在进行爬取中........");
        // 配置代理模式
        //        HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
        //        httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(
        //                new Proxy("124.93.201.59", 42672),
        //                new Proxy("222.90.110.194", 8080),
        //                new Proxy("120.236.130.132", 8060)
        //        ));
        Spider.create(new SinaPeNewsProcessor())
                //.setDownloader(httpClientDownloader) //设置代理
                .addUrl(PeNewsProperties.URL)
                .setScheduler(new QueueScheduler().setDuplicateRemover(new BloomFilterDuplicateRemover(100000)))
                .thread(2)
                .addPipeline(pipeline)
                .run();
    }
}
