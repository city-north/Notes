# 030-ByteBuf的浅层复制

[TOC]

## 简介

浅层复制是一种非常重要的操作, 可以很大程度地避免内存复制, 这一点对于规模消息通信来说是非常重要的

ByteBuf的浅层复制分为两种

- 有切片(slice)浅层复制和整体(diplicate)浅层复制

## slice切片浅层复制

ByteBuf的slice方法可以获取到一个ByteBuf的一个切片, 

- 一个ByteBuf可以进行多次浅层复制

- 多次切片后的ByteBuf对象可以共享一个存储区域

slice方法有两个重载版本

- public ByteBuf slice();
- public ByteBuf slice(int index, int length);

第一个是不带参数的slice方法, 在内部是调用了第二个带参数的slice()方法, 调用大致方向为: buf.slice(buf.readerIndex(), buf.readableBytes());

第二个是带参数的slice(int index, int length)方法, 可以通过灵活地设置不同的起始位置和长度来获取到ByteBuf不同区域的切片

```java
public class SliceTest {
    @Test
    public  void testSlice() {
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(9, 100);
        print("动作：分配 ByteBuf(9, 100)", buffer);
        buffer.writeBytes(new byte[]{1, 2, 3, 4});
        print("动作：写入4个字节 (1,2,3,4)", buffer);
        ByteBuf slice = buffer.slice();
        print("动作：切片 slice", slice);
    }

}
```

```java
[main|PrintAttribute.print] |>  after ===========动作：分配 ByteBuf(9, 100)============ 
[main|PrintAttribute.print] |>  1.0 isReadable(): false 
[main|PrintAttribute.print] |>  1.1 readerIndex(): 0 
[main|PrintAttribute.print] |>  1.2 readableBytes(): 0 
[main|PrintAttribute.print] |>  2.0 isWritable(): true 
[main|PrintAttribute.print] |>  2.1 writerIndex(): 0 
[main|PrintAttribute.print] |>  2.2 writableBytes(): 9 
[main|PrintAttribute.print] |>  3.0 capacity(): 9 
[main|PrintAttribute.print] |>  3.1 maxCapacity(): 100 
[main|PrintAttribute.print] |>  3.2 maxWritableBytes(): 100 
[main|PrintAttribute.print] |>  after ===========动作：写入4个字节 (1,2,3,4)============ 
[main|PrintAttribute.print] |>  1.0 isReadable(): true 
[main|PrintAttribute.print] |>  1.1 readerIndex(): 0 
[main|PrintAttribute.print] |>  1.2 readableBytes(): 4 
[main|PrintAttribute.print] |>  2.0 isWritable(): true 
[main|PrintAttribute.print] |>  2.1 writerIndex(): 4 
[main|PrintAttribute.print] |>  2.2 writableBytes(): 5 
[main|PrintAttribute.print] |>  3.0 capacity(): 9 
[main|PrintAttribute.print] |>  3.1 maxCapacity(): 100 
[main|PrintAttribute.print] |>  3.2 maxWritableBytes(): 96 
[main|PrintAttribute.print] |>  after ===========动作：切片 slice============ 
[main|PrintAttribute.print] |>  1.0 isReadable(): true 
[main|PrintAttribute.print] |>  1.1 readerIndex(): 0 
[main|PrintAttribute.print] |>  1.2 readableBytes(): 4 
[main|PrintAttribute.print] |>  2.0 isWritable(): false 
[main|PrintAttribute.print] |>  2.1 writerIndex(): 4 
[main|PrintAttribute.print] |>  2.2 writableBytes(): 0 
[main|PrintAttribute.print] |>  3.0 capacity(): 4 
[main|PrintAttribute.print] |>  3.1 maxCapacity(): 4 
[main|PrintAttribute.print] |>  3.2 maxWritableBytes(): 0 
```

## duplicate整体浅层复制

和slice切片不同, duplicate()返回的是源ByteBuf的整个对象的一个浅层复制

- duplicate 的读写指针, 最大容量值, 与源ByteBuf的读写指针相同
- duplicate() 不会改变源ByteBuf的引用计数
- duplicate() 不会复制源ByteBuf的底层数据

不同的是

- slice方法切取一段的浅层复制
- duplicate() 是整体的浅层复制

## 浅层复制的问题

浅层复制方法不会实际去复制数据, 也不会改变ByteBuf的引用计数, 这就会导致一个问题:

- 在源ByteBuf调用release()之后, 一旦引用计数为0, 就变得不能访问了

在这种场景下, 源ByteBuf的所有浅层复制都不能读写了, 如果强行读写, 不就会报错

因此, 在调用浅层复制的时候, 

- 可以调用一次retain()方法来增加引用, 表示它们对应的底层内存多了一次引用, 引用计数为2
- 在浅层复制实例用完之后, 需要调用两次release方法引用计数减一, 这样就不会影响到源ByteBuf的内存释放了