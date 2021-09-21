# 030-Guava的异步回调

[TOC]

## Guava如何进行异步回调的

- 引入一个新的接口 ListenableFuture , 继承了 Java 的 Future 接口, 是的 Future 的异步任务, 在Guava 中能被监控和获得非阻塞异步执行的结果
- 引入了一个新的接口 FutureTask , 这是一个独立的新的接口, 该接口的目的是, 是在异步任务执行完成后, 根据异步结果, 完成不同的回调处理, 并且可以处理异步结果

## FutureCallback

FutureCallback , 用来填写异步任务执行完完成之后的监听逻辑, 有两个回调方法

```java
public interface FutureCallback<V> {
  //成功回调
  void onSuccess(@Nullable V result);
  //失败回调
  void onFailure(Throwable t);
}
```

## ListenableFuture

```java
public interface ListenableFuture<V> extends Future<V> {
  /**
   *  添加监听器和监听器执行的执行器     
   */
  void addListener(Runnable listener, Executor executor);
}
```

我们需要使用Futures工具类, 来完成 FutureCallback 和 ListenableFuture的绑定

```java
//创建java 线程池
ExecutorService jPool = Executors.newFixedThreadPool(10);

Futures.addCallback(ListenableFuture future, new FutureCallback<R>() {
  public void onSuccess(R r) {
    //回调
  }

  public void onFailure(Throwable t) {
    //回调
  }
});
```

