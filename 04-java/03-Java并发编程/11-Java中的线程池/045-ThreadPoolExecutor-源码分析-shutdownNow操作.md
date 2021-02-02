# 045-ThreadPoolExecutor-源码分析-shutdownNow操作

[TOC]

## shutdownNow操作

调用shutdownNow方法后, 线程池就不会再接受新的任务了, 并且会丢弃队列里面的任务, 正在执行的任务会被中断,该方法会立即返回, 并不等待激活的任务执行完成.

返回值为这时候队列里面被丢弃的任务列表

```java
public List<Runnable> shutdownNow() {
  List<Runnable> tasks;
  final ReentrantLock mainLock = this.mainLock;
  mainLock.lock();
  try {
    //16 权限检查
    checkShutdownAccess();
    //17 设置线程池状态为STOP
    advanceRunState(STOP);
    //18 中断所有线程
    interruptWorkers();
    //19 将队列任务移动到task中
    tasks = drainQueue();
  } finally {
    mainLock.unlock();
  }
  tryTerminate();
  return tasks;
}
```

- 首先调用代码 (16 ) , 检查权限 , 然后调用代码 (17) 设置当前线程池的状态为STOP
- 随后执行代码 (18 ) 中断所有的工作线程

需要注意的是

- 中断的所有线程包含空闲线程和正在执行任务的线程

```java
private void interruptWorkers() {
  final ReentrantLock mainLock = this.mainLock;
  mainLock.lock();
  try {
    for (Worker w : workers)
      w.interruptIfStarted();
  } finally {
    mainLock.unlock();
  }
}
```

19 将当前任务队列里面的任务移动到 tasks 列表