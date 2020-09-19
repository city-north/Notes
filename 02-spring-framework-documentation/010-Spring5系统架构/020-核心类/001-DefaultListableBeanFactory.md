# DefaultListableBeanFactory

## 目录

- [图示](#图示)
- [简介](#简介)

----

## 图示

![image-20200917212321381](../../../assets/image-20200917212321381.png)

## 简介

DefaultListableBeanFactory 是整个Bean加载核心部分 , 是Spring 注册一级加载bean的默认实现,而对于 XmlBeanFactory 与DefaultListableBeanFactory 的区别在于

- XmlBeanFactory 中使用了自定义XML读取器 XmlBeanDefinitionReader 来读取 BeanDefinitionReader
- DefaultListableBeanFactory 继承了 AbstractAutowireCapableBeanFactory 
- DefaultListableBeanFactory 实现了 ConfigurableListableBeanFactory
- DefaultListableBeanFactory 实现了 BeanDefinitionRegistry

## 层级结构详解

![image-20200919224648982](../../../assets/image-20200919224648982.png)

- AliasRegistry : 定义对alias的增删改查
- BeanDefinitionRegistry : 定于对 BeanDefinition 的支持
- SimpleAliasRegistry : 主要使用 map 作为 alias 的缓存,并对接口 AliasRegistry 进行实现
- DefaultSingletonBeanRegistry : 对接口 SimpleAliasRegistry 各函数的实现
- FactoryBeanRegistrySupport : 在 DefaultSingletonBeanRegistry 基础上增加对FactoryBean的特殊处理功能
- AbstractBeanFactory：综合FactoryBeanRegistrySupport和ConfigurableBeanFactory的功能。
- AbstractAutowireCapableBeanFactory：综合AbstractBeanFactory并对接口Autowire Capable BeanFactory进行实现。
- SingletonBeanRegistry : 定义对单例的注册与获取
- BeanFactory : 定义获取bean以及bean的各种属性

HierarchicalBeanFactory : 继承 BeanFactory ,也就是在BeanFactory的基础上增加了对 parentFactory的支持

- ConfigurableBeanFactory : 提供配置Factory的各种方法

- ListableBeanFactory：根据各种条件获取bean的配置清单。

- AutowireCapableBeanFactory：提供创建bean、自动注入、初始化以及应用bean的后处理器。

- ConfigurableListableBeanFactory：BeanFactory配置清单，指定忽略类型及接口等。

- DefaultListableBeanFactory：综合上面所有功能，主要是对bean注册后的处理。

  