package cn.eccto.study.java.concurrent.create;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 实现 Runnable 接口的方式
 * </p>
 *
 * @author EricChen 2020/02/26 23:06
 */
public class RunnableExample implements Runnable {
    private String name;

    public RunnableExample(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        for(int i = 0; i < 10; i++) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("测试" + name);

        }
    }

    public static void main(String[] args) {
        new Thread(new RunnableExample("TEST1")).start();
        new Thread(new RunnableExample("TEST2")).start();
    }
}
