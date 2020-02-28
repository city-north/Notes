package cn.eccto.study.java.concurrent.interrupt;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/02/26 23:36
 */
public class InterruptDemo {
    private static int i;

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) { //默认情况下isInterrupted 返回 false、通过 thread.interrupt 变成了 true
                i++;
            }
            System.out.println("Num:" + i);
        }, "interruptDemo");
        thread.start();
        TimeUnit.SECONDS.sleep(1);
        thread.interrupt(); //加和不加的效果

    }
}