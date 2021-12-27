# Spring的编程模型

[TOC]

- [面向对象编程](#面向对象编程)
- [函数驱动](#函数驱动)
- [面向切面编程](#面向切面编程)
- [模块驱动](#模块驱动)
- [面向元编程](#面向元编程)

## 面向对象编程

- 契约接口 : Aware , BeanPostProcessor
- 设计模式 :  观察者模式 ,组合模式, 模板模式
- 对象继承 : Abstract* 类

## 函数驱动

- 函数接口 : ApplicationEventPublisher
- Reactive : Spring WebFlux

## 面向切面编程

- 动态代理 :  JdkDynamicAopProxy
- 代码提升 : ASM, CGLib, AspectJ

## 模块驱动

- Maven Artifacts
- OSGI Bundles 
- Java 9 AutoModules
- Spring @Enable*

## 面向元编程

- 注解 : 模式注解(@Component, @Service, @Responitory)
- 配置 : Environment 抽象, PropertySource , BeanDefinition
- 泛型 : GenericTypeReslover , ResolvableType