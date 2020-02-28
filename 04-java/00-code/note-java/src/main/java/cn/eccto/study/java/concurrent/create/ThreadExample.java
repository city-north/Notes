package cn.eccto.study.java.concurrent.create;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 使用 Thread类创建线程
 * </p>
 *
 * @author EricChen 2020/02/26 23:04
 */
public class ThreadExample extends Thread {

    public ThreadExample(String name) {
        super(name);
    }

    @Override
    public void run() {
        for(int i = 0; i < 10; i++) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("测试" + getName());

        }
    }

    public static void main(String[] args) {
        new ThreadExample("TEST1").start();
        new ThreadExample("TEST2").start();
    }
}
