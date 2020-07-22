package cn.eccto.study.java.collections.queue;

import java.sql.SQLOutput;
import java.util.Random;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * description
 * </p>
 */
public class DelayedQueueTest {

    static class DelayedEle implements Delayed {
        private final long delayTime;
        private final long expire;
        private String taskName;

        public DelayedEle(long delay, String taskName) {
            this.delayTime = delay;
            this.expire = System.currentTimeMillis() + delay;
            this.taskName = taskName;
        }

        @Override
        public long getDelay(TimeUnit unit) {
            return unit.convert(expire - System.currentTimeMillis(), unit);
        }

        @Override
        public int compareTo(Delayed o) {
            return (int) (this.getDelay(TimeUnit.MILLISECONDS) - o.getDelay(TimeUnit.MILLISECONDS));
        }

        @Override
        public String toString() {
            return "DelayedEle{" +
                    "delayTime=" + delayTime +
                    ", expire=" + expire +
                    ", taskName='" + taskName + '\'' +
                    '}';
        }
    }


    public static void main(String[] args) {
        DelayQueue<DelayedEle> delayQueue = new DelayQueue<>();
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            DelayedEle delayedEle = new DelayedEle(random.nextInt(500), "Task:" + i);
            delayQueue.offer(delayedEle);
        }
        DelayedEle ele = null;
        try {
            for (; ; ) { // 防止虚假唤醒
                while ((ele = delayQueue.take()) != null) {
                    System.out.println(ele.toString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
