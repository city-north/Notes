package cn.eccto.study.springframework.tutorials.task;

import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import java.time.LocalTime;

/**
 * 实例展示了在以固定速率调度任务,使用`ThreadPoolTaskScheduler`
 *
 * @author EricChen 2019/11/24 16:28
 */
public class ThreadPoolTaskSchedulerExample {
    public static void main(String[] args) throws Exception {
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(5);
        scheduler.initialize();

        for(int i = 0; i < 6; i++) {
//            每1000ms 执行一次
            scheduler.scheduleAtFixedRate(() -> System.out.printf("Task: %s, Time: %s:%n", Thread.currentThread().getName(), LocalTime.now()), 1000);

        }

        Thread.sleep(10000);
        System.out.println("shutting down after 10 sec");
        scheduler.getScheduledThreadPoolExecutor().shutdownNow();
    }

}
