# 010-Spring为什么要引入Resource机制

[TOC]

## 为什么Spring不使用Java标准资源管理,而选择重复造轮子

- Java标准资源管理强大,但是拓展复杂,资源存储方式不统一
- Spring要自立门户,Spring是一个完整的体系,他要挑战整个JavaEE体系
- Spring3C原则
  - 抄: 抄JavaEE的接口
  - 超: 超越JavaEE的接口
  - 潮: 引领潮流

## Java标准资源管理强大,但是拓展复杂,资源存储方式不统一

Java中的资源可以分为三类

- ClassLoader
- File
- URL

 [JAVA资源管理](../../04-java/16-Java资源管理/README.md) 

## 为什么Spring将资源封装成Resource接口

在Java中，将不同来源的资源抽象成URL，通过注册不同的handler（URLStreamHandler）来处理不同来源的资源的读取逻辑，

一般handler的类型使用不同前缀（协议，Protocol）来识别如

- file:
- http:
- jar:

等等

然而URL没有默认定义相对Classpath或ServletContext等资源的handler，虽然可以注册自己的URLStreamHandler来解析特定的URL前缀（协议），比如“classpath:”，然而这需要了解URL的实现机制，而且URL也没有提供基本的方法，如检查当前资源是否存在、检查当前资源是否可读等方法。

**因而Spring对其内部使用到的资源实现了自己的抽象结构：Resource接口封装底层资源。**

![image-20200919230849447](../../assets/image-20200919230849447.png)