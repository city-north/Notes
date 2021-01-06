# 050-Spring方法参数封装-MethodParameter

[TOC]

## 简介

核心API-org.springframework.core.MethodParameter

- 元信息(metadata)
  - 关联的方法-Method
  - 关联的构造器-Constructor
  - 构造器或方法参数索引-parameterIndex
  - 构造器或方法参数类型-parameterType
  - 构造器或方法参数泛型类型-genericParameterType
  - 构造器或方法参数参数名称-parameterName
  - 所在的类-containingClass

一个封装了方法参数的辅助类,是对方法参数的封装

## 属性值

```java
private static final Annotation[] EMPTY_ANNOTATION_ARRAY = new Annotation[0];

private final Executable executable;

/**
* 参数索引
*/
private final int parameterIndex; 

@Nullable
private volatile Parameter parameter;

/**
* 嵌入级别
*/
private int nestingLevel = 1;

/** Map from Integer level to Integer type index. */
@Nullable
Map<Integer, Integer> typeIndexesPerLevel;



@Nullable
private volatile Class<?> containingClass;

@Nullable
private volatile Class<?> parameterType;

@Nullable
private volatile Type genericParameterType;

@Nullable
private volatile Annotation[] parameterAnnotations;

@Nullable
private volatile ParameterNameDiscoverer parameterNameDiscoverer;

@Nullable
private volatile String parameterName;

@Nullable
private volatile MethodParameter nestedMethodParameter;

```

