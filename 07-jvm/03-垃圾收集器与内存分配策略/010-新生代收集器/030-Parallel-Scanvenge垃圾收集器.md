# 030-新生代-Parallel-Scanvenge垃圾收集器

[TOC]

## 图示

<img src="../../../assets/image-20200908105903706.png" alt="image-20200908105903706" style="zoom:67%;" />

## ParallelGC特点

新生代ParallelGC回收器也是使用复制算法的回收器,它和ParNew 回收器一样,都是多线程、独占式的回收器,但是 ParallelGC 回收器的一个重要特点 : **更加关注吞吐量**

总而言之言而总之:

- 复制算法
- 支持并行收集的多线程收集器
- 自适应调节策略 (垃圾收集器区别于 parNew 的重要区别)

```
$ java -XX:+PrintCommandLineFlags -version
-XX:InitialHeapSize=536870912 -XX:MaxHeapSize=8589934592 -XX:+PrintCommandLineFlags -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseParallelGC 
java version "1.8.0_201"
Java(TM) SE Runtime Environment (build 1.8.0_201-b09)
Java HotSpot(TM) 64-Bit Server VM (build 25.201-b09, mixed mode)
```

## 常用参数

| 参数                 | 关注点       | 解释                                                         |
| -------------------- | ------------ | ------------------------------------------------------------ |
| -XX:+UseParallelGC   |              | 新生代使用ParallelGC回收器,老年代使用串行回收器              |
| XX:+UseParallelOldGC |              | 新生代使用ParallelGC回收期,老年代使用ParallelOldGC回收器,ParallelGC回收器提供了两个重要的参数用于控制系统的吞吐量 |
| -XX:MaxGCPauseMills  | 期望停顿时间 | 设置最大垃圾收集停顿时间, 默认值是0, ParallelGC在工作时,会调整Java堆大小或者其他参数,尽可能地把停顿时间控制在MaxGCPauseMills 以内 , 如果读者希望减少停顿时间而把这个值设置的很小,为了达到预期的停顿时间,虚拟机会使用一个较小的堆(一个小的堆比一个大的堆回收的更快),而这样会导致垃圾收集变得很频繁,从而增加垃圾回收总时间,降低吞吐量 |
| -XX:GCTimeRatio      | 吞吐量       | 设置吞吐量大小,它的值是一个0-100的值,<br />假设 GCTimeRatio 的值为n,那么系统将花费不超过 1/(n+1)的时间进行垃圾收集<br />如果 GCTimeRatio 等于19 (默认) 则系统用于垃圾收集的时间不超过 1/(1+19) = 5%<br />默认情况下,它的取值是99,即有不超过1/(1+99) = 1% 的时间用于垃圾收集 |

**值得注意的 期望停顿时间 和 吞吐量 是互斥的,鱼和熊掌不可兼得, 这两个参数是互相矛盾的,通常如果减少了最大停顿时间 就会同时减少系统吞吐量 ,增加系统吞吐量又可能会同时增加一次垃圾收集的最大停顿时间**

除此之外,还可以设置 `-XX:+UseAdaptiveSizePolicy` 可以打开自适应GC策略 

## 自适应GC策略 

在这种模式下,新生代的大小、 Eden区 和 survivor区的比例 ,晋升老年代的对象年龄等参数会被自动调整, 以达到堆大小、吞吐量和停顿时间之间的平衡点

在手工调优比较困难的场合,可以直接使用这种自适应的方式,仅仅指定

- 虚拟机的最大堆
- 目标吞吐量(-XX:GCTimeRatio参数)
- 停顿时间(-XX:MaxGCPauseMillis)
- [控制自适应调节策略](#控制自适应调节策略)开启的`UseAdaptiveSizePolicy`参数

让虚拟机自己完成调优工作

> 自适应调节策略是 Parallel Scanvenge 收集器区别于 ParNew 收集器的重要特征

## 吞吐量

```java
吞吐量 = 运行用户代码时间/(运行用户代码时间 + 垃圾收集时间)
```

比如 用户代码加上垃圾收集时间是 100 分钟,垃圾收集使用了 1 分钟, 那么吞吐量就是 99%

## 控制最大垃圾收集停顿时间

`-XX:MAXGCPauseMillis `参数 允许一个大于 0 的值

垃圾收集器将**尽量**保证内存回收花费的时间不超过用户设定的值, 垃圾收集器的停顿时间缩短是以牺牲吞吐量和新生代空间为代价换取的:

比如系统把新生代调的小一点,收集 300M 肯定比收集500 M 新生代块,但是也导致了垃圾收集发生得更加频繁, 原来 10s 收集一次,每次停顿 100 毫秒,现在变为 5s 收集一次,停顿 70ms,停顿时间确实下降了,但是吞吐量也下降了

