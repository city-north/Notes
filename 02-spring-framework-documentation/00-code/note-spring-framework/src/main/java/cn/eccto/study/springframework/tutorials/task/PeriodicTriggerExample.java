package cn.eccto.study.springframework.tutorials.task;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.PeriodicTrigger;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

/**
 * description
 *
 * @author JonathanChen 2019/11/24 16:35
 */
public class PeriodicTriggerExample {
    public static void main(String[] args) throws InterruptedException {
        ThreadPoolTaskScheduler s = new ThreadPoolTaskScheduler();
        s.setPoolSize(5);
        s.initialize();
        for (int i = 0; i < 2; i++) {
            //设置 5 秒执行一次
            s.schedule(getTask(), new PeriodicTrigger(5, TimeUnit.SECONDS));
        }
        Thread.sleep(10000);
        System.out.println("shutting down after 10 sec");
        s.getScheduledThreadPoolExecutor().shutdownNow();
    }

    public static Runnable getTask() {
        return () -> System.out.printf("Task: %s, Time: %s:%n",
                Thread.currentThread().getName(),
                LocalTime.now());
    }
}
