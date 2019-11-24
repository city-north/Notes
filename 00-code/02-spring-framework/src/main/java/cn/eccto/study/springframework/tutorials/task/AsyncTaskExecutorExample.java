package cn.eccto.study.springframework.tutorials.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;

/**
 * 示例: 在 bean 中使用 {@link AsyncTaskExecutor}
 *
 * @author EricChen 2019/11/24 14:58
 */
@Configuration
public class AsyncTaskExecutorExample {
    @Bean
    MyBean myBean () {
        return new MyBean();
    }

    @Bean
    AsyncTaskExecutor taskExecutor () {
        SimpleAsyncTaskExecutor t = new SimpleAsyncTaskExecutor();
        //设置同时线程数
        t.setConcurrencyLimit(100);
        return t;
    }

    public static void main(String[] args) throws Exception{
        ApplicationContext context =
                new AnnotationConfigApplicationContext(AsyncTaskExecutorExample.class);
        MyBean bean = context.getBean(MyBean.class);
        bean.runTasks();
    }

    public static class MyBean {
        @Autowired
        private AsyncTaskExecutor executor;

        public void runTasks () throws Exception {
            List<Future<?>> futureList = new ArrayList<>();

            for (int i = 0; i < 122; i++) {
                Future<?> future = executor.submit(getTask(i));
                futureList.add(future);
            }

            for (Future<?> future : futureList) {
                System.out.println(future.get());
            }
        }

        private Callable<String> getTask (int i) {
            return () -> {
                System.out.printf("running task %d. Thread: %s%n",
                        i,
                        Thread.currentThread().getName());
                Thread.sleep(5000);
                return String.format("Task finished %d", i);
            };
        }
    }
}
