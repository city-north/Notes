# 040-Spring拓展点-InstantiationAwareBeanPostProcessor

[TOC]

## 一言蔽之

> org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor

该接口继承了BeanPostProcess接口，区别如下：

BeanPostProcess接口只在bean的初始化阶段进行扩展（注入spring上下文前后），而InstantiationAwareBeanPostProcessor接口在此基础上增加了3个方法，把可扩展的范围增加了实例化阶段和属性注入阶段。

该类主要的扩展点有以下5个方法，主要在bean生命周期的两大阶段：实例化阶段和初始化阶段，下面一起进行说明，按调用顺序为：

- postProcessBeforeInstantiation：实例化bean之前，相当于new这个bean之前
- postProcessAfterInstantiation：实例化bean之后，相当于new这个bean之后
- postProcessPropertyValues：bean已经实例化完成，在属性注入时阶段触发，@Autowired,@Resource等注解原理基于此方法实现
- postProcessBeforeInitialization：初始化bean之前，相当于把bean注入spring上下文之前
- postProcessAfterInitialization：初始化bean之后，相当于把bean注入spring上下文之后

使用场景：这个扩展点非常有用 ，无论是写中间件和业务中，都能利用这个特性。比如对实现了某一类接口的bean在各个生命期间进行收集，或者对某个类型的bean进行统一的设值等等。