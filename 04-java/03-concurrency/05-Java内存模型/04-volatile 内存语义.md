## Volatile

声明变量为 volatile 后,对这个变量的读和写可以看成是使用了同一个锁对这些单个读/写操作做了同步

确保了变量自身的:

- 可见性 : 对一个 volatile 变量的读,总是能看到(任意线程)对这个 volatile 变量最后的写入
- 原子性 : 对任意单个 volatile 变量的读/写具有原子性,但类似于 volatile++ 这种复合操作不具备原子性

```java
class VolatileFeaturesExample {
    volatile long vl = 0L; //使用volatile声明64位的long型变量

    public void set(long l) {
        vl = l; //单个volatile变量的写
    }

    public void getAndIncrement() {
        vl++; //复合（多个）volatile变量的读/写
    }

    public long get() {
        return vl; //单个volatile变量的读
    }
}

```

 使用 volatile 修饰后,就相当于 在 set 方法和 get 方法上加了`synchronized`

```java
class VolatileFeaturesExample1 {
    long vl = 0L; // 64位的long型普通变量

    public synchronized void set(long l) {//对单个的普通变量的写用同一个锁同步
        vl = l;
    }

    public void getAndIncrement() { //普通方法调用
        long temp = get(); //调用已同步的读方法
        temp += 1L; //普通写操作
        set(temp); //调用已同步的写方法
    }

    public synchronized long get() { //对单个的普通变量的读用同一个锁同步
        return vl;
    }
}

```

锁的 happens-before 规则保证释放锁和获取锁的两个线程之间的内存可见性,这意味着对一个 volatile 变量的读总能看到(任意线程)对这个 volatile 变量的最后的写

## 内存语义

**当写一个 volatile 变量时,JMM 会把线程对应的本地内存中的共享变量值刷新到主内存**

**当读一个 volatile 变量时,JMM会把该线程的对应的本地内存职位无效,线程只能去主内存中读取共享变量**

## 内存语义的实现

| 是否能够重排序 | 第二个操作 | 第二个操作  | 第二个操作  |
| -------------- | ---------- | ----------- | ----------- |
| 第一个操作     | 普通读/写  | Volatile 读 | volatile 写 |
| 普通读/写      |            |             | NO          |
| Volatile 读    | NO         | NO          | NO          |
| Volatile 写    |            | NO          | NO          |

- 当第二个操作是写 volatile 时, 不管第一个操作是什么,都不能重排序,确保写之前操作不会被编译器重排序到写之后
- 当第一个操作是读 volatile 时,不管第二个操作时什么,都不能重排序,确保读之后操作不会被编译器重排序到读之前
- 当第一个操作是写 volatile 时, 第二个操作时 volatile 读时,不能重排序

怎么实现的呢,内存拼装

在指令序列中插入内存屏障来禁止特定类型的处理器重排序

- 在每一个 volatile 写操作的前面插入一个 `StoreStore` 屏障,后面插入一个 `StoreLoad` 屏障
- 在每一个 volatile 读操作的后面插入一个 `LoadLoad` 屏障 和 一个  `LoadStore` 屏障

| 屏障类型           | 指令示意                     | 备注                                                         |
| ------------------ | ---------------------------- | ------------------------------------------------------------ |
| LoadLoadBarriers   | load1;LoadLoad;Load2         | 确保 load1数据的装载优先于 load2 以及所有后续装载指令的装载  |
| StoreStoreBarriers | Store1;storestore;store2     | 确保 store1数据对其他处理器可见优先于 store2 以及所有后续存储指令的存储 |
| LoadStoreBarriers  | `load1`;`loadstore`;`store2` | 确保`load1`数据装载优先于 `store2`以及后续的存储指令刷新到内存 |
| StoreLoadBarriers  | `store1`;`storeload`;`load2` | 确保 store1 数据对其他处理器变得可见,优先于 load2 以及所有后续装载指令的装载;这条内存屏障指令是一个全能型的屏障 |