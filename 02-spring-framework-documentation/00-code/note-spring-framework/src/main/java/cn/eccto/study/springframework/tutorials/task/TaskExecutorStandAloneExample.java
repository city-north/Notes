package cn.eccto.study.springframework.tutorials.task;

import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;


/**
 * 使用{@link SimpleAsyncTaskExecutor} 执行一个简单的 task
 *
 * @author JonathanChen 2019/11/24 14:51
 */
public class TaskExecutorStandAloneExample {
    public static void main(String[] args) {
        TaskExecutor executor = new SimpleAsyncTaskExecutor();
        executor.execute(() -> {
            System.out.println("running task in thread: " +
                    Thread.currentThread()
                            .getName());
        });
        System.out.println("in main thread: " + Thread.currentThread()
                .getName());
    }

}
