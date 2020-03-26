package cn.eccto.study.java.concurrent.art.chapter05;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * <p>
 * 双胞胎锁,同一时刻支持之多两个线程进行访问
 * </p>
 *
 * @author EricChen 2020/03/26 20:53
 */
public class TwinsLock implements Lock {
    private Sync sync = new Sync(2);

    @Override
    public void lock() {
        sync.tryAcquireShared(1);
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
        sync.acquireSharedInterruptibly(1);
    }

    @Override
    public boolean tryLock() {
        return sync.tryAcquireShared(1) >= 0;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return sync.tryAcquireSharedNanos(1, unit.toNanos(time));
    }

    @Override
    public void unlock() {
        sync.releaseShared(1);
    }

    @Override
    public Condition newCondition() {
        return sync.newCondition();
    }


    //自定义同步器
    private final static class Sync extends AbstractQueuedSynchronizer {

        public Sync(int count) {
            if (count <= 0) {
                throw new IllegalArgumentException("count must large than zero");
            }
            setState(count);
        }

        /**
         * 同时支持两个线程,所以一定是共享式访问
         */
        @Override
        protected int tryAcquireShared(int reduceCount) {
            //自旋
            for (; ; ) {
                int current = getState();
                int newCount = current - reduceCount;
                //通过使用 CAS 方式确保原子性
                if (newCount < 0 || compareAndSetState(current, newCount)) {
                    return newCount;
                }
            }
        }


        @Override
        protected boolean tryReleaseShared(int count) {
            //自旋
            for (; ; ) {
                //首先判断目前的状态
                int current = getState();
                int remain = current + count;
                if (compareAndSetState(count, remain)) {
                    return true;
                }
            }
        }

        final ConditionObject newCondition() {
            return new ConditionObject();
        }
    }

    public static void main(String[] args) {
        Lock lock = new TwinsLock();

    }
}
