# 050-ByteBuf缓冲区的类型

[TOC]

## 缓冲区类型

- Heep ByteBuf
- Direct ByteBuf
- Compose ByteBuf

无论哪一种， 都支持 池化（Pooled) 和 非池化（Unpooled)两种分配器来创建和分配内存空间

| 类型            | 说明                                                         | 优点                                                   | 不足                                                         |
| --------------- | ------------------------------------------------------------ | ------------------------------------------------------ | ------------------------------------------------------------ |
| Heep ByteBuf    | 内部数组是一个Java<br />数组存储在JVM的堆空间中， 通过hasArray来判断是否是堆缓冲区 | 不用池化的情况下， 能快速分配和释放                    | 写入底层传输通道之前， 都会复制到直接缓冲区                  |
| Direct ByteBuf  | 内部数据存储在操作系统的物理内存中                           | 能获得超过JVM限制的内存大小， 写入传输通道比堆缓冲区块 | 释放和分配空间昂贵，需要调用操作系统的方法，在Java中操作时， 需要复制一次到堆中 |
| Compose ByteBuf | 多个缓冲区的组合表示                                         | 方便一次操作多个缓冲区实例                             |                                                              |

## Direct Memory 直接内存

- Direct Memory 不属于 Java堆内存， 所分配的内存其实是调用操作系统 malloc() 函数开得到的， 由netty的本地内存堆Netty进行管理
- Direct Memory容量可以通过 -XX：MaxDirectMemorySize 指定， 如果不指定， 则默认与Java堆的最大值（-Xmx) 指定的一样， 不同的JVM 有不同的策略
- DirectMemory的使用避免了Java堆和Native 堆之间来回复制数据
- 在频繁创建缓冲区的场合下， 不适合DirectBuffer， 因为创建和销毁DirectBuffer的成本更高
- Direct Buffer 读写比 HeapBuffer块， 但是创建和销毁比普通的 Heap Buffer 慢

# Demo

```java
public class BufferTypeTest {
   final static Charset UTF_8 = Charset.forName("UTF-8");

    //堆缓冲区
    @Test
    public  void testHeapBuffer() {
        //取得堆内存
        //取得堆内存--netty4默认直接buffer，而非堆buffer
        //ByteBuf heapBuf = ByteBufAllocator.DEFAULT.buffer();
        ByteBuf heapBuf = ByteBufAllocator.DEFAULT.heapBuffer();
        heapBuf.writeBytes("疯狂创客圈:高性能学习社群".getBytes(UTF_8));
        if (heapBuf.hasArray()) {
            //取得内部数组
            byte[] array = heapBuf.array();
            int offset = heapBuf.arrayOffset() + heapBuf.readerIndex();
            int length = heapBuf.readableBytes();
            Logger.info(new String(array,offset,length, UTF_8));
        }
        heapBuf.release();
    }
}
```

```java
//直接缓冲区
@Test
public  void testDirectBuffer() {
    ByteBuf directBuf =  ByteBufAllocator.DEFAULT.directBuffer();
    directBuf.writeBytes("疯狂创客圈:高性能学习社群".getBytes(UTF_8));
    if (!directBuf.hasArray()) {
        int length = directBuf.readableBytes();
        byte[] array = new byte[length];
        //读取数据到堆内存
        directBuf.getBytes(directBuf.readerIndex(), array);
        Logger.info(new String(array, UTF_8));
    }
    directBuf.release();
}
```