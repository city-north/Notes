# Parallel 收集器GC日志

## 目录

- [图示](#图示)
- [日志命令行](#日志命令行)



## 图示

<img src="../../assets/image-20200908105903706.png" alt="image-20200908105903706" style="zoom:67%;" />

从图中可以看出, Parallel Scanvenge 收集器和 以下收集器可以配合

- Parallel Old 基于标记 -整理算法
- SerialOld 单线程标记整理算法

## 日志命令行

```
CommandLine flags: -XX:-BytecodeVerificationLocal -XX:-BytecodeVerificationRemote -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=dump.hprof -XX:InitialHeapSize=104857600 -XX:+ManagementServer -XX:MaxHeapSize=314572800 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:TieredStopAtLevel=1 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseParallelGC 
```

从上面的日志可以看出

- -XX:+UseParallelGC  使用的是 Parallel Scanvenge

```
2020-09-10T14:50:16.156-0800: 0.480: [GC (Allocation Failure) [PSYoungGen: 25600K->3350K(29696K)] 25600K->3366K(98304K), 0.0025312 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
```

从上面的日志可以看出

- 2020-09-10T14:50:16.156-0800   时间
- 0.480 时间偏移量
- [GC (Allocation Failure) 因为分配内存失败触发GC
- [PSYoungGen: 25600K->3350K(29696K)] 25600K->3366K(98304K), 0.0025312 secs]  年轻代中触发 ,耗时2.5ms



```
Java HotSpot(TM) 64-Bit Server VM (25.201-b09) for bsd-amd64 JRE (1.8.0_201-b09), built on Dec 15 2018 18:35:23 by "java_re" with gcc 4.2.1 (Based on Apple Inc. build 5658) (LLVM build 2336.11.00)
Memory: 4k page, physical 33554432k(3065236k free)

/proc/meminfo:

CommandLine flags: -XX:-BytecodeVerificationLocal -XX:-BytecodeVerificationRemote -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=dump.hprof -XX:InitialHeapSize=104857600 -XX:+ManagementServer -XX:MaxHeapSize=314572800 -XX:+PrintGC -XX:+PrintGCDateStamps -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:TieredStopAtLevel=1 -XX:+UseCompressedClassPointers -XX:+UseCompressedOops -XX:+UseParallelGC 

2020-09-10T14:50:16.156-0800: 0.480: [GC (Allocation Failure) [PSYoungGen: 25600K->3350K(29696K)] 25600K->3366K(98304K), 0.0025312 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
2020-09-10T14:50:16.294-0800: 0.619: [GC (Allocation Failure) [PSYoungGen: 28950K->4071K(29696K)] 28966K->4915K(98304K), 0.0043108 secs] [Times: user=0.01 sys=0.01, real=0.00 secs] 
2020-09-10T14:50:16.429-0800: 0.753: [GC (Allocation Failure) [PSYoungGen: 29671K->4066K(29696K)] 30515K->6544K(98304K), 0.0057692 secs] [Times: user=0.02 sys=0.01, real=0.01 secs] 
2020-09-10T14:50:16.641-0800: 0.966: [GC (Allocation Failure) [PSYoungGen: 29666K->4085K(55296K)] 32144K->8583K(123904K), 0.0043121 secs] [Times: user=0.02 sys=0.00, real=0.00 secs] 
2020-09-10T14:50:16.900-0800: 1.224: [GC (Metadata GC Threshold) [PSYoungGen: 46220K->4064K(55296K)] 50718K->12549K(123904K), 0.0053176 secs] [Times: user=0.02 sys=0.00, real=0.00 secs] 
2020-09-10T14:50:16.905-0800: 1.230: [Full GC (Metadata GC Threshold) [PSYoungGen: 4064K->0K(55296K)] [ParOldGen: 8485K->8090K(68608K)] 12549K->8090K(123904K), [Metaspace: 20397K->20395K(1067008K)], 0.0176113 secs] [Times: user=0.05 sys=0.00, real=0.02 secs] 
2020-09-10T14:50:17.250-0800: 1.575: [GC (Allocation Failure) [PSYoungGen: 51200K->6159K(88576K)] 59290K->14258K(157184K), 0.0052616 secs] [Times: user=0.01 sys=0.00, real=0.00 secs] 
2020-09-10T14:54:17.839-0800: 242.163: [GC (Allocation Failure) [PSYoungGen: 88079K->7590K(91136K)] 96178K->15697K(159744K), 0.0190221 secs] [Times: user=0.05 sys=0.01, real=0.02 secs] 
2020-09-10T14:55:27.850-0800: 312.174: [GC (Allocation Failure) [PSYoungGen: 90022K->9184K(51200K)] 98129K->62276K(119808K), 0.0580415 secs] [Times: user=0.22 sys=0.03, real=0.05 secs] 
2020-09-10T14:55:27.908-0800: 312.232: [Full GC (Ergonomics) [PSYoungGen: 9184K->0K(51200K)] [ParOldGen: 53092K->56458K(130048K)] 62276K->56458K(181248K), [Metaspace: 28790K->28619K(1075200K)], 0.4225591 secs] [Times: user=1.46 sys=0.01, real=0.43 secs] 
2020-09-10T14:55:28.339-0800: 312.663: [GC (Allocation Failure) [PSYoungGen: 33855K->30198K(72192K)] 90313K->100144K(202240K), 0.0249975 secs] [Times: user=0.13 sys=0.01, real=0.03 secs] 
2020-09-10T14:55:28.373-0800: 312.697: [GC (Allocation Failure) [PSYoungGen: 72182K->30200K(65024K)] 142128K->120610K(195072K), 0.0363489 secs] [Times: user=0.18 sys=0.01, real=0.03 secs] 
2020-09-10T14:55:28.417-0800: 312.741: [GC (Allocation Failure) [PSYoungGen: 65016K->33764K(68608K)] 155426K->139151K(198656K), 0.0333841 secs] [Times: user=0.18 sys=0.01, real=0.04 secs] 
2020-09-10T14:55:28.450-0800: 312.774: [Full GC (Ergonomics) [PSYoungGen: 33764K->2018K(68608K)] [ParOldGen: 105386K->129718K(204800K)] 139151K->131737K(273408K), [Metaspace: 28619K->28619K(1075200K)], 0.8030104 secs] [Times: user=2.94 sys=0.03, real=0.80 secs] 
2020-09-10T14:55:29.264-0800: 313.588: [GC (Allocation Failure) [PSYoungGen: 36834K->33792K(68608K)] 166553K->166142K(273408K), 0.1234827 secs] [Times: user=0.50 sys=0.00, real=0.12 secs] 
2020-09-10T14:55:29.413-0800: 313.737: [GC (Allocation Failure) [PSYoungGen: 68608K->33792K(68608K)] 237013K->236973K(273408K), 0.1234044 secs] [Times: user=0.67 sys=0.02, real=0.12 secs] 
2020-09-10T14:55:29.537-0800: 313.861: [Full GC (Ergonomics) [PSYoungGen: 33792K->8184K(68608K)] [ParOldGen: 203181K->204470K(204800K)] 236973K->212654K(273408K), [Metaspace: 28619K->28619K(1075200K)], 1.2280477 secs] [Times: user=5.25 sys=0.02, real=1.23 secs] 
2020-09-10T14:55:30.775-0800: 315.099: [Full GC (Ergonomics) [PSYoungGen: 43000K->42518K(68608K)] [ParOldGen: 204470K->204470K(204800K)] 247470K->246989K(273408K), [Metaspace: 28619K->28619K(1075200K)], 1.2951071 secs] [Times: user=5.92 sys=0.02, real=1.30 secs] 
2020-09-10T14:55:32.071-0800: 316.395: [Full GC (Ergonomics) [PSYoungGen: 43000K->42793K(68608K)] [ParOldGen: 204470K->204393K(204800K)] 247470K->247187K(273408K), [Metaspace: 28619K->28600K(1075200K)], 1.3761765 secs] [Times: user=6.35 sys=0.07, real=1.37 secs] 
```

- 