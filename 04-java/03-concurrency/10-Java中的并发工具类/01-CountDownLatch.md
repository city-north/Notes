# 等待多线程完成的CountDownLatch

CountDownLatch 的构造函数接收一个 int 类型的参数作为计数器,你要等待 n个线程完成,就写 n,当然 n也可以是一个线程的 n 个执行步骤

- 当我们调用 CountDownLatch 的 countDown 方法的时候, n 就会减一

countdownlatch 是一个同步工具类，它允许一个或多个线程一直等待，直到其他线程的操作执行完毕再执行。从命 名可以解读到 countdown 是倒数的意思，类似于我们倒计 时的概念。

countdownlatch 提供了两个方法，一个是 countDown， 一个是 await， countdownlatch 初始化的时候需要传入一个整数，在这个整数倒数到 0 之前，调用了 await 方法的 程序都必须要等待，然后通过 countDown 来倒数。

```java
/**
 * <p>
 * A test for {@link java.util.concurrent.CountDownLatch}
 * </p>
 *
 * @author EricChen 2020/04/05 16:59
 */
public class CountDownLatchTest {

    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(3);

        new Thread(() -> {
            System.out.println("Thread:" + Thread.currentThread().getName());
            countDownLatch.countDown();
        }).start();
        new Thread(() -> {
            System.out.println("Thread:" + Thread.currentThread().getName());
            countDownLatch.countDown();
        }).start();
        new Thread(() -> {
            System.out.println("Thread:" + Thread.currentThread().getName());
            countDownLatch.countDown();
        }).start();
        countDownLatch.await();//所有线程执行完成后才会执行 WAITING 状态
    }
}

```

对于 CountDownLatch，我们仅仅需要关心两个方法，一 个是 countDown() 方法，另一个是 await() 方法。 countDown() 方法每次调用都会将 state 减 1，直到 state 的值为 0;而 await 是一个阻塞方法，当 state 减 为 0 的时候，await 方法才会返回。await 可以被多个线 程调用，大家在这个时候脑子里要有个图:所有调用了 await 方法的线程阻塞在 AQS 的阻塞队列中，等待条件 满足(state == 0)，将线程从队列中一个个唤醒过来。

acquireSharedInterruptibly
countdownlatch 也用到了 AQS，在 CountDownLatch 内 部写了一个 Sync 并且继承了 AQS 这个抽象类重写了 AQS 中的共享锁方法。首先看到下面这个代码，这块代码主要 是判断当前线程是否获取到了共享锁;(在 CountDownLatch 中，使用的是共享锁机制，因为 CountDownLatch 并不需要实现互斥的特性)

doAcquireSharedInterruptibly

1. addWaiter 设置为 shared 模式。
2. tryAcquire 和 tryAcquireShared 的返回值不同，因此会多出一个判断过程
   3. 在 判 断 前 驱 节 点 是 头 节 点 后 ， 调 用 了
   setHeadAndPropagate 方法，而不是简单的更新一下头 节点。

#### 图解分析

加入这个时候有 3 个线程调用了 await 方法，由于这个时 候 state 的值还不为 0，所以这三个线程都会加入到 AQS 队列中。并且三个线程都处于阻塞状态

`CountDownLatch.countDown`
由于线程被 await 方法阻塞了，所以只有等到 countdown 方法使得 state=0 的时候才会被唤醒，我们 来看看 countdown 做了什么

![image-20200707085445146](../../../assets/image-20200707085445146.png)

1. 只有当 state 减为 0 的时候，tryReleaseShared 才返
回 true, 否则只是简单的 state = state - 1
2. 如果 state=0, 则调用 doReleaseShared
唤醒处于 await 状态下的线程

#### doAcquireSharedInterruptibly

一旦 ThreadA 被唤醒，代码又会继续回到 doAcquireSharedInterruptibly 中来执行。如果当前 state 满足=0 的条件，则会执行 setHeadAndPropagate 方法

#### setHeadAndPropagate