# 070-SpringBean实例化阶段

## 目录

- [简介](#简介)
- [实例化方式](#实例化方式)
  - [传统实例化方式](#传统实例化方式)
  - [构造器依赖注入](#构造器依赖注入)
- [三级目录](#三级目录)
- [四级目录](#四级目录)

## 简介

入口

```
org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#doCreateBean
```

![image-20201124195605080](../../assets/image-20201124195605080.png)

值得注意的是,当我们在进行依赖查找的时候,传入的覆盖构造器的参数会传入到AbstractAutowireCapableBeanFactory 中的创建bean方法参数中doCreateBean

## 实例化方式

#### 传统实例化方式

- 实例化策略 - InstantiationStrategy

#### 构造器依赖注入

- 

## 二级目录



## 三级目录



## 四级目录