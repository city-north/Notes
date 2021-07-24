package cn.eccto.study.java.concurrent.art.chapter08;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * <p>
 * A test for {@link java.util.concurrent.CyclicBarrier}
 * </p>
 *
 * @author Jonathan 2020/04/05 17:08
 */
public class CyclicBarrierTest {
    static CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

    public static void main(String[] args) throws Exception{
        new Thread(()-> {
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
            System.out.println(1);
        }).start();

        cyclicBarrier.await();
        System.out.println(2);
    }
}
