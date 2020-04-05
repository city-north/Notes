package cn.eccto.study.java.concurrent.art.chapter09;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>
 * 简单的线程池使用
 * </p>
 *
 * @author EricChen 2020/04/05 22:24
 */
public class ThreadPoolTest implements Runnable {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(3);
        for(int i = 0; i < 100; i++) {
            executorService.execute(new ThreadPoolTest());
        }
        executorService.shutdown();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("ThreadName:" + Thread.currentThread().getName());
    }
}
