# 070-初始化内建Bean：Spring事件广播器-initApplicationEventMulticaster()

[TOC]

## 初始化内建Bean：Spring事件广播器做了什么?

- AbstractApplicationContext#initApplicationEventMulticaster()方法 初始化事件广播器

## 相关知识

 [016-Spring事件](../016-Spring事件/README.md) 

## 图示

![image-20201007151953236](../../assets/image-20201007151953236.png)

## 方法实现

```java
protected void initApplicationEventMulticaster() {
  ConfigurableListableBeanFactory beanFactory = getBeanFactory();
  //如果包含的话,添加到ApplicationContext
  if (beanFactory.containsLocalBean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME)) {
    this.applicationEventMulticaster =
      beanFactory.getBean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, ApplicationEventMulticaster.class);
    if (logger.isTraceEnabled()) {
      logger.trace("Using ApplicationEventMulticaster [" + this.applicationEventMulticaster + "]");
    }
  }
  else {
    //新建一个事件广播器
    this.applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
    beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, this.applicationEventMulticaster);
    if (logger.isTraceEnabled()) {
      logger.trace("No '" + APPLICATION_EVENT_MULTICASTER_BEAN_NAME + "' bean, using " +
                   "[" + this.applicationEventMulticaster.getClass().getSimpleName() + "]");
    }
  }
}
```

