# 060-ByteBuf的引用计数

[TOC]

## 简介

Netty中的ByteBuf内存回收工作是通过引用计数的方式管理的, 为什么?

1. 对Pooled ByteBuf的引用进行计数, 对Pooled 池化技术的支持
2. 能够尽快发现回收的ByteBuf(非Pooled)以便提升ByteBuf的分配和销毁的效率

## Pooled 池化技术

Pooled池化ByteBuf缓冲区呢, 在通讯程序的执行过程中, ByteBuf缓冲区实例会被频繁的创建, 使用, 释放

频繁创建对象, 分配内存, 释放内存 很耗费性能, 使用Buffer对象池, 将没有被引用的Buffer对象, 放入对象缓冲池中, 当需要时, 则从对象缓冲池中取出, 而不需要重新创建

## 使用retain方法管理计数器

- 使用retain 计数器加1
- 使用release 计数器减1

```java
public class ReferenceTest {

    @Test
    public void testRef() {

        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        Logger.info("after create:" + buffer.refCnt());
        buffer.retain();
        Logger.info("after retain:" + buffer.refCnt());
        buffer.release();
        Logger.info("after release:" + buffer.refCnt());
        buffer.release();
        Logger.info("after release:" + buffer.refCnt());
        //错误:refCnt: 0,不能再retain
        buffer.retain();
        Logger.info("after retain:" + buffer.refCnt());
    }
}

```

#### 报错

```
io.netty.util.IllegalReferenceCountException: refCnt: 0, increment: 1

	at io.netty.buffer.AbstractReferenceCountedByteBuf.retain0(AbstractReferenceCountedByteBuf.java:90)
	at io.netty.buffer.AbstractReferenceCountedByteBuf.retain(AbstractReferenceCountedByteBuf.java:77)
	at com.crazymakercircle.netty.bytebuf.ReferenceTest.testRef(ReferenceTest.java:25)
```

## 正确的使用方式

- retain和release应该成对出现

```java
public void handlMethodA(ByteBufbyteBuf byteBuf) {
   byteBuf.retail();
   try{
      handleMethod(byteBuf);
   }finally {
      byteBuf.release();
   }
}
```

