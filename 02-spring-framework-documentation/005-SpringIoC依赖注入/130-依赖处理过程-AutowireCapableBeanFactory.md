# 依赖处理过程

---

[TOC]

## 先入为主的核心类

- 入口 ： **DefaultListableBeanFactory#resolveDependency**
- 依赖描述符 : DependencyDescriptor
  - 自定义绑定候选对象处理器 ：AutowireCandidateReslover	

## AutowireCapableBeanFactory

自动注入功能的实现是由AutowireCapableBeanFactory这个接口来实现的

<img src="../../assets/image-20200919224648982.png" alt="image-20200919224648982" style="zoom:80%;" />

从图中可以看出，它直接拓展了BeanFactory， 其核心子类是ConfigurableListableBeanfactory, 主要实现了

- 创建bean
- 自动注入
- 初始化以及应用bean的后处理器

## 接口内的方法

```java
org.springframework.beans.factory.config.AutowireCapableBeanFactory
//根据类型创建Bean,创建出来的bean是一个完全初始化的bean,包括调用它的各个生命周期方法
#createBean(java.lang.Class<T>)throws BeansException;
#createBean(java.lang.Class<?>, int, boolean)
  
//自动注入给定bean,通过初始化后置回调和bena属性回调
#autowireBean(Object existingBean) throws BeansException;

//配置给定的Bean, 自动注入Bean属性,应用bean属性值,应用工厂回调
#configureBean(Object existingBean, String beanName)

//根据指定的class初始化一个Bean, 根据指定的策略进行依赖注入,使用AUTOWIRE_NO的话只应用before-instantiation的回调(比如注解驱动的注入)
#autowire(Class<?> beanClass, int autowireMode, boolean dependencyCheck);
#autowireBeanProperties(Object existingBean, int autowireMode, boolean dependencyCheck)


#applyBeanPropertyValues
#initializeBean

#applyBeanPostProcessorsBeforeInitialization
#applyBeanPostProcessorsAfterInitialization

#destroyBean
#resolveNamedBean

//@since 2.5   DependencyDescriptor-> 依赖描述符 ;requestBeanName->当前需要注入的Bean名称
AutowireCapableBeanFactory#resolveDependency(DependencyDescriptor,String requestBeanName)
//@since 2.5 解析依赖
AutowireCapableBeanFactory#resolveDependency(DependencyDescriptor, java.lang.String, java.util.Set<java.lang.String>, TypeConverter)
//依赖注入模式-NO,只初始化并执行初始化前回调
AutowireCapableBeanFactory#AUTOWIRE_NO
//依赖注入模式-根据名称
AutowireCapableBeanFactory#AUTOWIRE_BY_NAME
//依赖注入模式-根据名称
AutowireCapableBeanFactory#AUTOWIRE_BY_TYPE
//依赖注入模式-根据构造器
AutowireCapableBeanFactory#AUTOWIRE_CONSTRUCTOR
//@Deprecated 依赖注入模式-自动检测
AutowireCapableBeanFactory#AUTOWIRE_AUTODETECT
```

 [133-依赖处理过程-解析依赖.md](133-依赖处理过程-解析依赖.md) 



