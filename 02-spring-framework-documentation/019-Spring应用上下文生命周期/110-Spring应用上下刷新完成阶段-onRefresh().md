# 110-Spring应用上下刷新完成阶段

[TOC]

## Spring应用上下刷新完成阶段做了什么?

- org.springframework.context.support.AbstractApplicationContext#onRefresh()方法
  - 清除ResourceLoader缓存-clearResourceCaches() @since 5.0
  - 初始化LifecycleProcessor对象-initLifecycleProcessor()
  - 调用LifecycleProcessor#onRefresh方法
  - 发布Spring应用上下文已刷新事件-ContextRefreshedEvent
  - 向MBeanServer托管Live Beans

