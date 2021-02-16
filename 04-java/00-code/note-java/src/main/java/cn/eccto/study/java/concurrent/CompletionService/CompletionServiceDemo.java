package cn.eccto.study.java.concurrent.CompletionService;

import java.util.concurrent.*;

/**
 * <p>
 * description
 * </p>
 *
 * @author qiang.chen04@hand-china.com 2021/2/16 20:43
 */
public class CompletionServiceDemo {
    static  Integer r = null;

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(3);
        // 异步向电商 S1 询价
        Future<Integer> f1 = executor.submit(() -> getPriceByS1());
        // 异步向电商 S2 询价
        Future<Integer> f2 = executor.submit(() -> getPriceByS2());
        // 异步向电商 S3 询价
        Future<Integer> f3 = executor.submit(() -> getPriceByS3());
        // 获取电商 S1 报价并保存
        r = f1.get();
        executor.execute(() -> save(r));

// 获取电商 S2 报价并保存
        r = f2.get();
        executor.execute(() -> save(r));
// 获取电商 S3 报价并保存
        r = f3.get();
        executor.execute(() -> save(r));
        executor.shutdown();
    }

    private static Integer getPriceByS1() throws InterruptedException {
        System.out.println("获取价格1");
        Thread.sleep(4000);
        return 1;
    }

    public static void save(Integer integer) {
        System.out.println("保存价格"+ integer);
    }

    private static Integer getPriceByS2() throws InterruptedException {
        System.out.println("获取价格2");
        Thread.sleep(2000);
        return 2;
    }
    private static Integer getPriceByS3() throws InterruptedException {
        System.out.println("获取价格3");
        Thread.sleep(3000);
        return 3;
    }

}
