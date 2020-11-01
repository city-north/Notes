# 030-集合类依赖查-ListableBeanFactory

## 目录

- ListableBeanFactory简介
  - [根据Bean类型查找](#根据Bean类型查找)
  - [通过注解类型查找](#通过注解类型查找)

## ListableBeanFactory简介

BeanFactory的直接子接口, 

- 设计的目的是支持 **列举所有容器中的Bean的实例**的能力

**容器初始化阶段仅仅是注册了BeanDefinition到容器,查找实际上实在容器的运行阶段进行的**

为了实现这个能力,定义了一下方法

![image-20200917212321381](../../assets/image-20200917212321381.png)

```java
//是否包含,展现容器特性
ListableBeanFactory#containsBeanDefinition(String beanName)
ListableBeanFactory#getBeanDefinitionCount
ListableBeanFactory#getBeanDefinitionNames

//@since 4.2 获取相同类型bean名称列表
ListableBeanFactory#getBeanNamesForType(ResolvableType)
ListableBeanFactory#getBeanNamesForType(java.lang.Class<?>)
ListableBeanFactory#getBeanNamesForType(java.lang.Class<?>, boolean, boolean)
//@since 4.2 获取相同类型bean名称列表
ListableBeanFactory#getBeansOfType(java.lang.Class<T>)
ListableBeanFactory#getBeansOfType(java.lang.Class<T>, boolean, boolean)
//@since 4.0 获取标注类型Bean名称列表
ListableBeanFactory#getBeanNamesForAnnotation(Class<? extends Annotation> annotationType);
//@since 3.0 获取标注类型Bean实例列表
ListableBeanFactory#getBeansWithAnnotation(Class<? extends Annotation> annotationType)
//@since 3.0 获取指定名称+标椎类型的bean实例
ListableBeanFactory#findAnnotationOnBean
```

## 根据Bean类型查找

- 获取相同类型bean名称列表

```java
//@since 4.2 获取相同类型bean名称列表
String[] ListableBeanFactory#getBeanNamesForType(java.lang.Class<?>)
  
//@since 4.2 获取相同类型bean名称列表
ListableBeanFactory#getBeanNamesForType(ResolvableType)
ListableBeanFactory#getBeanNamesForType(java.lang.Class<?>)
ListableBeanFactory#getBeanNamesForType(java.lang.Class<?>, boolean, boolean)
```

- 获取同类型bean实例列

```java
//@since 4.2 获取相同类型bean名称列表

ListableBeanFactory#getBeansOfType(java.lang.Class<T>)
ListableBeanFactory#getBeansOfType(java.lang.Class<T>, boolean, boolean)
```



## 根据注解类型查找

- [获取标注类型Bean名称列表](#获取标注类型Bean名称列表)
- [获取标注类型Bean实例列表](#获取标注类型Bean实例列表)
- [获取标注指定类型+名称的实例](#获取标注指定类型+名称的实例)

#### 获取标注类型Bean名称列表

```java
@since 4.0 获取标注类型Bean名称列表
ListableBeanFactory#getBeanNamesForAnnotation(Class<? extends Annotation> annotationType);
```

#### 获取标注类型Bean实例列表

```java
//@since 3.0 获取标注类型Bean实例列表
ListableBeanFactory#getBeansWithAnnotation(Class<? extends Annotation> annotationType)
```

#### 获取标注指定类型+名称的实例

```java
//@since 3.0 获取指定名称+标椎类型的bean实例
ListableBeanFactory#findAnnotationOnBean
```

## getBeansOfType类型查找的来源

- BeanDefinition的定义
- FactoryBean#getObjectType

实际上调用的是Class.isAssignableFrom(Class<?> cls)方法,也就是说当前类或者其子类

