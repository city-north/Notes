# 002-执行器服务-ExecutorService

[TOC]

## 一言蔽之

ExecutorService 封装了执行的服务的基本思想

- An Executor that provides methods to manage termination and methods that can produce a Future for tracking progress of one or more asynchronous tasks.
- An ExecutorService can be shut down, which will cause it to reject new tasks. 

Two different methods are provided for shutting down an ExecutorService.

- The shutdown method will allow previously submitted tasks to execute before terminating, while the shutdownNow method prevents waiting tasks from starting and attempts to stop currently executing tasks. 
- Upon termination, an executor has no tasks actively executing, no tasks awaiting execution, and no new tasks can be submitted. An unused ExecutorService should be shut down to allow reclamation of its resources.
- Method submit extends base method Executor.execute(Runnable) by creating and returning a Future that can be used to cancel execution and/or wait for completion. 
- Methods invokeAny and invokeAll perform the most commonly useful forms of bulk execution, executing a collection of tasks and then waiting for at least one, or all, to complete. (Class ExecutorCompletionService can be used to write customized variants of these methods.)

The Executors class provides factory methods for the executor services provided in this package.

## 接口源码

```java
public interface ExecutorService extends Executor {
  //执行完关闭
  void shutdown();
  //立即关闭
  List<Runnable> shutdownNow();
  //是否关闭
  boolean isShutdown();
  //是否中断
  boolean isTerminated();
  //等待中断
  boolean awaitTermination(long timeout, TimeUnit unit) throws InterruptedException;

  //提交
  <T> Future<T> submit(Callable<T> task);
  <T> Future<T> submit(Runnable task, T result);
  Future<?> submit(Runnable task);

  //执行全部,返回执行结果全部
  <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks) throws InterruptedException;
  <T> List<Future<T>> invokeAll(Collection<? extends Callable<T>> tasks,long timeout, TimeUnit unit) throws InterruptedException;
  //Executes the given tasks, returning the result of one that has completed successfully 
  <T> T invokeAny(Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException;
  //Executes the given tasks, returning the result of one that has completed successfully
  <T> T invokeAny(Collection<? extends Callable<T>> tasks, long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException;
}
```

