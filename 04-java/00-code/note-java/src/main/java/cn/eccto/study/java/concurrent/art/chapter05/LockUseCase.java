package cn.eccto.study.java.concurrent.art.chapter05;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * description
 * </p>
 *
 * @author Jonathan 2020/04/02 12:36
 */
public class LockUseCase {

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        try {
            lock.lock();

        } finally {
            lock.unlock();
        }
    }


    private static void useLockInterruptibly(){
        Lock lock = new ReentrantLock();
        try {
            //如果当前线程无法获取锁,会被封装成 Node 放入阻塞队列, 底层使用  LockSupport.park() 来让线程进入 WAITING 状态 会处理中断请求,不释放锁
            lock.lockInterruptibly();
        } catch (InterruptedException e) {
            //处理中断

        }finally {
            lock.unlock();
        }
    }



}
