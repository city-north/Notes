# 130-Spring应用上下文停止阶段

[TOC]

## Spring应用上下文停止阶段做了什么?

- AbstractApplication#stop()方法
  - 停止LifecycleProcessor
    - 依赖查找lifecycle Beans
    - 停止 Lifecycle Beans
  - 发布 Spring应用上下已停止事件- ContextStoppedEvent