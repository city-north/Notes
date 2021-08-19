# 040-Netty中的Reactor反应器模式

[TOC]

## Netty中的Reactor反应器模式

netty支持3中模式

-  [020-单线程Reactor反应器模式.md](020-单线程Reactor反应器模式.md) 
-  [030-非主从多线程Reactor反应器模式.md](030-非主从多线程Reactor反应器模式.md) 
-  [031-主从Reactor多线程Reactor反应器模式.md](031-主从Reactor多线程Reactor反应器模式.md) (推荐)



## 如何在Netty中使用Reactor模式

![image-20210516112444831](../../../assets/image-20210516112444831.png)

## Netty如何支持Reactor模式?

- 为什么Netty的mainReactor 大多并不能用到一个线程组,只能线程组里面的一个
- Netty给Channel分配NIO event loop的规则是什么
- 通用模式的NIO实现多路复用器是怎么跨平台的

##### Netty如何支持Reactor模式

- 两种Channel 分别绑定到两种EventLoopGroup中去

##### 为什么Netty的mainReactor 大多并不能用到一个线程组,只能线程组里面的一个

对于服务来来说, 只会绑定一个端口, 所以只能绑定到一个线程组

##### Netty给Channel分配NIO event loop的规则是什么

workgroup是怎么选出一个 Channel 

##### 通用模式的NIO实现多路复用器是怎么跨平台的