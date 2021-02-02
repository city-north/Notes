# 046-045-ThreadPoolExecutor-源码分析-awaitTermination操作

[TOC]

## awaitTermination操作

当线程调用awaitTermination方法后, 当前线程会被阻塞, 知道线程池状态改为 TERMINATED 才返回, 或者等待时间超时才返回

```java
public boolean awaitTermination(long timeout, TimeUnit unit)
  throws InterruptedException {
  long nanos = unit.toNanos(timeout);
  final ReentrantLock mainLock = this.mainLock;
  mainLock.lock();
  try {
    for (;;) {
      if (runStateAtLeast(ctl.get(), TERMINATED))
        return true;
      if (nanos <= 0)
        return false;
      nanos = termination.awaitNanos(nanos);
    }
  } finally {
    mainLock.unlock();
  }
```

代码首先会获取独占锁, 然后无限循环内部判断当前线程池的状态是否是TERMINATED状态, 

- 如果状态是否是TERMINATED状态,则直接返回,
-  否则说明当前线程池里面还有线程在执行,则看设置的超时时间 nanos 是否小于0
  - 小于0则说明不需要等待, 那就直接返回
  - 如果大于0 则调用条件变量 termination 的 awaitNanos 方法等待 nanos 时间, 期望在这段时间内线程池状态变为 TERMINATED

## awaitTermination和shutdown方法

当线程池状态改为TERMINATED时, 会调用 termination.signalAll() 用来调用条件变量 termination的 await系列方法被阻塞的所有线程 ,所有如果在调用 awaitTermination 之后又调用了shutdown 方法, 并且在shutdown 内部将线程池设置为 TERMINATED 则termination.awaitNacos 方法会返回

在工作线程Worker的runWorker 方法内, 当工作线程运行结束后, 会调用processWorkerExit 方法, 在 processWorkerExit 方法内部也会调用 tryTerminate 方法测试当前是否应该把线程池状态设置为 TERMINATED

- 如果是, 则也会调用 termination.singalAll()用来激活调用线程池的 awaitTermination 方法而被阻塞的线程

而且当等待时间超时后, termination.awaitNanos 也会返回, 

- 这时候会重新检查当前线程状态是否为TERMINATED

- 如果是则直接返回, 否则继续阻塞挂起自己

