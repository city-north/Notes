# Concurrent 集合

我们在前面已经通过`ReentrantLock`和`Condition`实现了一个`BlockingQueue`：

```java
public class TaskQueue {
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

`BlockingQueue`的意思就是说，当一个线程调用这个`TaskQueue`的`getTask()`方法时，该方法内部可能会让线程变成等待状态，直到队列条件满足不为空，线程被唤醒后，`getTask()`方法才会返回。

因为`BlockingQueue`非常有用，所以我们不必自己编写，可以直接使用Java标准库的`java.util.concurrent`包提供的线程安全的集合：`ArrayBlockingQueue`。

除了`BlockingQueue`外，针对`List`、`Map`、`Set`、`Deque`等，`java.util.concurrent`包也提供了对应的并发集合类。我们归纳一下：

| interface | non-thread-safe             | thread-safe                                  |
| :-------- | :-------------------------- | :------------------------------------------- |
| List      | `ArrayList`                 | `CopyOnWriteArrayList`                       |
| Map       | `HashMap`                   | `ConcurrentHashMap`                          |
| Set       | `HashSet `/ `TreeSet`       | `CopyOnWriteArraySet`                        |
| Queue     | `ArrayDeque` / `LinkedList` | `ArrayBlockingQueue `/ `LinkedBlockingQueue` |
| Deque     | `ArrayDeque `/ `LinkedList` | `LinkedBlockingDeque`                        |

