# 030-选择器-Selector

[TOC]

## 什么是选择器

选择器的使命就是完成IO多路复用，一个通道代表一个连接通道，通过选择器可以同时监控多个通道的IO状况

选择器和通道的关系， 就是监控和被监控的关系

选择器通过独特的API, 能够选出（select) 所监控的通道拥有哪些已经准备好的， 就绪的IO 操作

## 选择器（selector)和线程的关系

一个单线程处理一个选择器， 一个选择器可以监控多个通道

通过选择器， 一个单线程可以处理数百， 数千数万甚至更多的通道， 可以大幅度减少上下文切换的开销

## 选择器（selector)和通道（Channel)的关系

通道通过 Channel.register(Selector sel, int ops) 方法， 将通道实例注册到一个选择器中

- 参数1， 指定通道注册到的选择器实例
- 参数2 ， 指定选择器要监控的IO事件类型

## 选择器监控的通道IO事件类型

什么是IO事件呢？这里的IO事件不是对通道的IO事件，而是通道的某个IO操作的一种就绪状态， 表示具备完成某个IO操作的条件

有四种

- 可读 ： SelectKey.OP_READ
- 可写 ： SelectKey.OP_WRITE
- 连接 ： SelectKey.OP_CONNECT
- 接收 ： SelectKey.OP_ACCEPT

我们可以同时选择多个事件， 使用 按位或机就可以了

```java
channel.register(selector,
        SelectionKey.OP_READ | SelectionKey.OP_WRITE);
```

比如说

- 某个SocketChannel 通道， 完成了和对端的握手连接， 则处于“连接就绪状态” OP_CONNECT状态
- 某个ServerSocketChannel ,服务通道， 监听到一个新链接的到来，则处于 接受就绪（OP_ACCEOT）状态