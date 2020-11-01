# Future和Callable

## 目录

- [简介](#简介)

## 简介

Runnable 接口封装了一个异步执行的任务

- run没有参数, 没有返回值,不能抛出受检异常

Callable 接口封装了一个异步执行的任务

- 没有参数
- call有返回值,返回值类型,可以抛出抽检异常

Future表示对异步执行结果的封装

如果仔细看`ExecutorService.submit()`方法，可以看到，它返回了一个`Future`类型，一个`Future`类型的实例代表一个未来能获取结果的对象：

```java
ExecutorService executor = Executors.newFixedThreadPool(4); 
// 定义任务:
Callable<String> task = new Task();
// 提交任务并获得Future:
Future<String> future = executor.submit(task);
// 从Future获取异步执行返回的结果:
String result = future.get(); // 可能阻塞
```

当我们提交一个`Callable`任务后，我们会同时获得一个`Future`对象，然后，我们在主线程某个时刻调用`Future`对象的`get()`方法，就可以获得异步执行的结果。在调用`get()`时，如果异步任务已经完成，我们就直接获得结果。如果异步任务还没有完成，那么`get()`会阻塞，直到任务完成后才返回结果。

一个`Future`接口表示一个未来可能会返回的结果，它定义的方法有：

- `get()`：获取结果（可能会等待）
- `get(long timeout, TimeUnit unit)`：获取结果，但只等待指定的时间；
- `cancel(boolean mayInterruptIfRunning)`：取消当前任务；
- `isDone()`：判断任务是否已完成。

## 代码

```java
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

```

当我们提交一个`Callable`任务后，我们会同时获得一个`Future`对象，然后，我们在主线程某个时刻调用`Future`对象的`get()`方法，就可以获得异步执行的结果。在调用`get()`时，如果异步任务已经完成，我们就直接获得结果。如果异步任务还没有完成，那么`get()`会阻塞，直到任务完成后才返回结果。

一个`Future`接口表示一个未来可能会返回的结果，它定义的方法有：

- `get()`：获取结果（可能会等待）
- `get(long timeout, TimeUnit unit)`：获取结果，但只等待指定的时间；
- `cancel(boolean mayInterruptIfRunning)`：取消当前任务；
- `isDone()`：判断任务是否已完成。

## 总结

- 对线程池提交一个`Callable`任务，可以获得一个`Future`对象；

- 可以用`Future`在将来某个时刻获取结果。