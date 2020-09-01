package com.wjk.group.threadPoll;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @program: GTM-WJK
 * @description: 线程池
 * @author: junkang.wei
 * @create: 2020-08-23 15:28
 **/
public class ThreadPool {

    private ThreadPool() {
    }

    /**
     * 无界线程池，不影响事务流程执行
     */
    public static final ExecutorService POOL = Executors.newCachedThreadPool();
}
