# 010-Spring数据绑定使用场景

[TOC]

## 什么是Spring数据绑定

数据绑定的过程就是将配置元信息(BeanDefinition)映射到相关的Bean的属性上面去

## Spring数据绑定的使用场景

- SpringBeanDefinition到Bean实例的创建
- Spring数据绑定类(DataBinder)
- SpringWeb参数绑定(WebDataBinder)

### SpringBeanDefinition到Bean实例的创建

Spring在Bean的生命周期进行实例化时使用到了数据绑定 

[070-SpringBean实例化阶段.md](../008-SpringBean生命周期/070-SpringBean实例化阶段.md) 

```java
org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#populateBean(String beanName, RootBeanDefinition mbd, @Nullable BeanWrapper bw)
```

方法中,

- 先从BeanDefinition中获取PropertiesValues
- 然后根据Properties进行 apply

```java
protected void applyPropertyValues(String beanName, BeanDefinition mbd, BeanWrapper bw, PropertyValues pvs) {
}
```

### Spring数据绑定类(DataBinder)

 [020-Spring数据绑定组件.md](020-Spring数据绑定组件.md) 

### SpringWeb参数绑定(WebDataBinder)

Web程序中

- Web MVC
- Web Flux

