package cn.eccto.study.java.concurrent.art.chapter08;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/04/05 19:47
 */
public class CyclicBarrierTest3 {

    static CyclicBarrier c = new CyclicBarrier(2);

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    c.await();
                } catch (Exception e) {
                }
            }
        });
        thread.start();
        thread.interrupt();
        try {
            c.await();
        } catch (BrokenBarrierException e) {
            System.out.println(c.isBroken());//当线程被interrupt 的时候,会抛出异常
        }
    }
}
