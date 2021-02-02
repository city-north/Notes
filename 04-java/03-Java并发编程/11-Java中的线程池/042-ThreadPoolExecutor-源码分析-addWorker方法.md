# 042-ThreadPoolExecutor-源码分析-addWorker方法

[TOC]

# 一言蔽之



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

        // (6) 检查队列是否只在必要时为空
        if (rs >= SHUTDOWN &&
            ! (rs == SHUTDOWN &&
               firstTask == null &&
               ! workQueue.isEmpty()))
            return false;
				// (7) 循环 CAS增加线程个数	
        for (;;) {
            int wc = workerCountOf(c);
          	// (7.1) 如果线程个数超限则返回 fasle
            if (wc >= CAPACITY ||
                wc >= (core ? corePoolSize : maximumPoolSize))
                return false;
          //(7.2) CAS 增加线程个数,同时只有一个线程成功
            if (compareAndIncrementWorkerCount(c))
                break retry;
          //(7.3) CAS 失败了,则看线程池状态是否发生变化,变化了则跳到外层循环重新尝试偶去线程,状态,否则内循环重新 CAS
            c = ctl.get();  // Re-read ctl
            if (runStateOf(c) != rs)
                continue retry;
            // else CAS failed due to workerCount change; retry inner loop
        }
    }
	// (8) 到这说明 CAS 成功了
    boolean workerStarted = false;
    boolean workerAdded = false;
    Worker w = null;
    try {
      //(8.1) 创建 worker
        w = new Worker(firstTask);
        final Thread t = w.thread;
        if (t != null) {
            final ReentrantLock mainLock = this.mainLock;
          // (8.2) 独占加锁,为了实现 workers 同步,因为可能多个线程调用线程池的 execute方法
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

## 