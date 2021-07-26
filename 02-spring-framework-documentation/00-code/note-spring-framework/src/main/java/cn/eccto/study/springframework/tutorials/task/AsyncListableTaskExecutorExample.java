package cn.eccto.study.springframework.tutorials.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncListenableTaskExecutor;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import java.util.concurrent.Callable;

/**
 * 具有成功或者失败回调的任务执行器
 *
 * @author JonathanChen 2019/11/24 15:02
 * @see AsyncListenableTaskExecutor  自定义监听 Task 执行器
 * @see ListenableFutureCallback 回调
 */
@Configuration
public class AsyncListableTaskExecutorExample {
    @Bean
    MyBean myBean() {
        return new MyBean();
    }

    @Bean
    AsyncListenableTaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor t = new SimpleAsyncTaskExecutor();
        t.setConcurrencyLimit(100);
        return t;
    }

    @Bean
    ListenableFutureCallback<String> taskCallback() {
        return new MyListenableFutureCallback();
    }

    public static void main(String[] args) throws Exception {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(AsyncListableTaskExecutorExample.class);
        MyBean bean = context.getBean(MyBean.class);
        bean.runTasks();
    }


    private static class MyBean {
        @Autowired
        private AsyncListenableTaskExecutor executor;
        @Autowired
        private ListenableFutureCallback threadListenableCallback;


        public void runTasks() throws Exception {

            for (int i = 0; i < 10; i++) {
                ListenableFuture<String> f = executor.submitListenable(getTask(i));
                f.addCallback(threadListenableCallback);
            }
        }

        private Callable<String> getTask(int i) {
            return () -> {
                System.out.printf("running task %d. Thread: %s%n", i, Thread.currentThread().getName());
                int c = 10 / 0;//故意报错
                return String.format("Task finished %d", i);
            };
        }
    }

    /**
     * 自定义 Task 回调
     */
    private static class MyListenableFutureCallback implements ListenableFutureCallback<String> {
        @Override
        public void onFailure(Throwable ex) {
            System.out.println("faliure message: " + ex.getMessage());
            ex.printStackTrace();
        }

        @Override
        public void onSuccess(String result) {
            System.out.println("success object: " + result);
        }
    }
}
