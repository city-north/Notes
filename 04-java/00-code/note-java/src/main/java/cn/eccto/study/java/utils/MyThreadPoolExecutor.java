package cn.eccto.study.java.utils;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * multiple Thread
 *
 * @author EricChen 2019/12/31 23:27
 */
public class MyThreadPoolExecutor {


    public static void execute(Runnable runable) {
        ThreadFactory threadFactory = new ThreadFactoryBuilder()
                .setNameFormat("demo-pool-%d")
                .build();
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(10, 100, 10L,
                TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(1024),
                threadFactory,
                new ThreadPoolExecutor.AbortPolicy());
        for (int i = 0; i < 10; i++) {
            threadPoolExecutor.execute(runable);
        }
        threadPoolExecutor.shutdown();
    }
}
