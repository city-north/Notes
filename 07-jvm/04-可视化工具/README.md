# 概述

- 基础故障处理工具
  - jps : 虚拟机进程状况工具
  - jstat : 虚拟机统计信息监视工具
  - jinfo : Java 配置信息工具
  - jmap : Java 内存影像工具
  - jhat : 虚拟机堆转储快照分析工具
  - jstack : Java 堆栈跟踪工具

- 可视化故障处理工具

  - JHSDB : 基于服务型代理的调试工具
  - JConsole : Java 监视与管理控制台
  - VisualVM : 多合-故障处理工具
  - Java Mission Control 可持续在线监控工具

- HotSpot 虚拟机插件和工具

  - HSDIS : JIT 生成代码反汇编


## 实例

seata 默认运行时的参数有这些

```
 -Xmx4096m 
 -Xms4096m 
 -Xmn4096m 
 -Xss512k 
 -XX:SurvivorRatio=10 
 -XX:MetaspaceSize=128m 
 -XX:MaxMetaspaceSize=256m 
 -XX:MaxDirectMemorySize=4096m 
 -XX:-OmitStackTraceInFastThrow 
 -XX:-UseAdaptiveSizePolicy 
 -XX:+HeapDumpOnOutOfMemoryError 
 -XX:HeapDumpPath=/data/target/logs/java_heapdump.hprof 
 -XX:+DisableExplicitGC 
 -XX:+CMSParallelRemarkEnabled 
 -XX:+UseCMSInitiatingOccupancyOnly 
 -XX:CMSInitiatingOccupancyFraction=75 
 -Xloggc:/data/hls-hzero/hls-seata/target/logs/seata_gc.log 
 -verbose:gc 
 -Dio.netty.leakDetectionLevel=advanced 
 -Dapp.name=seata-server 
 -Dapp.pid=20722 
 -Dapp.repo=/data/hls-hzero/hls-seata/target/lib 
 -Dapp.home=/data/hls-hzero/hls-seata/target 
 -Dbasedir=/data/hls-hzero/hls-seata/target
```

