# 030-如何优化垃圾回收机制

[TOC]

```
io.seata.server.Server 
-Xmx4096m -Xms4096m -Xmn4096m -Xss512k 
-XX:SurvivorRatio=10 
-XX:MetaspaceSize=128m 
-XX:MaxMetaspaceSize=256m 
-XX:MaxDirectMemorySize=4096m 
-XX:-OmitStackTraceInFastThrow 
-XX:-UseAdaptiveSizePolicy 
-XX:+HeapDumpOnOutOfMemoryError 
-XX:HeapDumpPath=/data/hls-hzero/hls-seata/logs/java_heapdump.hprof 
-XX:+DisableExplicitGC 
-XX:+CMSParallelRemarkEnabled 
-XX:+UseCMSInitiatingOccupancyOnly 
-XX:CMSInitiatingOccupancyFraction=75 
-Xloggc:/data/hls-hzero/hls-seata/logs/seata_gc.log 
-verbose:gc 
-Dio.netty.leakDetectionLevel=advanced 
-Dapp.name=seata-server 
-Dapp.pid=102620 
-Dapp.repo=/data/hls-hzero/hls-seata/lib 
-Dapp.home=/data/hls-hzero/hls-seata 
-Dbasedir=/data/hls-hzero/hls-seata
```

