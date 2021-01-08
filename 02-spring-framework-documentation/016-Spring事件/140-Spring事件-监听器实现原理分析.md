# 140-Spring事件-监听器实现原理分析

[TOC]

## 早期时间监听回放机制

Spring3中BenPostProcessor 和ApplicationContextPublisher在一个类使用时会导致事件发送不出来

![image-20210108125332047](../../assets/image-20210108125332047.png)

## 核心类

- Simp