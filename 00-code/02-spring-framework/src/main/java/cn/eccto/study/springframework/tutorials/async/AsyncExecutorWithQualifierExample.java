package cn.eccto.study.springframework.tutorials.async;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executors;

/**
 * 代码实例
 * <p>
 * 如果有多个执行器(executors) 注册为 bean 时,我们可以通过使用  @Async("qualifierValue") 解决冲突
 *
 * @author EricChen 2019/11/24 17:12
 */
@EnableAsync
@Configuration
public class AsyncExecutorWithQualifierExample {
    @Bean
    public MyBean myBean() {
        return new MyBean();
    }

    @Bean
    @Qualifier("myExecutor1")
    public TaskExecutor taskExecutor2() {
        return new ConcurrentTaskExecutor(Executors.newFixedThreadPool(3));
    }

    @Bean
    @Qualifier("myExecutor2")
    public TaskExecutor taskExecutor() {
        return new ThreadPoolTaskExecutor();
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AsyncExecutorWithQualifierExample.class);
        MyBean bean = context.getBean(MyBean.class);
        System.out.printf("calling async method from thread: %s%n", Thread.currentThread().getName());
        bean.runTask();
        ThreadPoolTaskExecutor exec = context.getBean(ThreadPoolTaskExecutor.class);
        exec.getThreadPoolExecutor().shutdown();
    }


    private static class MyBean {

        @Async("myExecutor2")
        public void runTask() {
            System.out.printf("Running task  thread: %s%n", Thread.currentThread().getName());
        }
    }
}