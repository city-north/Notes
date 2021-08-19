package cn.eccto.study.java.concurrent.interrupt;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * description
 * </p>
 *
 * @author Jonathan 2020/02/26 23:36
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

    public class SafeInterruptedThread extends Thread {

        @Override
        public void run() {
            if (!Thread.currentThread().isInterrupted()) {
                try {
                    //TODO 处理线程具体的业务逻辑
                    sleep(3000);
                } catch (InterruptedException e) {
                    //处理线程的释放操作.如果释放锁
                    //重新设置中断标志
                    Thread.currentThread().interrupt();
                }
            }
            super.run();
        }
    }
}