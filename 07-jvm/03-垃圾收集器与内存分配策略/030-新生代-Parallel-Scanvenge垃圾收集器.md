# ParallelScanvenge 垃圾收集器

>  为了提高新生代垃圾收集效率而设计的垃圾收集器

Parallel Scanvenge 基于多线程复制算法实现,在系统吞吐量上有很大的优化,可以更高效的利用 CPU 尽快完成垃圾收集任务

- 通过是自适应调节策略提高吞吐量,有三个参数可以调节
  - 控制最大垃圾收集停顿时间 `-XX:MaxGCPauseMillis`
  - 控制吞吐量大小`-XX:GCTimeRatio`参数
  - 控制自适应调节策略开启与菲欧的`UseAdaptiveSizePolicy`参数