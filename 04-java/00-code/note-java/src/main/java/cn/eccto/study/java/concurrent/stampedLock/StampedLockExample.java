package cn.eccto.study.java.concurrent.stampedLock;

import cn.eccto.study.java.utils.MyThreadPoolExecutor;

import java.util.concurrent.locks.StampedLock;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/03/10 16:53
 */
public class StampedLockExample {
    private final StampedLock stampedLock = new StampedLock();

    private double value = 0;


    public double getValue() {
        long stamp = stampedLock.tryOptimisticRead();
        if (stampedLock.validate(stamp)) {
            return value;
        } else {
            try {
                stamp = stampedLock.readLock();
                return value;
            } finally {
                stampedLock.unlockRead(stamp);
            }
        }
    }

    public void incr(double value) {
        long stamp = stampedLock.writeLock();
        try {
            this.value += value;
        } finally {
            stampedLock.unlockWrite(stamp);
        }

    }

    public static void main(String[] args) throws Exception{
        StampedLockExample example = new StampedLockExample();
        for (int i = 0; i < 200; i++) {
            Thread thread = new Thread(() -> {
                example.incr(1);
            });
            thread.start();
        }
        for (int i = 0; i < 200; i++) {
            Thread thread = new Thread(() -> {
                double value = example.getValue();
                System.out.println(value);
            });
            thread.start();
        }
        Thread.sleep(10000);

    }
}
