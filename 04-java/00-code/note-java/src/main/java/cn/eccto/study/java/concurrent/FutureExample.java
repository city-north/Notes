package cn.eccto.study.java.concurrent;


import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * <p>
 * description
 * </p>
 *
 * @author Jonathan 2020/03/10 18:38
 */
public class FutureExample implements Callable<String> {

    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newFixedThreadPool(4);
// 定义任务:
        Callable<String> task = new FutureExample();
// 提交任务并获得Future:
        Future<String> future = executor.submit(task);
// 从Future获取异步执行返回的结果:
        while (!future.isDone()) {
            Thread.sleep(1000);
        }
        String result = future.get(); // 可能阻塞
        System.out.println(result);
        executor.shutdown();
    }

    @Override
    public String call() throws Exception {
        return Thread.currentThread().getName() + "线程";
    }

}
