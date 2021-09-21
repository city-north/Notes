# 050-ByteBuf的使用案例

[TOC]

## 使用步骤

- 分配一个ByteBuf实例
- 向ByteBuf写数据
- 从ByteBuf中毒数据

```java
@Test
public void testWriteRead() {
    ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
    print("动作：分配 ByteBuf(9, 100)", buffer);
    buffer.writeBytes(new byte[]{1, 2, 3, 4});
    print("动作：写入4个字节 (1,2,3,4)", buffer);
    Logger.info("start==========:get==========");
    getByteBuf(buffer);
    print("动作：取数据 ByteBuf", buffer);
    Logger.info("start==========:read==========");
    readByteBuf(buffer);
    print("动作：读完 ByteBuf", buffer);
}

//读取一个字节
private void readByteBuf(ByteBuf buffer) {
  while (buffer.isReadable()) {
    Logger.info("读取一个字节:" + buffer.readByte());
  }
}


//读取一个字节，不改变指针
private void getByteBuf(ByteBuf buffer) {
  for (int i = 0; i < buffer.readableBytes(); i++) {
    Logger.info("读取一个字节:" + buffer.getByte(i));
  }
}
```

#### 输出

```
[main|PrintAttribute.print] |>  after ===========动作：分配 ByteBuf(9, 100)============ 
[main|PrintAttribute.print] |>  1.0 isReadable(): false 
[main|PrintAttribute.print] |>  1.1 readerIndex(): 0 
[main|PrintAttribute.print] |>  1.2 readableBytes(): 0 
[main|PrintAttribute.print] |>  2.0 isWritable(): true 
[main|PrintAttribute.print] |>  2.1 writerIndex(): 0 
[main|PrintAttribute.print] |>  2.2 writableBytes(): 256 
[main|PrintAttribute.print] |>  3.0 capacity(): 256 
[main|PrintAttribute.print] |>  3.1 maxCapacity(): 2147483647 
[main|PrintAttribute.print] |>  3.2 maxWritableBytes(): 2147483647 
[main|PrintAttribute.print] |>  after ===========动作：写入4个字节 (1,2,3,4)============ 
[main|PrintAttribute.print] |>  1.0 isReadable(): true 
[main|PrintAttribute.print] |>  1.1 readerIndex(): 0 
[main|PrintAttribute.print] |>  1.2 readableBytes(): 4 
[main|PrintAttribute.print] |>  2.0 isWritable(): true 
[main|PrintAttribute.print] |>  2.1 writerIndex(): 4 
[main|PrintAttribute.print] |>  2.2 writableBytes(): 252 
[main|PrintAttribute.print] |>  3.0 capacity(): 256 
[main|PrintAttribute.print] |>  3.1 maxCapacity(): 2147483647 
[main|PrintAttribute.print] |>  3.2 maxWritableBytes(): 2147483643 
[main|WriteReadTest.testWriteRead] |>  start==========:get========== 
[main|WriteReadTest.getByteBuf] |>  读取一个字节:1 
[main|WriteReadTest.getByteBuf] |>  读取一个字节:2 
[main|WriteReadTest.getByteBuf] |>  读取一个字节:3 
[main|WriteReadTest.getByteBuf] |>  读取一个字节:4 
[main|PrintAttribute.print] |>  after ===========动作：取数据 ByteBuf============ 
[main|PrintAttribute.print] |>  1.0 isReadable(): true 
[main|PrintAttribute.print] |>  1.1 readerIndex(): 0 
[main|PrintAttribute.print] |>  1.2 readableBytes(): 4 
[main|PrintAttribute.print] |>  2.0 isWritable(): true 
[main|PrintAttribute.print] |>  2.1 writerIndex(): 4 
[main|PrintAttribute.print] |>  2.2 writableBytes(): 252 
[main|PrintAttribute.print] |>  3.0 capacity(): 256 
[main|PrintAttribute.print] |>  3.1 maxCapacity(): 2147483647 
[main|PrintAttribute.print] |>  3.2 maxWritableBytes(): 2147483643 
[main|WriteReadTest.testWriteRead] |>  start==========:read========== 
[main|WriteReadTest.readByteBuf] |>  读取一个字节:1 
[main|WriteReadTest.readByteBuf] |>  读取一个字节:2 
[main|WriteReadTest.readByteBuf] |>  读取一个字节:3 
[main|WriteReadTest.readByteBuf] |>  读取一个字节:4 
[main|PrintAttribute.print] |>  after ===========动作：读完 ByteBuf============ 
[main|PrintAttribute.print] |>  1.0 isReadable(): false 
[main|PrintAttribute.print] |>  1.1 readerIndex(): 4 
[main|PrintAttribute.print] |>  1.2 readableBytes(): 0 
[main|PrintAttribute.print] |>  2.0 isWritable(): true 
[main|PrintAttribute.print] |>  2.1 writerIndex(): 4 
[main|PrintAttribute.print] |>  2.2 writableBytes(): 252 
[main|PrintAttribute.print] |>  3.0 capacity(): 256 
[main|PrintAttribute.print] |>  3.1 maxCapacity(): 2147483647 
[main|PrintAttribute.print] |>  3.2 maxWritableBytes(): 2147483643 

Process finished with exit code 0

```

