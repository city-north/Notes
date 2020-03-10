# 使用StampedLock

前面介绍的`ReadWriteLock`可以解决多线程同时读，但只有一个线程能写的问题。

如果我们深入分析`ReadWriteLock`，会发现它有个潜在的问题：如果有线程正在读，写线程需要等待读线程释放锁后才能获取写锁，即读的过程中不允许写，这是一种悲观的读锁。

**要进一步提升并发执行效率，Java 8引入了新的读写锁**：`StampedLock`。

`StampedLock`和`ReadWriteLock`相比，改进之处在于：读的过程中也允许获取写锁后写入！这样一来，我们读的数据就可能不一致，所以，需要一点额外的代码来判断读的过程中是否有写入，这种读锁是一种乐观锁。

乐观锁的意思就是**乐观地估计读的过程中大概率不会有写入**，因此被称为乐观锁。反过来，悲观锁则是**读的过程中拒绝有写入**，也就是写入必须等待。显然乐观锁的并发效率更高，但一旦有小概率的写入导致读取的数据不一致，需要能检测出来，再读一遍就行。

```java
public class StampedLockExample {
    private final StampedLock stampedLock = new StampedLock();

    private double value = 0;


    public double getValue() {
        long stamp = stampedLock.tryOptimisticRead();
        if (stampedLock.validate(stamp)) {// 检查乐观读锁后是否有其他写锁发生
            return value;
        } else {
            try {
                stamp = stampedLock.readLock(); //获取一个悲观读锁
                return value;
            } finally {
                stampedLock.unlockRead(stamp);// 释放悲观读锁
            }
        }
    }

    public void incr(double value) {
        long stamp = stampedLock.writeLock();// 获取写锁
        try {
            this.value += value;
        } finally {
            stampedLock.unlockWrite(stamp);// 释放写锁
        }

    }
```

