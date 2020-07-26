# StampedLock

## 是什么

StampedLock 提供了三种模式的读写控制 :

- writeLock 写锁
- readLock 悲观读锁
- tryOptimisticRead 乐观读锁

当调用获取锁的系列函数时, 会返回一个 long 类型的变量, 叫做 戳记(stamp) . 这个 stamp 代表锁的状态

当调用 try-开头的获取锁的方法, 当获取失败时,会返回为 0 的戳记

**当调用释放锁和转换锁的方法时 需要传入获取锁时返回的 stamp 值**

#乐观读锁tryOptimisticRead

## 类图

![image-20200712111816081](../../../assets/image-20200712111816081.png)

## 组成

StampedLock 有三种状态,三种状态可以互相转换,

- [写锁writeLock](#写锁writeLock)
- [悲观读锁readLock](#悲观读锁readLock)
- [乐观读锁tryOptimisticRead](#乐观读锁tryOptimisticRead)

### 写锁writeLock

- 独占锁,但是不可以重入,请求读锁会返回一个stamp 变量用来表示该锁的版本

- 当释放该锁时需要调用 `unLockWrite` 方法并传递获取锁时的版本号 stamp

- 提供了 非阻塞 的 tryWriteLock 方法 。

```java
//StampedLockExample
public void incr(double value) {
  long stamp = stampedLock.writeLock();//获取读锁,返回版本号
  try {
    this.value += value;
  } finally {
    stampedLock.unlockWrite(stamp); // 释放写锁,传入版本号
  }
}
```

### 悲观读锁readLock

> 一个不可重入的共享锁 

是一个共享锁 ，在没有线程获取独占写锁的情况下，多个线程可以同时获取该锁 。

如果己经有线程持有写 锁，则其他线程请求获取该读锁会被阻塞，这类似于 `ReentrantReadWriteLock`的读锁 **(不同的是这里的读锁是不可重入锁〉**。 

这里说的悲观是指在 具体操作数据前其会悲观地认为其他线程可能要对自己操作的 数据进行修改，所以需要先对数据加锁，这是在读少写多的情况下的一种考虑 。

请求该锁成功后会返回一个 stamp 变量用来表示该锁的版本，当释放该锁时需要调用 `unlockRead` 方法并传递 `stamp` 参数。并且它提供了非阻塞的 `tryReadLock` 方法

### 乐观读锁tryOptimisticRead

它是相对于悲观锁来说的，在操作数据前并没有通过 CAS 设置锁的状态，仅仅通过位运算测试。

- 如果当前没有线程持有写锁 ，则简单地返回一个非0的stamp版本信息。 

- 获取该stamp后在具体操作数据前还需要调用 validate 方法验证 该 stamp 是否己经不可用，也就是看当调用 trγOptimisticRead 返回 stamp后到当前时间期间是否有其他线程持有了写锁，如果是则 validate会返回 o, 否则就可以使用该 stamp 版本的锁对数据进行操作 。

由于 tryOptimisticRead 并没有 使用 CAS 设置锁状态，所以不需要显式地释放该锁 。 

该锁的一个特点是适用于读多写少的场景 ，因为获取读锁只是使用位操作进行检验，不涉及 CAS 操作，所以效率会高很多，但是同时由于没有使用真正的锁，在保证数据 一致性上需要复制一份要操作的变量到方法栈，并且在操作数据时可能其他写线程己经修改了数据，而我们操作的是方法栈里面的数据，也就是一个快照，所以最多返回 的不是最新的数据，但是一致性还是得到保障的 。

## StampedLock 三种锁转换

StampedLock 支持是那种锁再一定条件下进行相互转换, 例如

```java
long tryConvertWriteLock(long stamp);
```

期望吧 stamp 标识的锁升级为写锁, 这个函数会在下面几种情况下返回一个有效的 stamp , 也就是晋升写锁层高

- 当前锁已经是写模式了
- 当前锁出于读锁模式,并且没有其他线程是读锁模式
- 当前出于乐观读模式,并且当前写锁可用

## StampedLock都是不可重入锁

StampedLock 的读写锁都是不可重入锁,所以在获取锁之后释放锁前不应该再调用会获取锁的操作, 以避免调用线程被阻塞

当多个线程同时尝试获取读锁和写锁时, 谁先获取锁没有一定的规则,完全是尽力而为,是随机的

StampedLock 锁并不直接实现 Lock 或者 ReadWriteLock 接口, 而是在内部维护了一个双向阻塞队列

## 三种锁实例

TestStampedLock类里有两个成员变量x,y，组成一个坐标，实例化一个StampedLock对象来保证操作的原子性。

```java
public class TestStampedLock {

    private double x,y;
    private final StampedLock sl = new StampedLock();

}

```

#### move方法：

改变x,y的值。首先获取了写锁，然后修改x,y的值，最后释放写锁。由于StampedLock的写锁是独占锁，当其他线程调用move方法时，会被阻塞。也保证了其他线程不能获取读锁来读取x,y的值，保证了对x,y操作的原子性和数据的一致性。

```java
  /**
     * 独占锁
     *
     * @param deltaX
     * @param deltaY
     */
    public void move(double deltaX, double deltaY) {
        long stamp = sl.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            sl.unlockWrite(stamp);
        }
    }
```

#### distanceFromOrigin方法：

计算当前坐标到原点的距离。(1)获取了乐观读锁，如果当前没有其他线程获取到了写锁，那么（1）的返回值就是非0，（2）复制了坐标变量到本地方法栈中。

(3)检查(1)获取到的stamp值是否还有效。之所以要检测，是因为（1）获取乐观读锁的时候没有通过CAS修改状态，而是通过为运算符返回一个stamp，在这里校验是看在获取stamp后判断前是否有其他线程持有写锁，如果有的话，则stamp无效。

（7）在计算期间，也有可能其他线程在这段时间里获取了写锁，并修改了x,y值，而（7）操作的是方法栈里的值，也就是快照而已，并不是最新的值。

（3）校验失败后，会获取悲观读锁，这时候如果有其他线程持有了写锁，则（4）会一直阻塞至其他线程释放了写锁，否则，当前线程获取到了读锁，执行（5）（6）。代码在执行（5）的时候，由于加了读锁，所以在这期间其他线程获取写锁的时候会阻塞，这保证了数据的一致性。

另外，这里的x,y没有被声明volatile会不会内存不可见，答案是不会的，因为加锁的语义保存了内存可见性。

当然，最后计算的值，依然有可能不是最新的。

```java
    /**
     * 乐观锁
     */
    public double distanceFromOrigin() {

        // (1)尝试获取乐观读锁
        long stamp = sl.tryOptimisticRead();
        // (2)将变量复制到方法栈中
        double currentX = x, currentY = y;
        // (3)检查获取读锁后，锁有没被其他线程排他性占抢
        if (!sl.validate(stamp)) {
            // (4)如果被抢占则获取一个共享读锁
            stamp = sl.readLock();
            try {
                // (5) 将成员变量复制到方法体内
                currentX = x;
                currentY = y;
            } finally {
                // (6) 释放共享读锁
                sl.unlockRead(stamp);
            }
        }
        // (7)返回计算结果
        return Math.sqrt(currentX * currentX + currentY * currentY);

    }
```

#### moveAtOrigin方法：

如果当前坐标在原点，则移动坐标。(1)获取悲观读锁，保证其他线程不能获取写锁来修改x,y的值。(2)判断是否在原点，是的话，则（3）尝试升级读锁为写锁，因为这时候可能有多个线程持有该悲观读锁，所以不一定能升级成功。当多个线程都执行到（3）时，则只有一个可以升级成功，然后执行（4）更新stamp，修改坐标值，退出循环。失败的话，执行（5），先释放读锁，再申请写锁，再循环。最后执行（6）释放锁。

```java

    /**
     * 使用悲观锁获取读锁，并尝试转换为写锁
     */
    public void moveAtOrigin(double newX, double newY) {
        // (1)
        long stamp = sl.readLock();
        try {
            // （2）如果当前点在原点则移动
            while (x == 0.0 && y == 0.0) {
                // （3）尝试将获取的读锁升级为写锁
                long ws = sl.tryConvertToWriteLock(stamp);
                if (ws != 0L) {
                    // （4）升级成功，更新戳记，并设置坐标，退出循环
                    stamp = ws;
                    x = newX;
                    y = newY;
                    break;
                } else {
                    // （5）读锁升级写锁失败，显示获取独占锁，循环重试
                    sl.unlockRead(stamp);
                    stamp = sl.writeLock();
                }
            }
        } finally {
          //(6)
            sl.unlock(stamp);
        }

    }
```

这里在使用乐观锁的时候，要考虑得比较多，必须要保证以下顺序：

```java
// 非阻塞获取乐观锁
long stamp = lock.tryOptimisticRead();
// 复制变量到本地方法栈中
copy();
// 校验stamp是否生效
if (!validate(stamp)) {
    // 获取悲观读锁
    long ws = lock.readLock();
    try {
        // 复制变量到本地方法栈中
        copy();
    } finally {
        // 释放悲观锁
        lock.unlock(stamp);
    }
}
```

## 三. StampedLock总结

1. 所有获取锁的方法，都会返回一个stamp戳记，stamp为0表示获取失败，其余均表示成功。
2. 所有释放锁的方法，都需要一个stamp戳记，这个stamp必须和成功获取锁时返回的stamp一样。
3. StampedLock是不可重入的锁，并且读性能比ReentrantReadWriteLock好，
4. StampedLock支持读锁和写锁的互相转换。
5. ReentrantReadWriteLock的锁被占用的时候，如果其他线程尝试获取写锁的时候，会被阻塞，但是，StampedLock在乐观获取锁后，其他线程尝试获取写锁，也不会被阻塞，这其实是对读锁的优化，所以，在获取乐观读锁后，还需要对结果进行校验。