# 020-随机访问文件RandomAccessFile

[TOC]

## 为什么要随机访问文件

RandomAccessFile类可以在文件中任何位置查找或者写入

- 磁盘都是随机访问的
- 网络不是随机访问的（因为网络基于网络套接字通讯的输入输出）

## 随机访问一个文件

```java
RandomAccessFile in = new RandomAccessFile("employee.dat", "r");	//只读模式
RandomAccessFile inOut = new RandomAccessFile("employee.dat","rw");//读写模式
```

怎么范围的？

RandomAccessFile 有一个表示下一个将被读入或者写出子接所在位置的文件指针，seek方法可以用来将这个文件指针设置到文件中的任意子接位置， seek的参数时一个long类型的整数，它的值位于0到文件长度（字节数）来度量的长度之间

