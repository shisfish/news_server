package com.shisfish.news.framework.manager;


import com.shisfish.news.common.utils.Threads;
import com.shisfish.news.common.utils.spring.SpringUtils;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 异步任务管理器
 */
public class AsyncManager {

    /**
     * 异步操作任务调度线程池
     */
    private final ScheduledExecutorService executor = SpringUtils.getBean("scheduledExecutorService");

    /**
     * 单例模式
     */
    private AsyncManager() {
    }

    private static final AsyncManager ASYNC_MANAGER = new AsyncManager();

    public static AsyncManager me() {
        return ASYNC_MANAGER;
    }

    /**
     * 执行任务
     *
     * @param task 任务
     */
    public void execute(TimerTask task) {
        /**
         * 操作延迟10毫秒
         */
        int operateDelayTime = 10;
        executor.schedule(task, operateDelayTime, TimeUnit.MILLISECONDS);
    }

    /**
     * 停止任务线程池
     */
    public void shutdown() {
        Threads.shutdownAndAwaitTermination(executor);
    }
}
