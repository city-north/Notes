# 010-什么是ByteBuf缓冲区

[TOC]

## 什么是ByteBuf

Netty 提供了ByteBuf 来替代Java NIO 的ByteBUffer 缓冲区， 用来操纵内存缓冲区

## ByteBuf的优势

与 Java NIO 的 ByteBuffer 相比， 优势如下：

1. Pooling 池化技术， 减少内存复制和GC, 提高了效率
2. 复合缓冲区类型， 支持零拷贝
3. 不需要调用 filp() 方法去切换读写模式
4. 拓展性好， 例如 StringBuffer
5. 可以自定义缓冲区类型
6. 读取和写入索引分开
7. 方法和链式调用
8. 可以进行引用计数， 方便重复使用

