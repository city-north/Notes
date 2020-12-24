# 050-Spring3通用类型转换接口

[TOC]

- 类型转换接口-org.springframework.core.convert.converter.Converter<S, T> 

- 泛型S: 来源类型 ,参数T 目标类型

- 核心方法

  ```
  @Nullable
  T convert(S source);
  ```

## 通用类型转换接口

org.springframework.core.convert.converter.GenericConverter

### 核心方法

```java
	@Nullable
	Object convert(@Nullable Object source, TypeDescriptor sourceType, TypeDescriptor targetType);
```

配对类型

```java
org.springframework.core.convert.converter.GenericConverter.ConvertiblePair
```

类型描述

```
org.springframework.core.convert.TypeDescriptor
```

