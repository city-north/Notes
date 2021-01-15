# 050-BeanFactory注册BeanpostProcessor阶段

[TOC]

## BeanFactory注册BeanpostProcessor阶段做了什么?

- org.springframework.context.support.AbstractApplicationContext#registerBeanPostProcessors(ConfigurableListableBeanFactory) 方法
  - 注册 PriorityOrdered 类型的BeanPostProcessor Beans
  - 注册 Ordered 类型的BeanPostProcessor Beans
  - 注册普通 BeanPostProcessor Beans
  - 注册 MergedBeanDefinitionPostProcessor Beans
  - 注册 ApplicationListenerDetector对象







### 注册 PriorityOrdered 类型的BeanPostProcessor Beans

### 注册 Ordered 类型的BeanPostProcessor Beans

###注册普通 BeanPostProcessor Beans

### 注册 MergedBeanDefinitionPostProcessor Beans

### 注册 ApplicationListenerDetector对象