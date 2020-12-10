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

 [JAVA资源管理](../../04-java/16-Java资源管理/README.md) 

