# 读写锁 ReentrantReadWriteLock

之前说的锁都是排他锁:

- 同一时刻值允许一个线程进行访问, 写的时候不允许读,读的时候不允许写

读写锁:

- 写的时候不允许读
- 读的时候允许读



## ![image-20200326215358440](../../../assets/image-20200326215358440.png)

## 接口

除了有两个基础的方法外,:

-  获取读锁 readLock
- 获取写锁 writeLock

还提供了一些接口



![image-20200326215634624](../../../assets/image-20200326215634624.png)

```java
/**
 * <p>
 * Read Write 缓存
 * </p>
 *
 * @author EricChen 2020/03/26 21:57
 */
public class ReadWriteCache {
    private static Map<String, Object> map = new HashMap<>();
    private static ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private static Lock readLock = readWriteLock.readLock();
    private static Lock writeLock = readWriteLock.writeLock();
    private static volatile boolean update = false;


    public static final Object get(String key) {
        readLock.lock();
        try {
            return map.get(key);
        } finally {
            readLock.unlock();
        }
    }


    public static final void set(String key, Object object) {
        writeLock.lock();
        try {
            map.put(key, object);
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 锁降级
     */
    public static void processData() {
        readLock.lock();
        if (!update) {
            //先释放读锁再降级
            readLock.unlock();
            //锁降级为写锁
            writeLock.lock();
            try {
                if (!update) {
                    update = true;
                }
                readLock.lock();
            } finally {
                writeLock.unlock();
            }
            //锁降级完成
        }

    }
```

# 读写锁的设计

- 读写锁的设计
- 写锁的获取与释放
- 读锁的获取和释放
- 锁降级

## 读写状态的设计

读写锁同样依赖自定义同步器AQS 来实现的,而读写状态就是器同步器的同步状态

- ReentrantLock 中自定义同步器的同步状态, 是一个线程重复获取的次数,
- ReentrantReadWriteLock 在自定义同步器的同步状态上维护多个读线程和一个写线程状态

如果要在一个整型变量上维护多种状态,使用位图数据结构

**高 16 位代表读,第 16 位代表写**



- 写状态等于 S&0x0000FFFF , 将高 16 位全部抹去
- 读状态等于 S >>> 16  无符号补 0 右移16位 , 当写操作增加 1时, 等于 S+1 ,当读状态增加 1 时,等于 S + (1 << 16)  也就是等于 S+0x0010000



前面讲到的`ReentrantLock`保证了只有一个线程可以执行临界区代码：

```java
public class Counter {
    private final Lock lock = new ReentrantLock();
    private int[] counts = new int[10];

    public void inc(int index) {
        lock.lock();
        try {
            counts[index] += 1;
        } finally {
            lock.unlock();
        }
    }

    public int[] get() {
        lock.lock();
        try {
            return Arrays.copyOf(counts, counts.length);
        } finally {
            lock.unlock();
        }
    }
}
```

但是有些时候，这种保护有点过头。因为我们发现，任何时刻，只允许一个线程修改，也就是调用`inc()`方法是必须获取锁，但是，`get()`方法只读取数据，不修改数据，它实际上允许多个线程同时调用。

实际上我们想要的是：允许多个线程同时读，但只要有一个线程在写，其他线程就必须等待：

|      | 读     | 写     |
| :--- | :----- | ------ |
| 读   | 允许   | 不允许 |
| 写   | 不允许 | 不允许 |

使用`ReadWriteLock`可以解决这个问题，它保证：

- 只允许一个线程写入（其他线程既不能写入也不能读取）；
- 没有写入时，多个线程允许同时读（提高性能）。

用`ReadWriteLock`实现这个功能十分容易。我们需要创建一个`ReadWriteLock`实例，然后分别获取读锁和写锁;

```java
public class Counter {
    private final ReadWriteLock rwlock = new ReentrantReadWriteLock();
    private final Lock rlock = rwlock.readLock();
    private final Lock wlock = rwlock.writeLock();
    private int[] counts = new int[10];

    public void inc(int index) {
        wlock.lock(); // 加写锁
        try {
            counts[index] += 1;
        } finally {
            wlock.unlock(); // 释放写锁
        }
    }

    public int[] get() {
        rlock.lock(); // 加读锁
        try {
            return Arrays.copyOf(counts, counts.length);
        } finally {
            rlock.unlock(); // 释放读锁
        }
    }
```

把读写操作分别用读锁和写锁来加锁，在读取时，多个线程可以同时获得读锁，这样就大大提高了并发读的执行效率。

使用`ReadWriteLock`时，适用条件是同一个数据，有大量线程读取，但仅有少数线程修改。

例如，一个论坛的帖子，回复可以看做写入操作，它是不频繁的，但是，浏览可以看做读取操作，是非常频繁的，这种情况就可以使用`ReadWriteLock`。

使用`ReadWriteLock`可以提高读取效率：

- `ReadWriteLock`只允许一个线程写入；
- `ReadWriteLock`允许多个线程在没有写入时同时读取；
- `ReadWriteLock`适合读多写少的场景。

## 