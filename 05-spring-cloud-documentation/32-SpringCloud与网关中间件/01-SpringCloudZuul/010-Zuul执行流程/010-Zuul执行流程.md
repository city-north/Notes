# 010-Zuul执行流程

[TOC]

## 图示

![image-20210225182210899](../../../../assets/image-20210225182210899.png)

## 核心Servlet

Zuul Filter 链是一系列配合工作的 Filter来实现的,他们能够在进行 Http 请求或者相应的时候执行相关操作

所有的服务都会走到Zuul的核心Servlet中

![image-20200603123627226](../../../../assets/image-20200603123627226.png)

## Filter的类型

- pre , 路由动作发生前
- post , 路由动作发生后

- Filter 的执行顺序

  同一种类型的 Filter 可以通过 FilterOrder()方法来设定顺序

- Filter 的执行条件

  Filter 运行锁需要的标准或者条件

- Filter 执行效果

  符合某个 Filter 执行条件,产生执行效果

## 值得注意的是

Fitler 之间之间不通讯,通过一个 `RequestContext`来共享状态, 它的内部是用 ThreadLocal 实现的