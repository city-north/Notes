package cn.eccto.study.springframework.tutorials.async;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * description
 *
 * @author EricChen 2019/11/24 17:13
 */
public class AsyncOnClassLevelExample {


    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(
                        MyConfig.class);
        MyBean bean = context.getBean(MyBean.class);
        System.out.printf("calling async method from thread: %s%n",
                Thread.currentThread().getName());
        bean.runTask1();
        bean.runTask2();
    }

    @EnableAsync
    @Configuration
    public static class MyConfig {
        @Bean
        public MyBean myBean() {
            return new MyBean();
        }
    }

    @Async
    private static class MyBean {

        public void runTask1() {
            System.out.printf("runTask1  thread: %s%n",
                    Thread.currentThread().getName());
        }

        public void runTask2() {
            System.out.printf("runTask2  thread: %s%n",
                    Thread.currentThread().getName());
        }
    }
}