# 014-Spring类型转换

[TOC]

## 什么是类型转换，它为什么重要

在Bean的生命周期中，一个很显著的特点就是从String类型的配置源转化成BeanDefinition类，那么这个转化的过程中，尝尝伴随着字符串类型的属性转化成目标类型，例如

```java
xml中的String类型 --数据绑定-类型转换--> Bean实例中的Date类型
```

类型转换的关注点是 **如何将外部的配置转化为内部的属性的状态**  

## Java基础的数据转换

JavaBeans规范提出了 PropertyEditor 属性编辑器，属性编辑器可以让我们进行字符串或者其他类型的转换

 [020-基于JavaBeans接口的类型转换.md](020-基于JavaBeans接口的类型转换.md) 

 [010-自定义PropertyEditor编辑属性.md](..\..\04-java\17-Java类型转换\010-自定义PropertyEditor编辑属性.md) 

## Java基础数据转换的劣势

- PropertyEditor API 是JavaBeans API的一部分，其很大程度上是为了GUI而设计的，有一定的代码冗余，特别是顶层接口PropertyEditor, 有很多API都是和GUI进行了强绑定
- 自定义使用较繁琐，通常情况下我们使用Java平台内预定义的属性就可以完成大部分需求

## Spring类型转换概览

#### Spring在考虑类型转换时，都有哪些场景以及核心类？

 [010-Spring类型转换的使用场景和实现.md](010-Spring类型转换的使用场景和实现.md) 