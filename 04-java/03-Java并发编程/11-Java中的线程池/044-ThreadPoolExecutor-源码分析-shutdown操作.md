# 044-ThreadPoolExecutor-源码分析-shutdown操作

[TOC]

## 一言蔽之

调用shutdown之后, 线程池就不会再接受新的任务了, 但是工作队列里面的任务还是要执行的

这个方法会立即返回,并不会等待队列任务完成再返回



## shutdown源码

```java
public void shutdown() {
  final ReentrantLock mainLock = this.mainLock;
  mainLock.lock();
  try {
    //(12) 权限检查
    checkShutdownAccess();
    //(13) 设置当前线程池的状态为SHUTDOWN, 如果已经SHUTDOWN则直接返回
    advanceRunState(SHUTDOWN);
    // (14) 设置中断标志
    interruptIdleWorkers();
    onShutdown(); // hook for ScheduledThreadPoolExecutor
  } finally {
    mainLock.unlock();
  }
  //(15)尝试将状态改为TERMINATED
    tryTerminate();
}

```

- 代码12 , 检查是否设置了安全管理器,如果设置了, 则看当前的调用shutdown命令的线程是否有关闭线程的权限
  - 如果有权限则还要看线程是否有中断工作线程的权限
  - 如果没有则抛出 SecurityException 或者 NullPointerException
- 代码13 , 如果当前线程状态>= SHUTDOWN则直接返回, 否则设置为SHUTDOWN状态

```java
private void advanceRunState(int targetState) {
    for (;;) {
        int c = ctl.get();
        if (runStateAtLeast(c, targetState) ||
            ctl.compareAndSet(c, ctlOf(targetState, workerCountOf(c))))
            break;
    }
}
```

- 代码14, 设置所有空闲线程的中断标志

#### 设置所有空闲线程的中断标志

首先使用全局锁, 同时只有一个线程可以调用shutdown方式设置中断标志

- 尝试获取Worker自己的锁, 获取成功则设置中断标志

- 由于正在执行的任务已经获取了锁, 所以正在执行的任务没有被中断

这里中断的是阻塞到getTask()方法企图从队列里面获取任务的线程, 也就是空闲线程

```java
private void interruptIdleWorkers(boolean onlyOne) {
    final ReentrantLock mainLock = this.mainLock;
    mainLock.lock();
    try {
        for (Worker w : workers) {
            Thread t = w.thread;
          // 如果工作线程没有被中断, 并且没有正在运行则设置中断标志
            if (!t.isInterrupted() && w.tryLock()) {
                try {
                    t.interrupt();
                } catch (SecurityException ignore) {
                } finally {
                    w.unlock();
                }
            }
            if (onlyOne)
                break;
        }
    } finally {
        mainLock.unlock();
    }
}
```

使用CAS设置当前线程池状态为 TIDYING 

- 如果设置成功则执行拓展接口 terminated 在线程池状态变为 TERMINATED 前做一些事情

- 然后设置当前线程的状态为 TERMINATED

- 最后调用termination.singalAll() 激活因调用条件变量 termination 的 await 系列方法而被阻塞的所有线程

  