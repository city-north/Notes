# 初始化SpringBean(Initialization)

---

[TOC]

## 简介

- @PostConstruct 标注方法
- 实现 InitializingBean 接口的 afterPropertiesSet() 方法
- 自定义初始化方法
  - XML配置 : `<bean init-method='init' .../>`
  - Java注解 :    `@Bean(initMethod='init')`
  - JavaAPI : AbstractBeanDefinition#setInitMethodName(String)

## 假设三种方式均在同一Bean中定义, 那么这些方法的执行顺序是?

1. @PostConstruct 标注方法
2. 实现 InitializingBean 接口的 afterPropertiesSet() 方法
3. 自定义初始化方法

```java
@PostConstruct : UserFactory 初始化中...
InitializingBean#afterPropertiesSet() : UserFactory 初始化中...
@PostConstruct : UserFactory 初始化中...
InitializingBean#afterPropertiesSet() : UserFactory 初始化中...
User{name='EricChen', age=123}
```



```java
@PostConstruct : UserFactory 初始化中...
InitializingBean#afterPropertiesSet() : UserFactory 初始化中...
自定义初始化方法 initUserFactory() : UserFactory 初始化中...
Spring 应用上下文已启动...
org.geekbang.thinking.in.spring.bean.factory.DefaultUserFactory@b7f23d9

Spring 应用上下文准备关闭...
@PreDestroy : UserFactory 销毁中...
DisposableBean#destroy() : UserFactory 销毁中...
自定义销毁方法 doDestroy() : UserFactory 销毁中...
Spring 应用上下文已关闭...
```

## 延迟加载

- Bean延迟初始化(Lazy Initialization)
- XML 配置 `<bean lazy-init='true'>`

```java
//org.springframework.context.support.AbstractApplicationContext#refresh

...
  
  // Instantiate all remaining (non-lazy-init) singletons.
  finishBeanFactoryInitialization(beanFactory);	
```

## 销毁SpringBean

Bean销毁(Destroy)

- @PreDestory标注方法
- 实现 DisposableBean接口的Destory()方法
- 自定义销毁方法
  - XML配置  `<bean destory ="destory" ... />`
  - Java配置 `@Bean(destory="destory")`
  - Java API : AbstractBeanDefinition#setDestoryMethodName(String) 
