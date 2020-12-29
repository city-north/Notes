# 015-PrintStream-像瘟疫一样躲避

[TOC]

## 总结

PrintStream是有害的,网络程序员应该像躲避瘟疫一样避开他

## 什么是PrintStream

PrintStream是一个过滤器流,System.out 就是一个 PrintStream

默认情况下autoFlush 参数为true,也就是说每次写1个字节数组或者换行,或者调用println都会刷新输出流

## PrintStream为什么是瘟疫

- println换行是平台有关的
- PrintStream想当然的使用所在平台的默认编码方式
- PrintStream生吞了所有异常

### 缺点1:println换行是平台有关的

PrintStream的输出是与平台有关的,取决于运行代码的机器,

如果编写必须遵循明文协议的网络客户端或者服务器而言,是个灾难

大多数网络协议(如 HTTP) 都规定使用\r\n (回车换行)作为结束

- Windows 默认是 \r\n (回车换行)
- Linux 默认是 \n (换行符)
- Mac 默认是(\r)

那么你使用println换行输出的话,很可能在windows可以工作,在linux或者mac无法使用

### 缺点2:PrintStream想当然的使用所在平台的默认编码方式

PrintStream想当然的使用所在平台的默认编码方式,这种编码可能不是服务器或者客户端想要的

PrintStream不提供任何改变默认编码的机制

### 缺点3:PrintStream生吞了所有异常

这使得PrintStream 适合作为教科书程序,因为要讲述简单的控制台输出,不用让学生先去学习异常处理相关知识

网络连接缺不如控制台可靠,经常会拥塞,崩溃,网络程序必须准备处理输出流中意料之外的中断, 

PrintStream 的5 个标准方法都没有抛出 IOException

如果发送了异常,你需要调用checkError()方法

```java
public boolean checkError() {
  if (out != null)
    flush();
  if (out instanceof java.io.PrintStream) {
    PrintStream ps = (PrintStream) out;
    return ps.checkError();
  }
  return trouble;
}
```

