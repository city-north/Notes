# Semaphore

semaphore 也就是我们常说的信号灯，semaphore 可以控 制同时访问的线程个数，通过 acquire 获取一个许可，如 果没有就等待，通过 release 释放一个许可。有点类似限流 的作用。叫信号灯的原因也和他的用处有关，比如某商场 就 5 个停车位，每个停车位只能停一辆车，如果这个时候 来了 10 辆车，必须要等前面有空的车位才能进入。

### Semaphore 源码分析

从 Semaphore 的功能来看，我们基本能猜测到它的底层 实现一定是基于 AQS 的共享所，因为需要实现多个线程共 享一个领排池

创建 Semaphore 实例的时候，需要一个参数 permits， 这个基本上可以确定是设置给 AQS 的 state 的，然后每 个线程调用 acquire 的时候，执行 state = state - 1，release 的时候执行 state = state + 1，当然，acquire 的 时候，如果 state=0，说明没有资源了，需要等待其他线 程 release。

Semaphore 分公平策略和非公平策略

### NofairSync

通过对比发现公平和非公平的区别就在于是否多了一个 hasQueuedPredecessors 的判断