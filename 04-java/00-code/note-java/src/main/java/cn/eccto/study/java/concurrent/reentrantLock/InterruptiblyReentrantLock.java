package cn.eccto.study.java.concurrent.reentrantLock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/06/30 07:12
 */
public class InterruptiblyReentrantLock {

    static ReentrantLock lock1 = new ReentrantLock();
    static ReentrantLock lock2 = new ReentrantLock();


    public Thread lock1() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //TODO 3.1 如果当前没有被中断,则获取锁
                    lock1.lockInterruptibly();
                    try {
                        //TODO 4.1 sleep 500ms 这里执行具体的业务逻辑
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        lock2.lockInterruptibly();
                        System.out.println(Thread.currentThread().getName() + ",执行完毕");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    //TODO 5.1 在业务逻辑执行完成之后,检查当前线程是否持有该锁,如果持有则释放
                    if (lock1.isHeldByCurrentThread()) {
                        lock1.unlock();
                    }
                    if (lock2.isHeldByCurrentThread()) {
                        lock2.unlock();
                    }
                    System.out.println(Thread.currentThread().getName() + ",退出");
                }

            }
        });
        t.start();
        return t;
    }

    public Thread lock2() {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    lock2.lockInterruptibly();
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        lock1.lockInterruptibly();
                        System.out.println(Thread.currentThread().getName() + ",执行完毕");
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    if (lock2.isHeldByCurrentThread()) {
                        lock2.unlock();
                    }
                    if (lock1.isHeldByCurrentThread()) {
                        lock1.unlock();
                    }
                    System.out.println(Thread.currentThread().getName() + ",退出");
                }

            }
        });
        t.start();
        return t;
    }

    public static void main(String[] args) {
        final long l = System.currentTimeMillis();
        InterruptiblyReentrantLock reentrantLock = new InterruptiblyReentrantLock();
        final Thread thread = reentrantLock.lock1();
        final Thread thread1 = reentrantLock.lock2();
        for (; ; ) {
            if (System.currentTimeMillis() - l >= 4000) {
                thread.interrupt();
            }
        }
    }
}
