# 使用Condition

任意一个 Java 对象,都有一组监视器方法

- Object.wait()
- Object.wait(long timeout)
- Object.notify()
- Object.notifyAll()

这些方法与 sychronized 关键字配合,可以实现等待/通知模式

Condition 也提供了类似 Object 的监视器方法:

- condtion.await()
- condtion.awaitUninterruptibly()
- condtion.awaitNanos
- condtion.awaitUntil(Date deadline)
- condtion.signal()
- condtion.signalAll()

但是使用方式和性能上还是有差异的

## Object 的监视器方法和 Condition 接口的对比

| 对比项                                              | Object Monitor Method           | Condition                                                    |
| --------------------------------------------------- | ------------------------------- | ------------------------------------------------------------ |
| 前置条件                                            | 获取对象的锁                    | 调用 Lock.lock()获取锁,</br>调用 Lock.newCondition()获取 Condition |
| 调用方式                                            | 直接调用 `Object.wait`          | `Condition.await()`                                          |
| 等待队列个数                                        | 一个                            | 多个                                                         |
| 当前线程释放锁并进入等待状态                        | 支持                            | 支持                                                         |
| 当前线程释放锁并进入等待状态,在等待状态中不响应中断 | 不支持                          | 支持`condtion.awaitUninterruptibly()`                        |
| 当前线程释放锁并进入等待状态到未来的某个时间        | 支持`Object.wait(long timeout)` | 支持`condtion.awaitNanos`                                    |
| 当前线程释放锁并进入等待状态将来的某个时间          | 不支持                          | 支持`condtion.awaitUntil(Date deadline)`                     |
| 唤醒等待队列中的全部线程                            | 支持`Object.notifyAll()`        | 支持`condtion.signalAll()`                                   |
| 唤醒等待队列中的一个线程                            | 支持`Object.notify()`           | 支持`condtion.signal()`                                      |

​	使用`ReentrantLock`比直接使用`synchronized`更安全，可以替代`synchronized`进行线程同步。

但是，`synchronized`可以配合`wait`和`notify`实现线程在条件不满足时等待，条件满足时唤醒，用`ReentrantLock`我们怎么编写`wait`和`notify`的功能呢？

答案是使用`Condition`对象来实现`wait`和`notify`的功能。

我们仍然以`TaskQueue`为例，把前面用`synchronized`实现的功能通过`ReentrantLock`和`Condition`来实现：

```java
class TaskQueue {
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private Queue<String> queue = new LinkedList<>();

    public void addTask(String s) {
        lock.lock();
        try {
            queue.add(s);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public String getTask() {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                condition.await();
            }
            return queue.remove();
        } finally {
            lock.unlock();
        }
    }
}
```

可见，使用`Condition`时，引用的`Condition`对象必须从`Lock`实例的`newCondition()`返回，这样才能获得一个绑定了`Lock`实例的`Condition`实例。

`Condition`提供的`await()`、`signal()`、`signalAll()`原理和`synchronized`锁对象的`wait()`、`notify()`、`notifyAll()`是一致的，并且其行为也是一样的：

- `await()`会释放当前锁，进入等待状态；
- `signal()`会唤醒某个等待线程；
- `signalAll()`会唤醒所有等待线程；
- 唤醒线程从`await()`返回后需要重新获得锁。

此外，和`tryLock()`类似，`await()`可以在等待指定时间后，如果还没有被其他线程通过`signal()`或`signalAll()`唤醒，可以自己醒来：

```java
if (condition.await(1, TimeUnit.SECOND)) {
    // 被其他线程唤醒
} else {
    // 指定时间内没有被其他线程唤醒
}
```

可见，使用`Condition`配合`Lock`，我们可以实现更灵活的线程同步。

- `Condition`可以替代`wait`和`notify`；
- `Condition`对象必须从`Lock`对象获取。

![image-20200326221118794](assets/image-20200326221118794.png)