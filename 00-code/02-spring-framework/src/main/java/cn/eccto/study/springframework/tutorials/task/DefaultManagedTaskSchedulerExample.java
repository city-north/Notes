package cn.eccto.study.springframework.tutorials.task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.DefaultManagedTaskScheduler;

import javax.naming.NamingException;
import java.time.LocalTime;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * description
 *
 * @author EricChen 2019/11/24 16:44
 */
public class DefaultManagedTaskSchedulerExample {
    public static void main (String[] args) throws NamingException, InterruptedException {
//        //binding the scheduler manually,shi
//        // In JEE compliant server environment this will be
//        // provided by the server product
//
//        SimpleNamingContextBuilder b = new SimpleNamingContextBuilder();
//        b.bind("java:comp/DefaultManagedScheduledExecutorService",
//                Executors.newScheduledThreadPool(5));
//        b.activate();
//
//        //bootstrapping spring
//        ApplicationContext context =
//                new AnnotationConfigApplicationContext(MyConfig.class);
//        MyBean bean = context.getBean(MyBean.class);
//        bean.runTask();
//        //shutdown after 10 sec
//        Thread.sleep(10000);
//        DefaultManagedTaskScheduler scheduler = context.getBean(
//                DefaultManagedTaskScheduler.class);
//        Executor exec = scheduler.getConcurrentExecutor();
//        ExecutorService es = (ExecutorService) exec;
//        System.out.println("shutting down executor service");
//        es.shutdownNow();
    }

    @Configuration
    public static class MyConfig {

        @Bean
        TaskScheduler taskScheduler () {
            return new DefaultManagedTaskScheduler();
        }

        @Bean
        MyBean myBean () {
            return new MyBean();
        }
    }

    private static class MyBean {
        @Autowired
        TaskScheduler taskScheduler;

        public void runTask () {
            taskScheduler.scheduleWithFixedDelay((Runnable) () -> {
                System.out.println("running " + LocalTime.now());
            }, 1000L);
        }
    }
}
