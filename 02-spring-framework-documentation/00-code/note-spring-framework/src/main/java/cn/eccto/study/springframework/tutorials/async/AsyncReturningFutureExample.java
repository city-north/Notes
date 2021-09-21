package cn.eccto.study.springframework.tutorials.async;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * 在 Async 方法后返回 Future 对象
 *
 * @author JonathanChen 2019/11/24 17:14
 */
@EnableAsync
@Configuration
public class AsyncReturningFutureExample {

    @Bean
    public MyBean myBean() {
        return new MyBean();
    }

    public static void main(String[] args) throws Exception {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AsyncReturningFutureExample.class);
        MyBean bean = context.getBean(MyBean.class);
        System.out.printf("calling MyBean#runTask() thread: %s%n", Thread.currentThread().getName());
        CompletableFuture<String> r = bean.runTask();
        System.out.println("result from task:" + r.get());
    }


    private static class MyBean {

        @Async
        public CompletableFuture<String> runTask() {
            System.out.printf("Running task  thread: %s%n",
                    Thread.currentThread().getName());

            CompletableFuture<String> future = new CompletableFuture<String>() {
                @Override
                public String get() throws InterruptedException, ExecutionException {
                    return " task result";
                }
            };
            return future;
        }
    }
}