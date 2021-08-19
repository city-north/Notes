package cn.eccto.study.java.concurrent.art.chapter05;

import java.util.concurrent.locks.LockSupport;

/**
 * <p>
 * description
 * </p>
 *
 * @author Jonathan 2020/07/11 16:33
 */
public class LockSupporttest2 {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("child thread begin park !");
                //调用 park 方法挂起自己
                LockSupport.park();
                System.out.println("child thread  unpark");
            }
        });

        thread.start();
        Thread.sleep(1000);
        System.out.println("main thread begin unpark");
        LockSupport.unpark(thread);
    }

}
