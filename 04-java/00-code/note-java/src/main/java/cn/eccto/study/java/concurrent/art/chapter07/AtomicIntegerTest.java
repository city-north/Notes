package cn.eccto.study.java.concurrent.art.chapter07;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <p>
 * A Test of {@link AtomicInteger} and {@link Integer} increment
 * </p>
 *
 * @author EricChen 2020/04/07 12:55
 */
public class AtomicIntegerTest implements Runnable {
    private AtomicInteger atomicInteger = new AtomicInteger(0);
    public Integer integer = 0;
    private CountDownLatch countDownLatch;

    public AtomicIntegerTest(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1000);
        AtomicIntegerTest atomicIntegerTest = new AtomicIntegerTest(countDownLatch);
        for (int i = 0; i < 10000; i++) {
            new Thread(atomicIntegerTest).start();
        }
        System.out.println("sum:" + atomicIntegerTest.getInteger());
        System.out.println("sumAto:" + atomicIntegerTest.getAtomicInteger());
        countDownLatch.await();
    }

    @Override
    public void run() {
        integer++;
        atomicInteger.incrementAndGet();
        countDownLatch.countDown();
    }

    public AtomicInteger getAtomicInteger() {
        return atomicInteger;
    }

    public void setAtomicInteger(AtomicInteger atomicInteger) {
        this.atomicInteger = atomicInteger;
    }

    public Integer getInteger() {
        return integer;
    }

    public void setInteger(Integer integer) {
        this.integer = integer;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public void setCountDownLatch(CountDownLatch countDownLatch) {
        this.countDownLatch = countDownLatch;
    }
}
