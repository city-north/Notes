# 030-Spring拓展点-BeanFactoryPostProcessor

[TOC]

## 一言蔽之

> org.springframework.beans.factory.config.BeanFactoryPostProcessor

这个接口是beanFactory的扩展接口，调用时机在spring在读取beanDefinition信息之后，实例化bean之前。

在这个时机，用户可以通过实现这个扩展接口来自行处理一些东西，比如修改已经注册的beanDefinition的元信息。

```java
static class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
    System.out.println("----- MyBeanFactoryPostProcessor- postProcessBeanFactory()------");
  }
}
```

