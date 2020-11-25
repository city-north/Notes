# 060-SpringBean实例化前阶段



## 目录

- [简介](#简介)

- [先入为主核心类](#先入为主核心类)


## 简介

![image-20201124195218996](../../assets/image-20201124195218996.png)

Spring实例化前阶段是一个非主流阶段- Bean实例化前阶段,这个阶段的在工作中很少使用,主要入口是

```java
Object InstantiationAwareBeanPostProcessor#postProcessBeforeInstantiation
```

返回的Object可以是Bean的包装,相当于是对Bean实例的拦截

## 先入为主核心类

ConfigurableListableBeanFactory 赋予了容器在结束时确保所有**[非延迟初始化]**的单例都初始化

确保所有非延迟初始化单例化都已实例化，同时也要考虑factorybean。如果需要，通常在工厂设置结束时调用。

```
org.springframework.beans.factory.config.ConfigurableListableBeanFactory#preInstantiateSingletons
```

Spring源码中唯一的调用时机是在初始化第11步  [110-第十一步-初始化所有剩余的非lazy单例Bean.md](../080-Spring拓展点/110-第十一步-初始化所有剩余的非lazy单例Bean.md) ,在refresh回调之后初始化

```
org.springframework.context.support.AbstractApplicationContext#finishBeanFactoryInitialization
```

```java
@Override
public void refresh() throws BeansException, IllegalStateException {
  synchronized (this.startupShutdownMonitor) {
    //....忽略 
    // 11、初始化所有剩余的单例Bean
    finishBeanFactoryInitialization(beanFactory);
    //....忽略
  }
}
```

解析

```java
//对配置了lazy-init属性的Bean进行预实例化处理
protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
  ///...其他操作

  // Instantiate all remaining (non-lazy-init) singletons.
  //对配置了lazy-init属性的单态模式Bean进行预实例化处理
  beanFactory.preInstantiateSingletons();
}
```

