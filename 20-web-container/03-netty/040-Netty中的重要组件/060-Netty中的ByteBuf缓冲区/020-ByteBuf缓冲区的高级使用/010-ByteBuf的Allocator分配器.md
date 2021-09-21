# 010-ByteBuf的Allocator分配器

[TOC]

## 简介

Netty通过ByteBufAllocator分配器来创建缓冲区和分配内存空间, 有两种实现

- PoolByteBufAllocator : 池化ByteBuf分配器, 将ByteBuf实例放如池中, 提高了性能, 将内存碎片减少到最小, 使用了jemalloc高效内存分配策略
- UnpooledByteBufAllocator : 是普通的未池化ByteBuf分配器它没有吧ByteBuf放入池中, 返回一个新的ByteBuf实例, 通过 Java的垃圾回收站回收

## 性能比较

- 使用UnpooledByteBufAllocator是普通的未池化的ByteBuf缓冲区, 开启了10000个长连接, 每秒往所有连接发送一个消息, 再看看服务器的内部使用量的情况

  实验结果: 短期内 , 10G内存, 随着系统运行, 空间不断增长, 最终导致内存溢出

- 使用PoolByteBufAllocator . 每个连接维持在1M左右的内存, 内存使用量保持在10G左右

```java
public class AllocatorTest {
    @Test
    public void showAlloc() {
        ByteBuf buffer = null;
        //方法一：默认分配器，分配初始容量为9，最大容量100的缓冲
        buffer = ByteBufAllocator.DEFAULT.buffer(9, 100);
        //方法二：默认分配器，分配初始为256，最大容量Integer.MAX_VALUE 的缓冲
        buffer = ByteBufAllocator.DEFAULT.buffer();
        //方法三：非池化分配器，分配基于Java的堆内存缓冲区
        buffer = UnpooledByteBufAllocator.DEFAULT.heapBuffer();
        //方法四：池化分配器，分配基于操作系统的管理的直接内存缓冲区
        buffer = PooledByteBufAllocator.DEFAULT.directBuffer();
        //…..其他方法

    }
}
```