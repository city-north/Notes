# 010-Reactor反应器模式中的IO事件的处理流程

[TOC]

## 什么是Reactor以及三种版本

#### 生活中的场景

饭店规模变化

- 一个人包揽所有 : 迎宾 , 点菜, 做饭, 上菜 ,送客 : Ractor 单线程模式
- 多招几个伙计 : 大家一起做上面的事情  : Reactor 多线程模式
- 进一步分工: 一组人专门迎宾 : 主从Reactor多线程模式

#### 饭店与网络世界的关联

| 饭店伙计 | 迎宾工作 | 点菜 | 做菜     | 上菜 | 送客 |
| -------- | -------- | ---- | -------- | ---- | ---- |
| 线程     | 接入连接 | 请求 | 业务处理 | 响应 | 断连 |

## Reactor核心流程

1. 注册感兴趣的事情
2. 扫描是否感兴趣的事情发生
3. 时间发生后作出相应的处理

| Client<br />/Server | SocketChannel<br />/ServerSocketChannel | OP_ACCEPT | OP_CONNECT | OP_WRITE | OP_READ |
| ------------------- | --------------------------------------- | --------- | ---------- | -------- | ------- |
| Client              | SocketChannel                           |           | Y          | Y        | Y       |
| Server              | ServerSocketChannel                     | Y         |            |          |         |
| Server              | SocketChannel                           |           |            | Y        | Y       |

- 服务端的 SocketChannel 是和客户端做连接的Channel , 它仅仅只关心读和写

## 处理流程

- 第一步 ：通道注册
- 第二步 ：查询选择
- 第三步 ：事件分发
- 第四步 ：完成真正的IO操作和业务处理7ww 

## 第一步：通道注册

IO源于通道Channel , IO是和通道（对于底层链接而言）强关联的， **一个IO事件， 一定属于某个通道**， 但是如果要查询通道的事件， 首先将通道注册到选择器， 只需要提前注册到Selector选择器即可，IO事件就会被选择器查询到

## 第二步：查询选择

在反应器模式中， 一个反应器（或者 SubReactor) 子反应器 会负责一个线程， 不停的轮询， 查询选择器中的IO事件（选择键 SelectorKey）

## 第三步：事件分发

如果查询到一个IO事件， 则分发给与IO事件有绑定关系的Handler 业务处理器的功能， 反应器模式仅仅是利用了Java NIO的优势而已

## 第四步：完成真正的IO操作和业务处理

这一步由Handler处理

上面的第一步和第二步实际上是Java NIO的功能， 反应器模式仅仅是利用了 Java NIO 的优势而已

![](http://processon.com/chart_image/608ebb027d9c084071a7659e.png)

