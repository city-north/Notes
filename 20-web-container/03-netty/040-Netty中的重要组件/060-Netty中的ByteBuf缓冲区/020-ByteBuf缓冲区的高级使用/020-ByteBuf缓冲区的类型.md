# 020-ByteBuf缓冲区的类型

[TOC]

## Heap ByteBuf和Direct ByteBuf

| 类型            | 说明                                                         | 优点                                                         | 不足                                                         |
| --------------- | ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| Heap ByteBuf    | 内部数据为一个Java数组, 存储在JVM的堆空间中, 通过hasArray()来判断是否是缓冲区 | 快速分配和释放                                               | 写入底层通道之前复制到直接内存                               |
| Direct ByteBuf  | 内部数据存储在操作系统的物理内存中                           | 能够获取超过JVM限制大小的内存空间,写入传输通道比堆缓冲区更快 | 释放和分配空间昂贵(使用系统的方法)在Java操作时需要复制到堆上 |
| CompositeBuffer | 多个缓冲区的组合表示                                         | 方便一次操作多个缓冲区实例                                   |                                                              |

上面3中缓冲区, 都有池化(Pooled)和非池化(UnPooled)两种分配器来创建和分配内存空间

## Direct ByteBuf

- Direct Memory 不属于 Java堆内存, 所分配的内存其实是调用操作系统malloc函数得到的, 由 Netty的本地内存堆Native堆进行管理
- Direct Memory 容量可以通过 -XX:MaxDirectMemorySize来指定, 如果不指定, 则默认与Java堆的最大值(-Xms指定)一样 (注意不是强制要求, 有的JVM默认的Direct Memory与 -Xms无任何关系)

## 创建效率

- 在频繁需要创建缓冲区的场合, 由于创建和销毁Direct Buffer 直接缓冲区的代价非常高昂, 因此不宜使用Direct Buffer, 也就是说, Direct Buffer尽量在池化器中分配和回收, 如果能将Direct Buffer进行复用, 在读写频繁的情况下, 就可以大幅度改善性能了

## 读写速率

- 对Direct Buffer的读写比 Heap Bufer快, 但是它的创建和销毁比普通Heap Buffer 慢

### 垃圾回收

在Java的垃圾回收器回收Java堆时, Netty框架也会释放不再使用的Direct Buffer缓冲区, 因为它的内存为堆外内存, 所以清理的工作不会为Java虚拟机(JVM)带来压力

- 垃圾回收仅在Java堆被填满时, 以至于无法对新的堆分配请求提供服务时, 才发生
- Java应用程序中调用System.gc 函数来释放内存