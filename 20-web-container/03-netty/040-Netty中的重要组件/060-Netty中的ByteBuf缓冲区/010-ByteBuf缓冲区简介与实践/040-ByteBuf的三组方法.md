# 040-ByteBuf的三组方法

[TOC]

## 简介

1. 第一组方法 : 容器系列
2. 第二组方法: 写入系列
3. 第三组方法: 读取系列

## 第一组:容器系列

- capacity(): 表示 ByteBuf的容量, 它的值是以下三部分之和: 废弃的字节数和可写字节数
- maxCapacity() : 表示 ByteBuf最大能够容纳的最大字节数, 当向 ByteBuf中写数据的时候, 如果发现容量不足, 则进行扩容, 直到扩容不足

## 第二组:写入系列

- isWriteable(): 表示 ByteBuf是否可写, 如果capcity容量大于writerIndex指针的位置, 则表示可写, 否则为不可写
- writableByte(): 取得可写入的字节数, 他的值等于容量capcaity减去writerIndex
- maxWriteableBytes() : 取得最大的可写字节数
- writeBytes(byte[] src): 写入src
- writeTYPE(TYPE value): 写入基础数据类型的数据, 例如 writeBoolean();
- setTYPE():基础数据类型的设置
- markWriterIndex() 与 resetwriterIndex 

## 第三组:读取系列

- isReadable(): 返回ByteBuf 是否可读
- readableBytes(): 返回表示ByteBuf当前可读取的字节数, 它的值等于writerIndex减去readerIndex
- readBytes() : 
- readType() :
- getTYPE() :
- markReaderIndex() : 把当期啊啊按的读指针readerIndex保存在markedReaderIndex属性中
- resetReaderIndex() : 把保存在markedReaderIndex属性的值恢复到读指针中

