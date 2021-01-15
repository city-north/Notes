# 140-Spring应用上下文关闭阶段

[TOC]

## Spring应用上下文关闭阶段做了什么?

- AbstractApplicationContext#close()方法
  - 状态标识
    - active(false)
    - closed(true)
  - Live Beans JMX 撤销托管
    - LiveBeansView.unregisterApplicationContext(ConfigurableApplicationContext)
  - 发布Spring应用上下文已关闭事件-ContextClosedEvent
  - 关闭LifecycleProcessor
    - 依赖查找- Lifecycle Beans
    - 停止Lifecycle Beans
  - 销毁 Spring Beans
  - 关闭 BeanFactory
  - 回调 onClose()
  - 注册 Shutdown Hook 线程(如果曾注册)