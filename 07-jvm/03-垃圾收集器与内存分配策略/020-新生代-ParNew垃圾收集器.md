# ParNew 垃圾收集器

> ParNew 垃圾收集器是 Serial 垃圾收集器的多线程实现,同样采用了复制算法,它采用多线程工作模式,除此之外和 Serial 收集器几乎一样,ParNew 收集器在垃圾收集过程中会暂停所有其他工作线程,是 Java 虚拟机运行在 Server 模式的默认收集器
>
> ParNew 垃圾收集器默认开启与 CPU同等量级的线程进行垃圾回收,在 Java 启动时可以通过`-XX:ParallelGCThreads`参数调节 ParNew 垃圾回收期的工作线程数

- 多线程并行版本的 Serial

- 是 Server 模式下**新生代**的默认垃圾收集器

- 可以通过`-XX:ParallelGCThreads`参数调节 ParNew 垃圾回收期的工作线程数

- 除了 Serial 收集器之外,目前只有它能与 收集器CMS (老年代)配合工作

  > JDK9 开始,parNew+CMS已经不是官方推荐的组合了, G1 替代了他们

<img src="../../assets/image-20200526230058404.png" alt="image-20200526230058404" style="zoom:50%;" />

