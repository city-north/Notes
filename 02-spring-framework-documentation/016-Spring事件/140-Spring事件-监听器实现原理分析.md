# 140-Spring事件-监听器实现原理分析

[TOC]

## 早期时间监听回放机制

Spring3中BenPostProcessor 和ApplicationContextPublisher在一个类使用时会导致事件发送不出来

## 核心类

- org.springframework.context.event.SimpleApplicationEventMulticaster
  - 设计模式:观察者模式拓展
    - 被观察者-org.springframework.context.ApplicationListener
      - API添加
      - 依赖查找
    - 通知对象-org.springframework.context.ApplicationEvent
  - 执行模式：同步、异步
  - 异步处理：org.springframework.util.ErrorHandler
  - 泛型处理：org.springframework.core.ResolvableType

## 异步处理：org.springframework.util.ErrorHandler

 [120-同步和异步的事件广播.md](120-同步和异步的事件广播.md) 