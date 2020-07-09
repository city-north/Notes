# Semaphore

## 信号灯

- 支持 公平策略和非公平策略
- 限流 permits 令牌限流

semaphore 也就是我们常说的信号灯，semaphore 可以控制同时访问的线程个数，通过 acquire 获取一个许可，如 果没有就等待，通过 release 释放一个许可。有点类似限流 的作用。叫信号灯的原因也和他的用处有关，比如某商场 就 5 个停车位，每个停车位只能停一辆车，如果这个时候 来了 10 辆车，必须要等前面有空的车位才能进入。

### Semaphore 源码分析

创建 Semaphore 实例的时候，需要一个参数 permits， 

```java
    public Semaphore(int permits) {
        sync = new NonfairSync(permits);
    }
```

这个基本上可以确定是设置给 AQS 的 state 的，

然后每 个线程调用 acquire 的时候，执行` state = state - 1`，release 的时候执行` state = state + 1`，当然，acquire 的 时候，如果 state=0，说明没有资源了，需要等待其他线程 release。

**Semaphore 分公平策略和非公平策略**

## 使用

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

### NofairSync

通过对比发现公平和非公平的区别就在于是否多了一个 `hasQueuedPredecessors` 的判断

```java
static final class NonfairSync extends Sync {
  private static final long serialVersionUID = -2694183684443567898L;

  NonfairSync(int permits) {
    super(permits);
  }

  protected int tryAcquireShared(int acquires) {
    return nonfairTryAcquireShared(acquires);
  }
}
```

我们可以看到 Sync 中的 公平锁 的获取

```java
        protected int tryAcquireShared(int acquires) {
            for (;;) {
              //队列是是否有前驱节点,如果有需要等待
                if (hasQueuedPredecessors())
                    return -1;
                int available = getState();
                int remaining = available - acquires;
                if (remaining < 0 ||
                    compareAndSetState(available, remaining))
                    return remaining;
            }
        }
```

非公平锁获取

```java
        final int nonfairTryAcquireShared(int acquires) {
            for (;;) {
              //不判断是否有先驱节点
                int available = getState();
                int remaining = available - acquires;
                if (remaining < 0 ||
                    compareAndSetState(available, remaining))
                    return remaining;
            }
        }
```

## acquire

```java
    public void acquire(int permits) throws InterruptedException {
        if (permits < 0) throw new IllegalArgumentException();
        sync.acquireSharedInterruptibly(permits);
    }

    public final void acquireSharedInterruptibly(int arg)
            throws InterruptedException {
        if (Thread.interrupted())
            throw new InterruptedException();
        if (tryAcquireShared(arg) < 0)
            doAcquireSharedInterruptibly(arg);
    }
```

### release

```java
protected final boolean tryReleaseShared(int releases) {
  for (;;) {
    int current = getState();
    //加
    int next = current + releases;
    if (next < current) // overflow
      throw new Error("Maximum permit count exceeded");
    if (compareAndSetState(current, next))
      return true;
  }
}
```

### 值得注意的是

Semaphore 对锁的申请和释放和 ReentrantLock 类似,通过 acquire 方法和 release 方法来获取和释放许可信号资源,

`Semaphore.acquire` 方法默认和 `ReentrentLock.lockInterruptibly` 方法效果一样,为可响应中断锁,也就是说在等待许可信号资源的过程中可以被` Thread.interrupt `方法中断而取消对许可信号的申请

## 使用场景

- 对象池,资源池的构建, 比如静态全局对象池,数据库连接池等等
- 我们可以创建一个计数器为 1 的 `Semaphore`.将其作为一种互斥锁的机制(二元信号量,表示两种互斥的状态),同一时间只有一个线程可以获取该锁

