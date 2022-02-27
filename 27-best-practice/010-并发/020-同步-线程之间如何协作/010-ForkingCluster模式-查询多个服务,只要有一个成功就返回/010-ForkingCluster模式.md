# Forking 的集群模式

[TOC]

## 简介

ForkingCluster ,Dubbo 中有一种叫做**Forking 的集群模式**，这种集群模式下，支持**并行地调用多个查询服务，只要有一个成功返回结果，整个服务就可以返回了**。

例如你需要提供一个地址转坐标的服务，为了保证该服务的高可用和性能，你可以并行地调用 3 个地图服务商的 API，然后只要有 1 个正确返回了结果 r，那么地址转坐标这个服务就可以直接返回 r 了。

这种集群模式可以容忍 2 个地图服务商服务异常，但缺点是消耗的资源偏多



```java

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * <p>
 * ForkingCluster ,Dubbo 中有一种叫做**Forking 的集群模式**，这种集群模式下，支持**并行地调用多个查询服务，只要有一个成功返回结果，整个服务就可以返回了**。
 * <p>
 * 例如你需要提供一个地址转坐标的服务，为了保证该服务的高可用和性能，你可以并行地调用 3 个地图服务商的 API，然后只要有 1 个正确返回了结果 r，那么地址转坐标这个服务就可以直接返回 r 了。
 * <p>
 * 这种集群模式可以容忍 2 个地图服务商服务异常，但缺点是消耗的资源偏多
 *
 */
public class ForkingClusterDemo {
    public static void main(String[] args) throws InterruptedException {
        new ForkingClusterDemo().doForkingCluster();
    }

    public Integer doForkingCluster()  {
        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(3);
        // 创建 CompletionService
        CompletionService<Integer> cs = new ExecutorCompletionService<>(executor);
        // 用于保存 Future 对象
        List<Future<Integer>> futures = new ArrayList<>(3);
        // 提交异步任务，并保存 future 到 futures
        futures.add(cs.submit(() -> geocoderByS1()));
        futures.add(cs.submit(() -> geocoderByS2()));
        futures.add(cs.submit(() -> geocoderByS3()));
        // 获取最快返回的任务执行结果
        Integer r = 0;
        try {
            // 只要有一个成功返回，则 break
            for (int i = 0; i < 3; ++i) {
                r = cs.take().get();
                // 简单地通过判空来检查是否成功返回
                if (r != null) {
                    break;
                }
            }
        } catch (ExecutionException e) {
            //执行异常处理
        } catch (InterruptedException) {
            //处理中断
        } finally {
            // 取消所有任务
            for (Future<Integer> f : futures)
                f.cancel(true);
        }
        // 返回结果
        return r;
    }

    private Integer geocoderByS1() {
        return 1;
    }

    private Integer geocoderByS2() {
        return 2;
    }

    private Integer geocoderByS3() {
        return 3;
    }
}
```

