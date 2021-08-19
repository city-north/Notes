package cn.eccto.study.java.concurrent.art.chapter05;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.LockSupport;

/**
 * <p>
 * description
 * </p>
 *
 * @author Jonathan 2020/07/11 16:47
 */
public class FIFOMutex {

    private final AtomicBoolean locked = new AtomicBoolean(false);
    private final Queue<Thread> waiters = new ConcurrentLinkedDeque<>();

    public void Lock() {
        boolean wasInterrupted = false;
        final Thread current = Thread.currentThread();
        waiters.add(current);
        //如果当前线程不是或者当前锁已经被其他线程偶去,则调用 park 挂起自己
        while (waiters.peek() != current || !locked.compareAndSet(false, true)) {
            LockSupport.park(this);
            if (Thread.interrupted()) {
                //如果 park 方法是因为中断而返回,则忽略中断并重置中断标志,做个记号,然后再次判断当前线程是不是队首元素或者当前锁是否已经被其他线程获取,如果则继续调用 park 方法挂起自己
                wasInterrupted = true;
            }
        }
        waiters.remove();
        if (wasInterrupted) {
            //其他线程中断了线程,其他线程有可能对该状态感兴趣,所以回复下
            current.interrupt();
        }
    }

    public void unLock() {
        locked.set(false);
        LockSupport.unpark(waiters.peek());
    }

}
