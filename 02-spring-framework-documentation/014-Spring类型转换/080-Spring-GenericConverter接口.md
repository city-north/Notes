# 080-Spring-GenericConverter接口

[TOC]

## 使用场景

用于复合类型转换，比如Collection、Map、数组等等

```java
public interface GenericConverter {
    //获取类型
	@Nullable
	Set<ConvertiblePair> getConvertibleTypes();
	@Nullable
	Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType);


	/**
	 * Holder for a source-to-target class pair.
	 */
	final class ConvertiblePair {

		private final Class<?> sourceType;

		private final Class<?> targetType;
    }

}

```



### 转换范围

```
Set<ConvertiblePair> getConvertbleTypes()''
```

### 配对类型

```java
org.springframework.core.convert.converter.GenericConverter.ConvertiblePair
```

### 转换方法

```java
Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType);

```

### 类型描述

```java
org.springframework.core.convert.TypeDescriptor
```

## GenericConverter局限性

- 缺少 SourceType 和Target Type前置判断
- 单一类型转换实现复杂

## GenericConverter优化接口-ConditionGenericConverter

 [090-优化GenericConverter接口.md](090-优化GenericConverter接口.md) 

