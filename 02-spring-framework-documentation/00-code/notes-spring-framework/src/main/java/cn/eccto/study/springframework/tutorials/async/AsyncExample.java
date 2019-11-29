package cn.eccto.study.springframework.tutorials.async;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 异步使用简单实例
 *
 * @author EricChen 2019/11/24 16:51
 */
public class AsyncExample {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
        MyBean bean = context.getBean(MyBean.class);
        System.out.printf("calling async method from thread: %s%n", Thread.currentThread().getName());
        bean.runTask();
    }

    @EnableAsync
    @Configuration
    public static class MyConfig {
        @Bean
        public MyBean myBean() {
            return new MyBean();
        }
    }

    private static class MyBean {

        @Async
        public void runTask() {
            System.out.printf("Running task  thread: %s%n", Thread.currentThread().getName());
        }
    }
}
