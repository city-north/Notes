# 090-Spring事件监听器注册阶段

[TOC]

## Spring事件监听器注册阶段做了什么?

- AbstractApplicationContext#registerListeners()方法
  - 添加当前应用上下文所关联的ApplicationListener对象(集合)
  - 添加BeanFactory锁注册ApllicationListener Beans
  - 广播早期Spring事件

