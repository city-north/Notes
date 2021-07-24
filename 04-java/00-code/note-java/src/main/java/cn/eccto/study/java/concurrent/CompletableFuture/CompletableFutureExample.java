package cn.eccto.study.java.concurrent.CompletableFuture;

import java.util.concurrent.CompletableFuture;

/**
 * <p>
 * Example of {@link CompletableFuture}
 * </p>
 *
 * @author Jonathan 2020/03/10 20:40
 */
public class CompletableFutureExample {


    public static void main(String[] args) throws Exception {
        // 创建异步执行任务:
        CompletableFuture<Double> cf = CompletableFuture.supplyAsync(CompletableFutureExample::fetchPrice);
        // 如果执行成功:
        cf.thenAccept((result) -> {
            System.out.println("price: " + result);
        });
        // 如果执行异常:
        cf.exceptionally((e) -> {
            e.printStackTrace();
            return null;
        });
        // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭:
        Thread.sleep(20000);
    }


    static Double fetchPrice() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
        if (Math.random() < 0.3) {
            throw new RuntimeException("fetch price failed!");
        }
        return 5 + Math.random() * 20;
    }
}
