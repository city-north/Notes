package cn.eccto.study.java.concurrent.producerconsumer;

import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * 生产者
 * </p>
 *
 * @author Jonathan 2020/02/26 15:32
 */
public class Producer implements Runnable {
    private volatile boolean isRunning = true;
    private BlockingQueue<String> queue;// 内存缓冲区
    private static AtomicInteger count = new AtomicInteger();// 总数 原子操作
    private static final int SLEEPTIME = 1000;

    public Producer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        Random r = new Random();
        System.out.println("start producting id:" + Thread.currentThread().getId());
        try {
            while (isRunning) {
                Thread.sleep(r.nextInt(SLEEPTIME));
                System.out.println(r.toString() + " 加入队列");
                if (!queue.offer(r.toString(), 2, TimeUnit.SECONDS)) {
                    System.err.println(" 加入队列失败");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

    }

    public void stop() {
        isRunning = false;
    }
}
