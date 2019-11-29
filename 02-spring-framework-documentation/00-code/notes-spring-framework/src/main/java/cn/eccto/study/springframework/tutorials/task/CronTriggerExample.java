package cn.eccto.study.springframework.tutorials.task;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import java.time.LocalTime;

/**
 * description
 *
 * @author EricChen 2019/11/24 16:36
 */
public class CronTriggerExample {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Program starts at : " + LocalTime.now());
        ThreadPoolTaskScheduler s = new ThreadPoolTaskScheduler();
        s.setPoolSize(5);
        s.initialize();
        for (int i = 0; i < 2; i++) {
            s.schedule(getTask(), new CronTrigger("0/2 * * * * ? "));
        }
        Thread.sleep(10000);
        s.getScheduledThreadPoolExecutor().shutdown();
    }

    public static Runnable getTask() {
        return () -> System.out.printf("Task: %s, Time: %s:%n",
                Thread.currentThread().getName(),
                LocalTime.now());
    }
}
