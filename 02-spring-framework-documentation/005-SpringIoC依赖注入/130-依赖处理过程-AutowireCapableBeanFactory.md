# 依赖处理过程

#### 先入为主的核心类

- 入口 ： DefaultListableBeanFactory#resolveDependency
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
//根据类型创建Bean
AutowireCapableBeanFactory#createBean(java.lang.Class<T>)
//依赖注入bean
AutowireCapableBeanFactory#autowireBean
//配置bean
AutowireCapableBeanFactory#configureBean
//创建Bean
AutowireCapableBeanFactory#createBean(java.lang.Class<?>, int, boolean)
AutowireCapableBeanFactory#autowire
AutowireCapableBeanFactory#autowireBeanProperties
AutowireCapableBeanFactory#applyBeanPropertyValues
AutowireCapableBeanFactory#initializeBean
AutowireCapableBeanFactory#applyBeanPostProcessorsBeforeInitialization
AutowireCapableBeanFactory#applyBeanPostProcessorsAfterInitialization
AutowireCapableBeanFactory#destroyBean
AutowireCapableBeanFactory#resolveNamedBean
AutowireCapableBeanFactory#resolveDependency(org.springframework.beans.factory.config.DependencyDescriptor, java.lang.String)
AutowireCapableBeanFactory#resolveDependency(org.springframework.beans.factory.config.DependencyDescriptor, java.lang.String, java.util.Set<java.lang.String>, org.springframework.beans.TypeConverter)
AutowireCapableBeanFactory#AUTOWIRE_NO
AutowireCapableBeanFactory#AUTOWIRE_BY_NAME
AutowireCapableBeanFactory#AUTOWIRE_BY_TYPE
AutowireCapableBeanFactory#AUTOWIRE_CONSTRUCTOR
AutowireCapableBeanFactory#AUTOWIRE_AUTODETECT
```



