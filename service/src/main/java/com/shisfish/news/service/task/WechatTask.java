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
     * 安全整改：不再触发静态化（静态正文页已废弃，由 nginx 拦截 /resource 下 html）。
     */
    @Async
    @Scheduled(cron = "1 */4 0 * * ?")
    public void saveNewsFromWechat() {
        newsService.saveNewsFromWechat();
    }


    /**
     * 每天一次，本地化操作
     * 安全整改（2026-11-16）：静态化定时任务已停用，保留方法体但不再调度。
     */
    // @Scheduled(cron = "1 5 0 * * ?")
    @Async
    public void loadNewsToStatic() {
        // 静态正文页机制废弃，不再执行；如需彻底移除可删除本方法
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
