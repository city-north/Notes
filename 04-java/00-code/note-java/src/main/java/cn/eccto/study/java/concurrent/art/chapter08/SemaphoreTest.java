package cn.eccto.study.java.concurrent.art.chapter08;

import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * A Test for {@link java.util.concurrent.Semaphore}
 * </p>
 *
 * @author EricChen 2020/04/05 19:17
 */
public class SemaphoreTest {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3);
        for (int i = 0; i < 10; i++) {
            new Car(i, semaphore).start();
        }
    }

    static class Car extends Thread {

        private int num;
        private Semaphore semaphore;

        public Car(int num, Semaphore semaphore) {
            this.num = num;
            this.semaphore = semaphore;
        }


        @Override
        public void run() {
            try {
                semaphore.acquire();//获取一个令牌(类似于停车卡)
                System.out.println("num:" + num + " ,got a parking space");
                TimeUnit.SECONDS.sleep(5);
                System.out.println("num" + num + " ,release a parking space");
                semaphore.release();//释放令牌(类似于停车卡)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
