# 040-Netty的异步回调

[TOC]

# Netty的异步回调

Netty官方文档中指出, Netty 的网络操作都是异步的, 在 Netty 源代码中用了大量的异步回调处理

- Netty 应用的Handler 处理器中的业务处理代码, 也都是异步执行的

## Netty异步回调接口实现

Netty继承和拓展了JDK Future 系列异步回调的API,  定义了自身的Future 接口和类, 实现了异步任务的监控/异步执行结果的读取

- 通过继承Future接口, 扩展了一个新的 Netty 的Future接口, 只是对Java中的接口进行了增强, 使得Netty异步任务能够以非阻塞的方式处理回调的结果
- 引入了新接口 GenericFutureListener, 用于标识 异步执行完成的监听器, 这个接口和 Guava 的FutureCallback 回调接口不同, Netty使用了监听器模式, 异步任务执行完成后的回调逻辑抽象成了Listener 监听器接口,这样 Netty 的GenericFutureListener 监听器接口加入 Netty的异步任务 Future中, 实现对异步任务执行状态的监听
  - Netty的 Future 接口, 可以对应Guava 的 ListenableFuture接口
  - Netty的 GenericFutureListener 接口, 可以对应 Guava 的FutureCallback

总体上来说, 在异步非阻塞回调的设计思路上, Netty 和 Guava

## 详解 GenericFutureListener接口

```java
public interface GenericFutureListener<F extends Future<?>> extends EventListener {

    void operationComplete(F future) throws Exception;
}
```

- operationComplete 当异步任务操作完成之后, 执行整个方法

## Netty的Future接口

```java
public interface Future<V> extends java.util.concurrent.Future<V> {

    boolean isSuccess();
    boolean isCancellable();
    Throwable cause();
    Future<V> addListener(GenericFutureListener<? extends Future<? super V>> listener);
    Future<V> addListeners(GenericFutureListener<? extends Future<? super V>>... listeners);
    Future<V> removeListener(GenericFutureListener<? extends Future<? super V>> listener);
    Future<V> removeListeners(GenericFutureListener<? extends Future<? super V>>... listeners);
    Future<V> sync() throws InterruptedException;
    Future<V> syncUninterruptibly();
    Future<V> await() throws InterruptedException;
    Future<V> awaitUninterruptibly();
    boolean await(long timeout, TimeUnit unit) throws InterruptedException;
    boolean await(long timeoutMillis) throws InterruptedException;
    boolean awaitUninterruptibly(long timeout, TimeUnit unit);
    boolean awaitUninterruptibly(long timeoutMillis);
    V getNow();
    @Override
    boolean cancel(boolean mayInterruptIfRunning);
}
```

## ChannelFuture

Netty的ChannelFuture 是Future的子接口,  Netty 有一系列子接口, 代表不同的异步任务 如 ChannelFuture

```java
ChannelFuture f = bootstrap.connect();
	f.addListener((ChannelFuture futureListener) ->
{
  if (futureListener.isSuccess()) {
		Logger.info("EchoClient客户端连接成功!");
	 } else {
		Logger.info("EchoClient客户端连接失败!");
}
});
```

