# LockSupport

LockSupport 可以用来

- 阻塞一个线程
- 唤醒一个线程

**LockSupport 类与每个使用它的线程都会关联一个许可证,在默认情况下,调用 LockSupport 类的方法的线程是不持有许可证的**

> LockSupport 类 是使用 Unsafe 类实现的

## 重要的方法

- [park](#park)
- [unpark](#unpark(Thread thread 方法))
- parkNanos(long nanos)
- parkUntil(long deadline)

## park

- 如果调用 park 方法的线程已经拿到了与 LockSupport 关联的许可证 , 则这个线程调用 LockSupport.park 时会 的时候会立即返回
- 默认情况下调用线程不持有许可证

```java
public class LockSupportTest {

    public static void main(String[] args) {
        System.out.println("begin park");
        LockSupport.park();//   java.lang.Thread.State: WAITING (parking)
        System.out.println("end park");
    }
}
```

因为默认情况下调用线程不持有许可证,所以会进入等待状态

- 其他线程可以调用 `LockSuport.unpark(Thread 等待状态的线程)`方法时, thread 线程会立刻返回
- 其他线程调用` 等待状态的线程.interrupt()`方法时, 等待状态的线程不会抛出`InterruptedException`异常 ,然后立即返回

## unpark(Thread thread 方法)

- 当一个线程调用 unpark 时, 如果 参数 thread 没有持有 LockSupport 关联的许可证,则让 `thread` 持有
- 如果 thread 之前没有调用 park ,则调用 unpark 方法后, 再调用 park 方法, 其会直接返回

```java
public class UnparkTest {
    public static void main(String[] args) {
        System.out.println("begin park");

        //使当前线程获取到许可证
        LockSupport.unpark(Thread.currentThread());

        //再次调用 park 方法
        LockSupport.park();
        System.out.println("end park");

    }
}
// begin park
// end park
```

park 方法的返回值不会告诉你为什么返回.所以调用者需要根据之前调用 park 方法的原因,再次检查条件会否满足,不满足再次 park

例如根据调用者前后中断状态的对比就可以判断是不是因为被中断才返回的

```java
public class LockSupporttest3 {

    public static void main(String[] args) throws Exception {
        Thread thread = new Thread(() -> {
            System.out.println("child thread begin park");
            //确保只有中断返回,才相应
            while (!Thread.currentThread().isInterrupted()) {
                LockSupport.park();
            }
            System.out.println("child thread unpark");
        });
        thread.start();
        Thread.sleep(1000);
        System.out.println("main thread begin unpark");
        //中断子线程
        thread.interrupt();
    }
}
```

#### 先进先出锁

```java
public class FIFOMutex {

    private final AtomicBoolean locked = new AtomicBoolean(false);
    private final Queue<Thread> waiters = new ConcurrentLinkedDeque<>();

    public void Lock() {
        boolean wasInterrupted = false;
        final Thread current = Thread.currentThread();
        waiters.add(current);
        //如果当前线程不是或者当前锁已经被其他线程偶去,则调用 park 挂起自己
        while (waiters.peek() != current || !locked.compareAndSet(false, true)) {
            LockSupport.park(this);
            if (Thread.interrupted()) {
                //如果 park 方法是因为中断而返回,则忽略中断并重置中断标志,做个记号,然后再次判断当前线程是不是队首元素或者当前锁是否已经被其他线程获取,如果则继续调用 park 方法挂起自己
                wasInterrupted = true;
            }
        }
        waiters.remove();
        if (wasInterrupted) {
            //其他线程中断了线程,其他线程有可能对该状态感兴趣,所以回复下
            current.interrupt();
        }
    }
    public void unLock() {
        locked.set(false);
        LockSupport.unpark(waiters.peek());
    }
}
```







LockSupport 类是 Java6 引入的一个类，提供了基本的线程同步原语。LockSupport 实际上是调用了 Unsafe 类里的函数，归结到 Unsafe 里，只有两个函数

- unpark 函数为线程提供“许可(permit)”，线程调用 park 函数则等待“许可”。这个有点像信号量，但是这个“许可”是不能叠加的，“许可”是一次性的。
- permit 相当于 0/1 的开关，默认是 0，调用一次 unpark 就加 1 变成了 1.调用一次park 会消费 permit，又会变成 0。 如果再调用一次 park 会阻塞，因为 permit 已 经是 0 了。
- 直到 permit 变成 1.这时调用 unpark 会把 permit 设置为 1.每个线程都 有一个相关的 permit，permit 最多只有一个，重复调用 unpark 不会累积

![image-20200326220911476](../../../assets/image-20200326220911476.png)

