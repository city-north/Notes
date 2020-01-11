package cn.eccto.study.springframework.tutorials.async;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * description
 *
 * @author EricChen 2019/11/24 17:12
 */
public class AsyncConfigurerExample {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
        MyBean bean = context.getBean(MyBean.class);
        System.out.printf("calling MyBean#runTask() thread: %s%n", Thread.currentThread().getName());
        bean.runTask();
        ConcurrentTaskExecutor exec = (ConcurrentTaskExecutor) context.getBean("taskExecutor");
        ExecutorService es = (ExecutorService) exec.getConcurrentExecutor();
        es.shutdown();
    }

    @EnableAsync
    @Configuration
    public static class MyConfig implements AsyncConfigurer {
        @Bean
        public MyBean myBean() {
            return new MyBean();
        }

        @Bean("taskExecutor")
        @Override
        public Executor getAsyncExecutor() {
            return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(3));
        }

        @Override
        public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
            return (throwable, method, objects) -> System.out.println("-- exception handler -- " + throwable);
        }
    }

    private static class MyBean {

        @Async
        public void runTask() {
            System.out.printf("Running task  thread: %s%n",
                    Thread.currentThread().getName());
            throw new RuntimeException("test exception");
        }
    }
}