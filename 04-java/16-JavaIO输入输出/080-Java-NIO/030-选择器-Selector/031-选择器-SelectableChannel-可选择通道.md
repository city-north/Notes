# 031-选择器-SelectableChannel-可选择通道

[TOC]

## SelectableChannel 可选择通道

并不是所有的通道都可以被选择器监控或者选择的

必须要继承抽象类 java.nio.channels.SelectableChannel ， 提供了实现通道可选择性所需要的公共方法。 NIO中的所有网络连接的Socket 套接字通道， 都继承了它

