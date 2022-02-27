# 010-CompletionService-实践

[TOC]

## CompletionService 接口说明

下面我们详细地介绍一下 CompletionService 接口提供的方法，CompletionService 接口提供的方法有 5 个，这 5 个方法的方法签名如下所示。

其中，submit() 相关的方法有两个。

```java
Future<V> submit(Callable<V> task);//提交任务
Future<V> submit(Runnable task, V result);//提交任务
```

- 一个方法参数是`Callable<V> task`，前面利用 CompletionService 实现询价系统的示例代码中，我们提交任务就是用的它。
- 另外一个方法有两个参数，分别是`Runnable task`和`V result`，这个方法类似于 ThreadPoolExecutor 的 `<T> Future<T> submit(Runnable task, T result)` ，这个方法在[《23 | Future：如何用多线程实现最优的“烧水泡茶”程序？》](https://time.geekbang.org/column/article/91292)中我们已详细介绍过，这里不再赘述。

CompletionService 接口其余的 3 个方法，都是和阻塞队列相关的，take()、poll() 都是从阻塞队列中获取并移除一个元素；

```java
Future<V> take() throws InterruptedException; //如果阻塞队列是空的，那么调用 take() 方法的线程会被阻塞
Future<V> poll();//如果阻塞队列是空的， poll() 方法会返回 null 值
Future<V> poll(long timeout, TimeUnit unit) throws InterruptedException;
```

它们的区别在于如果阻塞队列是空的，那么调用 take() 方法的线程会被阻塞，而 poll() 方法会返回 null 值。

-  `poll(long timeout, TimeUnit unit)` 方法支持以超时的方式获取并移除阻塞队列头部的一个元素，如果等待了 timeout unit 时间，阻塞队列还是空的，那么该方法会返回 null 值。

## 最佳实践

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