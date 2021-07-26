package cn.eccto.study.springframework.tutorials.async;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * 异步方法的返回值以及方法参数
 *
 * @author JonathanChen 2019/11/24 17:11
 */
@EnableAsync
@Configuration
public class AsyncArgAndReturnValueExample {

    @Bean
    public MyBean myBean() {
        return new MyBean();
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AsyncArgAndReturnValueExample.class);
        MyBean bean = context.getBean(MyBean.class);
        System.out.printf("calling MyBean#runTask() thread: %s%n", Thread.currentThread().getName());
        String s = bean.runTask("from main");
        System.out.println("call MyBean#runTask() returned");
        System.out.println("returned value: " + s);
    }


    private static class MyBean {
        @Async
        public String runTask(String message) {
            System.out.printf("Running task  thread: %s%n", Thread.currentThread().getName());
            System.out.printf("message: %s%n", message);
            System.out.println("task ends");
            return "return value";
        }
    }
}
