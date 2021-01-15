# 120-Spring应用上下文启动阶段

[TOC]

## Spring应用上下文启动阶段做了什么?

- AbstractApplicationContext#start()方法
  - 启动LifecycleProcessor
    - 依赖查找 Lifecycle Beans
    - 启动 Lifecyle Beans
  - 发布Spring应用上下文已启动事件-ContextStaredEvent

