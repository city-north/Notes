# 030-Netty中的Reactor反应器

[TOC]

## Netty中的Reactor反应器

在反应器模式中, 

- 一个Reactor反应器(或者 SubReactor 子反应器) 会负责一个事件处理线程, 不断地轮询, 通过 Selector选择器不断查询注册过的IO事件(选择键) ,
-  如果查询到 IO事件, 则分发给Handler业务处理器

## Netty中的反应器的实现类

Netty的反应器有多种实现

- 与Channel通道类有关系, 对于NioSocketChannel 通道, Netty的反应器类为: NioEventLoop

