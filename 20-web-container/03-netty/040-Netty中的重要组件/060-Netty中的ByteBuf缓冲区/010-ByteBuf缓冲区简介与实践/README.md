# 060-Netty中的ByteBuf缓冲区

[TOC]

## 为什么要有ByteBuf

Netty提供了ByteBuf来代替 Java NIO 的ByteBuffer 缓冲区, 以操纵内存缓冲区

## ByteBuf的优势

与JavaNIO的 ByteBuf相比, ByteBuf的优势如下

- Pooling 池化技术, 减少了内存复制和GC, 提高效率
- 复合缓冲区类型, 支持零拷贝
- 不需要调用flip()方法区切换读写模式
- 拓展性好, 例如 StringBuffer
- 可以自定义缓冲区类型
- 读取和写入索引分开
- 方法的链式调用
- 可以进行引用计数, 方便重复使用

