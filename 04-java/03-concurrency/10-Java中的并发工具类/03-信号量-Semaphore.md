# Semaphore

信号灯

semaphore 也就是我们常说的信号灯，semaphore 可以控制同时访问的线程个数，通过 acquire 获取一个许可，如 果没有就等待，通过 release 释放一个许可。有点类似限流 的作用。叫信号灯的原因也和他的用处有关，比如某商场 就 5 个停车位，每个停车位只能停一辆车，如果这个时候 来了 10 辆车，必须要等前面有空的车位才能进入。

### Semaphore 源码分析

从 Semaphore 的功能来看，我们基本能猜测到它的底层 实现一定是基于 AQS 的共享锁，因为需要实现多个线程共 享一个领排池

创建 Semaphore 实例的时候，需要一个参数 permits， 这个基本上可以确定是设置给 AQS 的 state 的，然后每 个线程调用 acquire 的时候，执行 state = state - 1，release 的时候执行 state = state + 1，当然，acquire 的 时候，如果 state=0，说明没有资源了，需要等待其他线 程 release。

Semaphore 分公平策略和非公平策略

### NofairSync

通过对比发现公平和非公平的区别就在于是否多了一个 hasQueuedPredecessors 的判断

### 



```java
public class SemaphoreTest {
    public static void main(String[] args) {
        Semaphore semaphore = new Semaphore(3);
        for (int i = 0; i < 10; i++) {
            new Car(i, semaphore).start();
        }
    }

    static class Car extends Thread {

        private int num;
        private Semaphore semaphore;

        public Car(int num, Semaphore semaphore) {
            this.num = num;
            this.semaphore = semaphore;
        }


        @Override
        public void run() {
            try {
                semaphore.acquire();//获取一个令牌(类似于停车卡)
                System.out.println("num:" + num + " ,got a parking space");
                TimeUnit.SECONDS.sleep(5);
                System.out.println("num" + num + " ,release a parking space");
                semaphore.release();//释放令牌(类似于停车卡)
            } catch (InterruptedException e) {
              可以打断
                e.printStackTrace();
            }
        }
    }
}

```

### 值得注意的是

Semaphore 对锁的申请和释放和 ReentrantLock 类似,通过 acquire 方法和 release 方法来获取和释放许可信号资源,

`Semaphore.acquire` 方法默认和 `ReentrentLock.lockInterruptibly` 方法效果一样,为可响应中断锁,也就是说在等待许可信号资源的过程中可以被` Thread.interrupt `方法中断而取消对许可信号的申请

## 使用场景

- 对象池,资源池的构建, 比如静态全局对象池,数据库连接池等等
- 我们可以创建一个计数器为 1 的 `Semaphore`.将其作为一种互斥锁的机制(二元信号量,表示两种互斥的状态),同一时间只有一个线程可以获取该锁

