package cn.eccto.study.springframework.tutorials.async;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * description
 *
 * @author JonathanChen 2019/11/24 17:13
 */
public class AsyncOverrideDefaultExecutorExample {
    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(
                        MyConfig.class);
        MyBean bean = context.getBean(MyBean.class);
        System.out.printf("calling async method from thread: %s%n",
                Thread.currentThread().getName());
        bean.runTask();

        ConcurrentTaskExecutor exec = context.getBean(ConcurrentTaskExecutor.class);
        ((ExecutorService) exec.getConcurrentExecutor()).shutdown();
    }

    @EnableAsync
    @Configuration
    public static class MyConfig {
        @Bean
        public MyBean myBean() {
            return new MyBean();
        }

        @Bean
        public TaskExecutor taskExecutor() {
            return new ConcurrentTaskExecutor(
                    Executors.newFixedThreadPool(3));
        }
    }

    private static class MyBean {

        @Async
        public void runTask() {
            System.out.printf("Running task  thread: %s%n",
                    Thread.currentThread().getName());
        }
    }
}