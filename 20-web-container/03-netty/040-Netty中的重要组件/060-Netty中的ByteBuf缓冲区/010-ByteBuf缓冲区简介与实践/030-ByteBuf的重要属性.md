# 030-ByteBuf的重要属性

[TOC]



## 简介

ByteBuf通过三个整型的属性有效地区分可读数据和可写数据, 使得读写之间没有冲突

`AbstractByteBuf`抽象类中

- readerIndex 读指针
- writerIndex 写指针
- maxCapacity 最大容量

![image-20210703105813341](../../../../../assets/image-20210703105813341.png)

#### 读指针readerIndex

指示读取的起始位置, 每读取一个字节, readerIndex 自动增加1, 一旦readerIndex与writerIndex 相等, 则表示ByteBuf不可读了

#### 写指针writerIndex

指示写入的起始位置, 每写入一个字节 , writerIndex自动增加1,一旦 writerIndex与 capacity() 容量相等, 则表示ByteBuf已经不可写了, capacity是一个成员方法, 不是一个成员属性, 它表示ByteBuf中可以写入的容量, 注意, 它不是最大容量maxCapcity

#### 最大容量maxCapacity

表示ByteBuf可以扩容的最大容量, 当向ByteBUf写的时候, 如果容量不足, 可以进行扩容

但是如果扩容的最大限度由maxCapcity的值来设定,超过maxCapcity就会报错

## 

