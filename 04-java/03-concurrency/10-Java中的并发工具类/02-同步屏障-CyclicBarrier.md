# CyclicBarrier

## 内存屏障

CyclicBarrier 的字面意思是可循环使用(Cyclic)的屏障 (Barrier)。它要做的事情是，让一组线程到达一个屏障(也 可以叫同步点)时被阻塞，直到最后一个线程到达屏障时， 屏障才会开门，所有被屏障拦截的线程才会继续工作。 CyclicBarrier 默认的构造方法是 CyclicBarrier(int parties)， 其参数表示屏障拦截的线程数量，每个线程调用 await 方 法告诉 CyclicBarrier 当前线程已经到达了屏障，然后当前

## 使用场景

当存在需要所有的子任务都完成时，才执行主任务，这个 时候就可以选择使用 CyclicBarrier

## 注意点

1)对于指定计数值 parties，若由于某种原因，没有足够的 线程调用 CyclicBarrier 的 await，则所有调用 await 的线程 都会被阻塞;
2)同样的 CyclicBarrier 也可以调用 await(timeout, unit)， 设置超时时间，在设定时间内，如果没有足够线程到达， 则解除阻塞状态，继续工作;
3)通过 reset 重置计数，会使得进入 await 的线程出现 BrokenBarrierException;
4 ) 如 果 采 用 是 CyclicBarrier(int parties, Runnable barrierAction) 构造方法，执行 barrierAction 操作的是最 后一个到达的线程

### 实现原理

CyclicBarrier 相比 CountDownLatch 来说，要简单很多， 源码实现是基于 ReentrantLock 和 Condition 的组合使 用。看如下示意图，CyclicBarrier 和 CountDownLatch 是 不是很像，只是 CyclicBarrier 可以有不止一个栅栏，因为 它的栅栏(Barrier)可以重复使用(Cyclic)