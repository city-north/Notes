package cn.eccto.study.springframework.tutorials.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ConcurrentTaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * description
 *
 * @author EricChen 2019/11/24 15:19
 */
@Configuration
public class ConcurrentTaskExecutorExample {
    @Bean
    MyBean myBean() {
        return new MyBean();
    }

    @Bean
    TaskExecutor taskExecutor() {
        ConcurrentTaskExecutor t = new ConcurrentTaskExecutor(Executors.newFixedThreadPool(3));
        t.setTaskDecorator(new TaskDecorator() {
            @Override
            public Runnable decorate(Runnable runnable) {
                return () -> {

                    MyTask task = (MyTask) runnable;
                    long t = System.currentTimeMillis();
                    task.run();
                    System.out.printf("time taken for task: %s , %s%n", task.getI(), (System.currentTimeMillis() - t));
                };
            }
        });
        return t;
    }

    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(ConcurrentTaskExecutorExample.class);
        MyBean bean = context.getBean(MyBean.class);
        bean.runTasks();
        ConcurrentTaskExecutor exec = context.getBean(ConcurrentTaskExecutor.class);
        ExecutorService service = (ExecutorService) exec.getConcurrentExecutor();
        service.shutdown();
    }


    private static class MyBean {
        @Autowired
        private TaskExecutor executor;

        public void runTasks() {
            for (int i = 0; i < 10; i++) {
                executor.execute(new MyTask(i));

            }
        }
    }

    private static class MyTask implements Runnable {

        private final int i;

        MyTask(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(400);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("running task %d. Thread: %s%n", i, Thread.currentThread().getName());
        }


        public int getI() {
            return i;
        }
    }
}
