package cn.eccto.study.java.concurrent.art.chapter04.lockDemo;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * Lock 接口的实例
 * </p>
 *
 * @author EricChen 2020/03/08 21:01
 */
public class LockExample implements Runnable {
    private Lock lock;

    public LockExample(Lock lock) {
        this.lock = lock;
    }

    public static void main(String[] args) {
        Lock lock = new ReentrantLock();
        Thread thread = new Thread(new LockExample(lock));
        thread.start();


    }

    @Override
    public void run() {
        while (true) {
            lock.lock();
        }
    }
}
