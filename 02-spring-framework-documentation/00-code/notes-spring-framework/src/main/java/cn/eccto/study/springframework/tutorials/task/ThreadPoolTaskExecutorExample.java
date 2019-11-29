package cn.eccto.study.springframework.tutorials.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

/**
 * {@link ThreadPoolTaskExecutor} 代码实例
 *
 * @author EricChen 2019/11/24 15:13
 */
@Configuration
public class ThreadPoolTaskExecutorExample {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(ThreadPoolTaskExecutorExample.class);
        MyBean bean = context.getBean(MyBean.class);
        bean.runTasks();
        ThreadPoolTaskExecutor t = context.getBean(ThreadPoolTaskExecutor.class);
        t.shutdown();
    }

    @Bean
    MyBean myBean() {
        return new MyBean();
    }

    @Bean
    TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor t = new ThreadPoolTaskExecutor();
        t.setCorePoolSize(10);
        t.setMaxPoolSize(100);
        t.setQueueCapacity(50);
        t.setAllowCoreThreadTimeOut(true);
        t.setKeepAliveSeconds(120);
        return t;
    }


    private static class MyBean {
        @Autowired
        private TaskExecutor executor;

        public void runTasks() {
            for (int i = 0; i < 100; i++) {
                executor.execute(getTask(i));
            }
        }

        private Runnable getTask(int i) {
            return () -> {
                System.out.printf("running task %d. Thread: %s%n", i, Thread.currentThread().getName());
            };
        }
    }
}
