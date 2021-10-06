# 031-主从Reactor多线程Reactor反应器模式

[TOC]

## 图示

![image-20210516112230426](../../../assets/image-20210516112230426.png)

- 使用一个专门的mainReactor 去接收连接请求
- 使用一个专门的subReactor 去处理请求的读取与响应
- 使用 一个线程池来解决 decode, compute, encode 等操作

