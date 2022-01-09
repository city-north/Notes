# 050-Spring底层JavaBeans替换实现

[TOC]

## JavaBeans核心实现-java.beans.BeanInfo

- 属性(Property)
  - java.beans.PropertyEditor
- 方法(Method)
- 事件(Event)
- 表达式(Expression)

## Spring替代实现-BeanWrapper

- 属性(Property)
  - java.beans.PropertyEditor
- 嵌套属性路径(nested path)

## BeanWrapper

- Spring 底层JavaBeans基础设施的中心化接口
- 通常不会直接使用, 简介用于BeanFactory 和DataBinder
- 提供标准JavaBeans分析和操作, 能够单独或者批量存储JavaBean的属性(properties)
- 支持嵌套属性路径(nested path)
- 实现类 org.springframework.beans.BeanWrapperImpl
