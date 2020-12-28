# 006-DI第五步-检测parentBeanFactory

[TOC]

从代码上看，如果缓存没有数据的话直接转到父类工厂上去加载了，这是为什么呢？

## 简介

DI第五步实际上是检查是否有双亲BeanFactory

- 如果有双亲BeanFactory
- 且当前BeanFactory不包含指定要查找的Bean

则递归查找其双亲BeanFactory知道查找到相关的Bean

## 源码

```java
//org.springframework.beans.factory.support.AbstractBeanFactory#doGetBean		
BeanFactory parentBeanFactory = getParentBeanFactory();
//当前容器的父级容器存在，且当前容器中不存在指定名称的Bean
if (parentBeanFactory != null && !containsBeanDefinition(beanName)) {
  // Not found -> check parent.
  //解析指定Bean名称的原始名称
  String nameToLookup = originalBeanName(name);
  if (parentBeanFactory instanceof AbstractBeanFactory) {
    return ((AbstractBeanFactory) parentBeanFactory).doGetBean(
      nameToLookup, requiredType, args, typeCheckOnly);
  }
  else if (args != null) {
    // Delegation to parent with explicit args.
    //委派父级容器根据指定名称和显式的参数查找
    return (T) parentBeanFactory.getBean(nameToLookup, args);
  }
  else {
    // No args -> delegate to standard getBean method.
    //委派父级容器根据指定名称和类型查找
    return parentBeanFactory.getBean(nameToLookup, requiredType);
  }
}
```

值得注意的是判断条件

```java
if (parentBeanFactory != null && !containsBeanDefinition(beanName)){
	...
}
```

## 图示

![image-20200929211813368](../../assets/image-20200929211813368.png)

## 调用时序图







![image-20200922192538797](../../assets/image-20200922192538797.png)