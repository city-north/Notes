package cn.eccto.study.springframework.tutorials.schedule;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalTime;

/**
 * `@Scheduled`标注的方法必须 void 安徽以及不能有任何参数 ,运行会报错
 *
 * @author EricChen 2019/11/24 17:00
 */
@EnableScheduling
@Configuration
public class ScheduledMethodArgExample {

    @Bean
    public MyBean myBean () {
        return new MyBean();
    }
    public static void main (String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ScheduledMethodArgExample.class);

        MyBean bean = context.getBean(MyBean.class);
        System.out.printf("calling MyBean#runTask() thread: %s%n",
                Thread.currentThread().getName());
        String s = bean.runTask("from main");
        System.out.println("call MyBean#runTask() returned");
        System.out.println("returned value: " + s);
    }



    private static class MyBean {

        @Scheduled
        public String runTask (String message) {
            System.out.printf("task thread: %s, time: %s%n",
                    Thread.currentThread().getName(),
                    LocalTime.now());
            System.out.printf("message: %s%n", message);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
            }
            return "return value";
        }
    }
}