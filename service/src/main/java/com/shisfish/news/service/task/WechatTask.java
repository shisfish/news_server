package com.shisfish.news.service.task;

import com.shisfish.news.service.service.biz.INewsService;
import com.shisfish.news.service.service.biz.INewsWechatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author shisfish
 * @date 2023/8/31
 * @Description 微信任务
 */
@Component
@EnableAsync
public class WechatTask {

    @Autowired
    private INewsService newsService;

    @Autowired
    private INewsWechatService newsWechatService;

    /**
     * 每天一次，保存最新的公众号
     */
    @Async
    @Scheduled(cron = "1 0 0 * * ?")
    public void saveLatestNewsWechatBySpider() {
        newsWechatService.saveLatestNewsWechatBySpider();
    }

    /**
     * 每四个小时执行一次，保存还未录入新闻的公众号信息
     */
    @Async
    @Scheduled(cron = "1 */4 0 * * ?")
    public void saveNewsFromWechat() {
        newsService.saveNewsFromWechat();
        newsService.loadNewsToStatic();
    }


    /**
     * 每天一次，本地化操作
     */
    @Async
    @Scheduled(cron = "1 5 0 * * ?")
    public void loadNewsToStatic() {
        newsService.loadNewsToStatic();
    }

    @Async
    @Scheduled(cron = "0 0 3 * * 1")
    public void copyToBack() {
        newsService.copyToBack();
    }

    @Async
    @Scheduled(cron = "0 0 1 * * 1")
    public void copyToBm() {
        newsService.copyToBm();
    }

}
