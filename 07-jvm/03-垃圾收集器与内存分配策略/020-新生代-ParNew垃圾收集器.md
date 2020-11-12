# ParNew 垃圾收集器

<img src="../../assets/image-20200908105903706.png" alt="image-20200908105903706" style="zoom:67%;" />

并行回收器在串行回收器的基础上，增加了多个线程同时进行回收，对于并行能力强的计算机，可以有效减少垃圾收集所需要的实际时间

> ParNew 垃圾收集器是 Serial 垃圾收集器的多线程实现,同样采用了复制算法,它采用多线程工作模式,除此之外和 Serial 收集器几乎一样,ParNew 收集器在垃圾收集过程中会暂停所有其他工作线程,是 Java 虚拟机运行在 Server 模式的默认收集器
>
> ParNew 垃圾收集器默认开启与 CPU同等量级的线程进行垃圾回收,在 Java 启动时可以通过`-XX:ParallelGCThreads`参数调节 ParNew 垃圾回收期的工作线程数

- 多线程并行版本的 Serial

- 是 Server 模式下**新生代**的默认垃圾收集器

- 可以通过`-XX:ParallelGCThreads`参数调节 ParNew 垃圾回收期的工作线程数

- 除了 Serial 收集器之外,目前只有它能与 收集器CMS (老年代)配合工作

  > JDK9 开始,parNew+CMS已经不是官方推荐的组合了, G1 替代了他们

<img src="../../assets/image-20200526230058404.png" alt="image-20200526230058404" style="zoom:50%;" />

## 特点

| 特点     | 解释                                                |
| -------- | --------------------------------------------------- |
| 优点     | 在多CPU时，比Serial效率高                           |
| 缺点     | 收集过程暂停所有应用程序线程，单CPU时比Serial效率差 |
| 算法     | 复制算法                                            |
| 适用范围 | 新生代                                              |
| 应用     | 运行在Server模式下的虚拟机中首选的新生代收集器      |

#### 使用参数

```
XX：+UseParNewGC//新生代使用ParNew回收器，老年代使用串行回收器Serial Old ,（JDK 9、JDK 10已经删除，因为ParNew需要和CMS搭配工作，而CMS已经被G1替代，不再支持此参数）
```

```java
-XX：+UseConcMarkSweepGC（JDK 9、JDK 10不建议使用，建议使用默认的G1垃圾回收器）：新生代使用ParNew回收器，老年代使用CMS。
```

#### 指定回收线程

ParNew回收器工作时的线程数量可以使用`-XX:ParallelGCThreads`参数指定。

一般，最好与CPU数量相当，避免过多的线程数影响垃圾回收性能。在默认情况下，

- 当CPU数量小于8时，ParallelGCThreads的值等于CPU数量
- 当CPU数量大于8时，ParallelGCThreads的值等于3+((5×CPU_Count)/8)

