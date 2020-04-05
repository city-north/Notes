package cn.eccto.study.java.concurrent.art.chapter08;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * <p>
 * A test for {@link CyclicBarrier}
 * </p>
 *
 * @author EricChen 2020/04/05 17:08
 */
public class CyclicBarrierTest2 {
    static CyclicBarrier cyclicBarrier = new CyclicBarrier(2, new A());//达到屏障后,优先执行 A

    public static void main(String[] args) throws Exception {
        new Thread(() -> {
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println(1);
        }).start();
        try {
            cyclicBarrier.await();
        } catch (Exception e) {

        }
        System.out.println(2);
    }


    static class A implements Runnable {

        @Override
        public void run() {
            System.out.println(3);
        }
    }
}
