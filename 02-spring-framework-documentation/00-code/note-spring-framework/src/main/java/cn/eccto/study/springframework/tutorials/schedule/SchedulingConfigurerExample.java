package cn.eccto.study.springframework.tutorials.schedule;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.time.LocalTime;

/**
 * 使用{@link SchedulingConfigurer} 器配置一个定时任务
 *
 * @author EricChen 2019/11/24 17:02
 */
public class SchedulingConfigurerExample {

    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(
                        MyConfig.class);

        Thread.sleep(5000);
        System.out.println(" -- Exiting application after 5 secs-- ");
        System.exit(0);

    }

    @EnableScheduling
    @Configuration
    public static class MyConfig implements SchedulingConfigurer {

        @Override
        public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
            taskRegistrar.addFixedDelayTask(() -> {
                System.out.println("Running task : " + LocalTime.now());
            }, 500);
        }
    }
}