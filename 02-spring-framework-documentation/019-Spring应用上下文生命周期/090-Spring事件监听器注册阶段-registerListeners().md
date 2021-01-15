# 090-Spring事件监听器注册阶段

[TOC]

## Spring事件监听器注册阶段做了什么?

- AbstractApplicationContext#registerListeners()方法
  - 添加当前应用上下文所关联的ApplicationListener对象(集合)
  - 添加BeanFactory锁注册ApllicationListener Beans
  - 广播早期Spring事件

## 图示

![image-20201007151953236](../../assets/image-20201007151953236.png)

## 广播早期Spring事件

在广播器ApplicationEventMulticaster初始化之前,事件已经发生了,那么Spring会先将事件存储,等到初始化applicationEventMulticaster 以后再进行执行

## 源码分析

- 添加当前应用上下文所关联的ApplicationListener对象(集合)
- 添加BeanFactory锁注册ApllicationListener Beans
- 广播早期Spring事件

```java
protected void registerListeners() {
  // Register statically specified listeners first.
  for (ApplicationListener<?> listener : getApplicationListeners()) {
    //添加当前应用上下文所关联的ApplicationListener对象(集合)
    getApplicationEventMulticaster().addApplicationListener(listener);
  }

  //通过起来查找的方式把所有Bean的名称找出来,注册进去
  // Do not initialize FactoryBeans here: We need to leave all regular beans
  // uninitialized to let post-processors apply to them!
  String[] listenerBeanNames = getBeanNamesForType(ApplicationListener.class, true, false);
  for (String listenerBeanName : listenerBeanNames) {
    getApplicationEventMulticaster().addApplicationListenerBean(listenerBeanName);
  }

  //执行早期事件
  // Publish early application events now that we finally have a multicaster...
  Set<ApplicationEvent> earlyEventsToProcess = this.earlyApplicationEvents;
  this.earlyApplicationEvents = null;
  if (!CollectionUtils.isEmpty(earlyEventsToProcess)) {
    for (ApplicationEvent earlyEvent : earlyEventsToProcess) {
      getApplicationEventMulticaster().multicastEvent(earlyEvent);
    }
  }
}
```