# 并发编程笔记

## 膜拜

前排膜拜并发大师 Doug Lea

![Doug Lea](assets/small.jpg)

# 简介

## 为什么要编写并发程序

与串行程序相比，在并发程序中有更多容易出错的地方。那么，为什么还要编写并发程序呢？

1. 线程使得复杂的异步代码变得更加简单。
2. 要想充分发挥处理器系统的强大计算能力，最简单的办法就是使用线程。

## 为什么要操作系统来实现多个程序的同时执行

1. **资源利用率**。在某些情况下，程序必须等待某个外部操作执行完成，例如输入操作或输出操作。而等待时程序无法执行其他工作，因此，在等待的同事可以运行另一个程序，可以提高资源的利用率。
2. **公平性**。不同用户的程序对于计算机的资源有着同等的使用权。一种高效地运行方式是通过粒度的时间分片（Time Slicing）使这些用户和城西能够共享计算机资源。
3. **便利性**。通常来说，在计算机多个任务时，应该编写多个程序，每个程序执行一个任务并在必要时互相通讯。这比只编写一个程序来计算所有任务更容易实现。

## 线程的优势

1. 发挥多处理器的强大能力
2. 建模的简单性
3. 异步时间的简化处理
4. 相应更灵敏的用户界面

## 线程带来的风险

1. **安全性。**永远不发生糟糕的事情。

2. **活跃性问题**。跳跃性指的是某件正确的事情最终会发生。 跳跃性问题发生在应用程序触及一种无法继续执行的状态。当某个操作无法继续执行下去时，就会发生跳跃性问题。在串行程序中，跳跃性问题的形式之一就是无意中造成的**无限循环**，从而使循环之后的代码得到执行，

3. **性能问题**。跳跃性意味着某件正确的事情最终会发生，但却不够好，因为我们通常希望正确的事情尽快发生。性能问题包含多个方面，如：服务时间过长，响应不够灵敏，吞吐率过低，资源消耗过高，或者可伸缩性较低等。

## Java 中如何使用线程

- Runnable 接口
- Thread 类(本质上是对 Runnable 接口的实现)
- Callable/Future 带返回值的线程()
- ThreadPool

线程可以合理利用多核心CPU 组员,提高程序的吞吐量

## Java Concurrency Tutorial

Covering whole java concurrency in single post is simply almost impossible. So, I have written below Java Concurrency Tutorials discussing one individual concept in single post. Go through these tutorials, and let me know if you have any questions or suggestions.

#### Java Concurrency Basics

[Concurrency Evolution](https://howtodoinjava.com/java/multi-threading/java-multi-threading-evolution-and-topics/)
[What is Thread Safety?](https://howtodoinjava.com/java/multi-threading/what-is-thread-safety/)
[Object level locking and class level locking](https://howtodoinjava.com/java/multi-threading/thread-synchronization-object-level-locking-and-class-level-locking/)
[Compare and Swap (CAS) Algorithm](https://howtodoinjava.com/java/multi-threading/compare-and-swap-cas-algorithm/)
[wait(), notify() and notifyAll() methods](https://howtodoinjava.com/java/multi-threading/how-to-work-with-wait-notify-and-notifyall-in-java/)

#### Difference between

[Difference between “implements Runnable” and “extends Thread”](https://howtodoinjava.com/java/multi-threading/difference-between-implements-runnable-and-extends-thread-in-java/)
[Difference between lock and monitor](https://howtodoinjava.com/java/multi-threading/multithreading-difference-between-lock-and-monitor/)
[Difference between yield() and join()](https://howtodoinjava.com/java/multi-threading/difference-between-yield-and-join-in-threads-in-java/)
[Difference between sleep() and wait()?](https://howtodoinjava.com/java/multi-threading/difference-between-sleep-and-wait/)

#### Executor Framework

[Executor framework tutorial](https://howtodoinjava.com/java-5/java-executor-framework-tutorial-and-best-practices/)
[ScheduledThreadPoolExecutor Example](https://howtodoinjava.com/2015/03/25/task-scheduling-with-executors-scheduledthreadpoolexecutor-example/)
[FixedSizeThreadPoolExecutor Example](https://howtodoinjava.com/java/multi-threading/java-fixed-size-thread-pool-executor-example/)
[ThreadPoolExecutor Example](https://howtodoinjava.com/java/multi-threading/java-thread-pool-executor-example/)
[ThreadPoolExecutor + Callable + Future Example](https://howtodoinjava.com/java/multi-threading/threadpoolexecutor-callable-future-example/)
[Throttling task submission rate using ThreadPoolExecutor and Semaphore](https://howtodoinjava.com/java/multi-threading/throttling-task-submission-rate-using-threadpoolexecutor-and-semaphore/)
[BlockingQueue Example](https://howtodoinjava.com/java-5/how-to-use-blockingqueue-and-threadpoolexecutor-in-java/)
[UncaughtExceptionHandler Example](https://howtodoinjava.com/java/multi-threading/how-to-restart-thread-using-uncaughtexceptionhandler/)

#### Advance Concurrency

[ForkJoinPool Example](https://howtodoinjava.com/java-7/forkjoin-framework-tutorial-forkjoinpool-example/)
[CountDownLatch Example](https://howtodoinjava.com/java/multi-threading/when-to-use-countdownlatch-java-concurrency-example-tutorial/)
[Control concurrent access using semaphore](https://howtodoinjava.com/java/multi-threading/control-concurrent-access-to-multiple-copies-of-a-resource-using-semaphore/)
[BinarySemaphore](https://howtodoinjava.com/java/multi-threading/binary-semaphore-tutorial-and-example/)
[java.util.concurrent.locks.Lock](https://howtodoinjava.com/java/multi-threading/how-to-use-locks-in-java-java-util-concurrent-locks-lock-tutorial-and-example/)
[java.util.concurrent.ThreadFactory](https://howtodoinjava.com/java/multi-threading/creating-threads-using-java-util-concurrent-threadfactory/)
[ThreadLocal Variables](https://howtodoinjava.com/java/multi-threading/when-and-how-to-use-thread-local-variables/)
[Inter-thread communication](https://howtodoinjava.com/java/multi-threading/inter-thread-communication-using-piped-streams-in-java/)

#### Concurrent Collections

[ConcurrentHashMap Example](https://howtodoinjava.com/java/collections/best-practices-for-using-concurrenthashmap/)
[ConcurrentLinkedDeque Example](https://howtodoinjava.com/java/multi-threading/non-blocking-thread-safe-list-concurrentlinkeddeque-example/)

#### Miscellaneous

[Creating and resolving Deadlock](https://howtodoinjava.com/java/multi-threading/writing-a-deadlock-and-resolving-in-java/)