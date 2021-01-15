# Spring 官方文档学习笔记

[Spring Framework Reference](https://docs.spring.io/spring/docs/current/spring-framework-reference/)

## 学习整体思路

在修理冰箱之前,一定是先看冰箱说明书,同样,在学习 Spring 源码前,一定要看 Spring 官方文档

- 知识点穿插使用的案例
- 分析Spring做法是否合适

## Spring 简介

- Spring 致力于让 Java 企业级应用的构建更加简单
- 支持 Groovy 和 Kotlin
- Spring 5.1 以上 版本需要 JDK8+,支持 JDK 11 LTS
- 建议最低 Java8 update 60 以上版本

Spring 支持广泛的应用场景:

- 在大型企业级软件中,应用往往存在很长时间.必须在 JDK 和应用服务器上运行,而他们的升级周期对于开发人员来说,不可控
- 又有一些应用作为单个 jar 文件内嵌到服务器中执行
- 也有一些应用作为独立的应用(例如批处理或集成工作负载),不需要一个服务器

Spring 优势:

- 开源
- 社区活跃
- 基于生产环境实例的反馈

## 什么是 Spring 框架

[原文连接](https://www.logicbig.com/tutorials/spring-framework/spring-core/quick-start.html)

![img](assets/di-explained.png)



1. **DI (Dependency Injection) :** 依赖注入,服务的实现类实例会被注入到目标对象的变量/属性上去(这里的属性/变量最好是用这个接口类型进行声明),通过构造器注入或者是 Setter 方法注入而不是这个目标对象主动创建他们,因此,这种方法激活了 POJO 对象可以被用在不同环境,不同实现类的场景

   > 依赖注入就是指对象时被动接受依赖类而不是自己主动去找,换句话说就是指对象不是从容器中查找它依赖的类,而是在容器实例化对象的时候主动将他依赖的类注入给它 

    

2. **IOC (Inversion of Control) container:** 在应用的具体操作中,框架代码调用应用代码获取信息而不是应用直接调用框架代码,因此,控制是反转的,一个 IoC 的例子是模板模式的子类,SpringIoc 提供一系列方法

    

3. **AOP (Aspect-Oriented Programming) :** 面向切面编程,这允许通过向应用程序代码中添加行为(方面)来分离横切关注点，而不是将应用程序涉及到那些关注点本身。这使得应用程序能够模块化，而不是将不同的关注点混合到一个地方。例如事务管理、日志记录等。

    

4. **Lightweight Alternative to Java EE :** Spring是使用POJO构建企业应用程序的轻量级解决方案。它可以用于servlet容器(例如Tomcat服务器)，并且不需要应用服务器。

## Spring 核心特性

- IoC容器
- Spring事件(Events)
- 资源管理(Resources)
- 国际化(i18n)
- 校验(validation)
- 数据绑定(Data Binding)
- 类型转换(Type Conversion)
- Spring 表达式(Spring Express Language)
- 面向切面编程

# IoC核心原理

[TOC]

IoC（Inversion of Control，控制反转）就是把原来代码里需要实现的对象创建、依赖，反转给容器来帮忙实现。我们需要创建一个容器，同时需要一种描述来让容器知道要创建的对象与对象的关系。这个描述最具体的表现就是我们所看到的配置文件。

DI（Dependency Injection，依赖注入）就是指对象被动接受依赖类而不自己主动去找，换句话说，就是指对象不是从容器中查找它依赖的类，而是在容器实例化对象时主动将它依赖的类注入给它。

我们先从自己设计的视角来考虑。

1. [对象与对象的关系怎么表示](#对象与对象的关系怎么表示)
2. [描述对象关系的文件存放在哪里](#描述对象关系的文件存放在哪里)
3. [不同的配置文件对对象的描述不一样，如标准的、自定义声明式的，如何统一](#不同的配置文件对对象的描述不一样，如标准的、自定义声明式的，如何统一)
4. [如何对不同的配置文件进行解析](#如何对不同的配置文件进行解析)


## 对象与对象的关系怎么表示

可以用XML、properties等语义化配置文件表示

## 描述对象关系的文件存放在哪里

可能是classpath、filesystem或者URL网络资源、servletContext等。

Spring将资源统一抽象成接口 -  [Resource机制 ](../090-Spring机制/010-Resource机制/README.md) 

## 不同的配置文件对对象的描述不一样，如标准的、自定义声明式的，如何统一

在内部需要有一个统一的关于对象的定义，所有外部的描述都必须转化成统一的描述定义。

## 如何对不同的配置文件进行解析

需要对不同的配置文件语法采用不同的解析器。

