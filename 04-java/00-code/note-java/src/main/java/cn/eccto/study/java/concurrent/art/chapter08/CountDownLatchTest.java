package cn.eccto.study.java.concurrent.art.chapter08;

import java.util.concurrent.CountDownLatch;

/**
 * <p>
 * A test for {@link java.util.concurrent.CountDownLatch}
 * </p>
 *
 * @author EricChen 2020/04/05 16:59
 */
public class CountDownLatchTest {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);

        new Thread(() -> {
            System.out.println("Thread:" + Thread.currentThread().getName());
            countDownLatch.countDown();
        }).start();
        new Thread(() -> {
            System.out.println("Thread:" + Thread.currentThread().getName());
            countDownLatch.countDown();
        }).start();
        new Thread(() -> {
            System.out.println("Thread:" + Thread.currentThread().getName());
            countDownLatch.countDown();
        }).start();
        countDownLatch.await();//所有线程执行完成后才会执行 WAITING 状态
    }
}
