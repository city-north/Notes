package cn.eccto.study.java.concurrent;

import java.util.concurrent.TimeUnit;

/**
 * <p>
 * description
 * </p>
 *
 * @author Jonathan 2020/02/26 16:18
 */
public class ThreadStatusDemo {

    public static void main(String[] args) {
        new Thread(() -> {
            while (true) {
                try {
                    //sleep jstack 的状态是 TIMED_WAITING
                    TimeUnit.SECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "Time_Waiting_Thread").start();
        new Thread(() -> {
            while (true) {
                synchronized (ThreadStatusDemo.class) {
                    try {
                        ThreadStatusDemo.class.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, "Wating_Thread").start();
    }


}
