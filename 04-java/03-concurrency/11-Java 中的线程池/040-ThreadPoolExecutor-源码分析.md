# ThreadPoolExecutor-源码分析

 [010-ThreadPoolExecutor.md](010-ThreadPoolExecutor.md) 

ThreadPoolExecutor 只是 Executors 工具类的一部分 ,线程池巧妙地使用一个 Integer 类型的原子变量来记录线程池状态和线程池中的线程个数,通过线程池状态来控制任务的执行,每个 Worker 线程可以处理多个任务,线程池通过现成的复用减少线程创建和销毁的开销

- [execute方法](#execute方法)
- [addWorker方法](#addWorker方法)



## execute方法

execute 方法的作用是提交任务 command 到线程池中进行执行 . 用户线程提交任务到线程池的模型图

![image-20200726165259957](../../../assets/image-20200726165259957.png)

#### 一个生产消费模型

从图中可以看出 ThreadPoolExecutor 实际上就是一个生产消费模型,

- 当用户添加任务到线程池时,相当于生产者生产元素
- workers 线程工作集中国的线程直接执行任务或者从任务队列里面获取任务时则相当于消费者消费元素

```java
    public void execute(Runnable command) {
      // ① 如果任务为 null , 则排除 NPE
        if (command == null)
            throw new NullPointerException();
      // ②    获取当前线程池的状态 + 线程个数变量的组合值
        int c = ctl.get();
      //// ③ 如果当前线程池的状态个数是否小于 corePoolSize ,小于则开启新线程运行
        if (workerCountOf(c) < corePoolSize) {
            if (addWorker(command, true))
                return;
            c = ctl.get();
        }
      // ④ 如果线程池出于 RUNNING 状态则添加任务到阻塞队列
        if (isRunning(c) && workQueue.offer(command)) {
          // 4.1 二次检查
            int recheck = ctl.get();
          // 4.2 如果当前线程状态不是 Running 状态,则从队列中删除任务, 并执行拒绝策略
            if (! isRunning(recheck) && remove(command))
                reject(command);
          // 4.3 否则如果当前线程池为空,则添加一个线程
            else if (workerCountOf(recheck) == 0)
                addWorker(null, false);
        }
      //⑤ 如果队列满,则新增线程池,新增失败则执行拒绝策略
        else if (!addWorker(command, false))
            reject(command);
    }
```

- 代码 ③ 判断如果当前线程小于 corePoolSize ,则会向 works 里面新增一个核心线程 core 线程执行该任务
- 如果当前线程池中线程个数大于等于 corePoolSize 则执行④代码,判断一下线程池是不是非 running 状态,如果是非 Running 状态,则抛弃
- 4.1 处的二次检查实际上是因为添加任务到任务队列后,执行代码 4.2 前有可能线程池的状态已经变化了
  - 如果不是线程池 running 状态了,则把任务从任务队列中移除并执行拒绝策略
- 如果二次校验通过则执行 4.3 , 添加一个线程
- 如果 添加失败,说明任务对哦已经满了,那么执行 ⑤尝试新开启一个线程来执行任务 , 如果当前线程池中的线程个数 > maximumPoolSize 则执行拒绝策略

## addWorker方法

- 第一部分双重循环目的是通过 CAS 操作增加线程数
- 第二部分主要是把并发安全的任务添加到 workers 里面,并执行任务

添加失败的几种可能:

- 当线程池是 STOP/ TIDYING / TERMINATED
- 当线程池为 SHUTDOWN 并且已经有一个任务
- 当前线程为 SHUTDOWN 并且队列为空

```JAVA
private boolean addWorker(Runnable firstTask, boolean core) {
    retry:
    for (;;) {
        int c = ctl.get();
        int rs = runStateOf(c);

        // Check if queue empty only if necessary.
        if (rs >= SHUTDOWN &&
            ! (rs == SHUTDOWN &&
               firstTask == null &&
               ! workQueue.isEmpty()))
            return false;

        for (;;) {
            int wc = workerCountOf(c);
            if (wc >= CAPACITY ||
                wc >= (core ? corePoolSize : maximumPoolSize))
                return false;
            if (compareAndIncrementWorkerCount(c))
                break retry;
            c = ctl.get();  // Re-read ctl
            if (runStateOf(c) != rs)
                continue retry;
            // else CAS failed due to workerCount change; retry inner loop
        }
    }

    boolean workerStarted = false;
    boolean workerAdded = false;
    Worker w = null;
    try {
        w = new Worker(firstTask);
        final Thread t = w.thread;
        if (t != null) {
            final ReentrantLock mainLock = this.mainLock;
            mainLock.lock();
            try {
                // Recheck while holding lock.
                // Back out on ThreadFactory failure or if
                // shut down before lock acquired.
                int rs = runStateOf(ctl.get());

                if (rs < SHUTDOWN ||
                    (rs == SHUTDOWN && firstTask == null)) {
                    if (t.isAlive()) // precheck that t is startable
                        throw new IllegalThreadStateException();
                    workers.add(w);
                    int s = workers.size();
                    if (s > largestPoolSize)
                        largestPoolSize = s;
                    workerAdded = true;
                }
            } finally {
                mainLock.unlock();
            }
            if (workerAdded) {
                t.start();
                workerStarted = true;
            }
        }
    } finally {
        if (! workerStarted)
            addWorkerFailed(w);
    }
    return workerStarted;
}
```