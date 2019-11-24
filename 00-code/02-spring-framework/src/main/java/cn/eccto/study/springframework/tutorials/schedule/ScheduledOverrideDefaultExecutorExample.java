package cn.eccto.study.springframework.tutorials.schedule;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;

import java.time.LocalTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 使用 TaskScheduler 替换默认
 *
 * @author EricChen 2019/11/24 17:02
 */
public class ScheduledOverrideDefaultExecutorExample {

    public static void main (String[] args) throws Exception {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(
                        MyConfig.class);
        MyBean bean = context.getBean(MyBean.class);
        bean.runTask();
        System.out.println("call MyBean#runTask() returned");

        Thread.sleep(3000);
        System.out.println("Shutting down after 3 secs");
        ConcurrentTaskExecutor exec = (ConcurrentTaskExecutor) context.getBean("taskExecutor");
        ExecutorService es = (ExecutorService) exec.getConcurrentExecutor();
        es.shutdownNow();
    }

    @EnableScheduling
    @Configuration
    public static class MyConfig {
        @Bean
        public MyBean myBean () {
            return new MyBean();
        }

        @Bean
        public TaskScheduler taskExecutor () {
            return new ConcurrentTaskScheduler(
                    Executors.newScheduledThreadPool(3));
        }
    }

    private static class MyBean {

        @Scheduled(fixedDelay = 1000)
        public void runTask () {
            System.out.printf("task thread: %s, time: %s%n",
                    Thread.currentThread().getName(),
                    LocalTime.now());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
    }
}