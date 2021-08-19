package cn.eccto.study.springframework.tutorials.schedule;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalTime;

/**
 * 简单使用@Scheduled 和 @EnableScheduling
 *
 * @author JonathanChen 2019/11/24 16:59
 */
public class ScheduledExample {

    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(
                        MyConfig.class);

        MyBean bean = context.getBean(MyBean.class);
        System.out.printf("calling MyBean#runTask() thread: %s%n",
                Thread.currentThread().getName());
        bean.runTask();
        System.out.println("call MyBean#runTask() returned");

        //exit after 5 secs
        Thread.sleep(5000);
        System.exit(0);
    }

    @EnableScheduling
    @Configuration
    public static class MyConfig {
        @Bean
        public MyBean myBean() {
            return new MyBean();
        }
    }

    private static class MyBean {

        @Scheduled(fixedRate = 1000)
        public void runTask() {
            System.out.printf("Running scheduled task " + " thread: %s, time: %s%n", Thread.currentThread().getName(), LocalTime.now());
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
        }
    }
}