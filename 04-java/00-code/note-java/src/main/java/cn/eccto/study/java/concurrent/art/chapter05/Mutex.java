package cn.eccto.study.java.concurrent.art.chapter05;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;

/**
 * <p>
 * 自定义锁实现,用户使用 Mutex 的时候并不会直接和内部同步器的实现打交道,而是调用了</p>
 * </p>
 *
 * @author EricChen 2020/03/08 21:39
 */
public class Mutex {

    private final Sync sync = new Sync();

    public void lock() {
        sync.tryAcquire(1);
    }

    public boolean tryLock() {
        return sync.tryAcquire(1);
    }

    public void unlock() {
        sync.tryRelease(1);
    }

    public Condition newCondition() {
        return sync.newCondition();
    }

    public boolean isLock() {
        return sync.isHeldExclusively();
    }

    public boolean isQueuedThreads() {
        return sync.hasQueuedThreads();
    }

    public void lockInterruptibly() throws InterruptedException {
        sync.acquireInterruptibly(1);
    }

    public void tryLock(long timeout, TimeUnit timeUnit) throws InterruptedException {
        sync.tryAcquireNanos(1, timeUnit.toNanos(timeout));
    }


    /**
     * 集成同步器并重写指定的方法,随后将同步器组合在自定义的同步器组件中实现
     */
    static class Sync extends AbstractQueuedSynchronizer {

        /**
         * @return 是否独占
         */
        @Override
        protected boolean isHeldExclusively() {
            return getState() == 1;
        }

        /**
         * 当状态为 0 的时候获取锁
         *
         * @return 0
         */
        @Override
        protected boolean tryAcquire(int arg) {
            if (compareAndSetState(0, 1)) {
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
            return false;
        }

        @Override
        protected boolean tryRelease(int arg) {
            if (getState() == 0) {
                throw new IllegalArgumentException();
            }
            setExclusiveOwnerThread(null);
            setState(0);//更新状态
            return true;
        }

        public Condition newCondition() {
            return new ConditionObject();
        }
    }
}
