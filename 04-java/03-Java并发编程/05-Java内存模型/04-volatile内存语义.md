# Volatile内存语义

## 目录

- [Volatile实现原理](#Volatile实现原理)

- [内存屏障步骤](#内存屏障步骤)

---

## Volatile实现原理

声明变量为 volatile 后,对这个变量的读和写可以看成是使用了内存屏障

确保了变量自身的:

- 可见性 : 对一个 volatile 变量的读, happens-before (任意线程)对这个 volatile 变量最后的写入
- 原子性 : 对任意单个 volatile 变量的读/写具有原子性,但类似于 volatile++ 这种复合操作不具备原子性

## 内存屏障步骤

- 在volatile写操作的前面插入一个StoreStore屏障。保证volatile写操作不会和之前的写操作重排序。
- 在volatile写操作的后面插入一个StoreLoad屏障。保证volatile写操作不会和之后的读操作重排序。
- 在volatile读操作的后面插入一个LoadLoad屏障+LoadStore屏障。保证volatile读操作不会和之后的读操作、写操作重排序。

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

怎么实现的呢,内存屏障

在指令序列中插入内存屏障来禁止特定类型的处理器重排序

- 在每一个 volatile 写操作的前面插入一个 `StoreStore` 屏障,后面插入一个 `StoreLoad` 屏障
- 在每一个 volatile 读操作的后面插入一个 `LoadLoad` 屏障 和 一个  `LoadStore` 屏障

| 屏障类型           | 指令示意                       | 备注                                                         |
| ------------------ | ------------------------------ | ------------------------------------------------------------ |
| LoadLoadBarriers   | `load1`;`LoadLoad`;`Load2`     | 确保 load1数据的装载优先于 load2 以及所有后续装载指令的装载  |
| StoreStoreBarriers | `Store1`;`storestore`;`store2` | 确保 store1数据对其他处理器可见优先于 store2 以及所有后续存储指令的存储 |
| LoadStoreBarriers  | `load1`;`loadstore`;`store2`   | 确保`load1`数据装载优先于 `store2`以及后续的存储指令刷新到内存 |
| StoreLoadBarriers  | `store1`;`storeload`;`load2`   | 确保 store1 数据对其他处理器变得可见,优先于 load2 以及所有后续装载指令的装载;这条内存屏障指令是一个全能型的屏障 |