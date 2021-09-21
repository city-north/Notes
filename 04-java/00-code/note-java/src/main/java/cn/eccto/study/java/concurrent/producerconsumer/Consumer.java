package cn.eccto.study.java.concurrent.producerconsumer;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * <p>
 * 消费者
 * </p>
 *
 * @author Jonathan 2020/02/26 15:41
 */
public class Consumer implements Runnable {

    private BlockingQueue<String> queue;
    private static final int SLEEPTIME = 1000;

    public Consumer(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        Random r = new Random();
        try {
            while (true) {
                String data = queue.take();
                if (data != null) {
                    System.out.println("处理消息:" + data);
                    System.out.println("目前容量:" +queue.size());
                    Thread.sleep(r.nextInt(SLEEPTIME));
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

}
