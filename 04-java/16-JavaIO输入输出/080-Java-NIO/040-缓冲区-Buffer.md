# 040-缓冲区-Buffer

[TOc]

## Buffer缓冲区是什么

NIO的通道本质上是一个内存块， 既可以是写入数据， 也可以从中读取数据， NIO 的Buffer 类， 是一个抽象类， 位于 java.nio包中， 其内部是一个内存块（数组）

NIO的Buffer和普通的内存块(Java数组) 不同的是， NIO buffer 对象， 提供了一组更加有效的方法， 用来进行写入和读取交替访问

Buffer类是一个线程不安全的类

## Buffer的作用

应用程序与通道（Channel）主要的交互交互操作，就是进行数据的read读取和write写入。

为了能够确保数据的读取和写入， NIO为大家准备了第三个重要的组件-NIO Buffer ， 

- 通道的读取， 就是将数据从通到读取到缓冲区中
- 通道的写入， 就是从缓冲区中写入到通道中

## Buffer类

java.nio.Buffer 类是一个抽象类， 对应Java的主要数据类型， 在NIO 中有8种缓冲区类， 分别是

| Buffer子类       | 存储类型                         |
| ---------------- | -------------------------------- |
| ByteBuffer       |                                  |
| CharBuffer       |                                  |
| DoubleBuffer     |                                  |
| FloatBuffer      |                                  |
| IntBuffer        |                                  |
| LongBuffer       |                                  |
| ShortBuffer      |                                  |
| MappedByteBuffer | 专门用于内存映射的一种ByteBuffer |

​	