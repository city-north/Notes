# 100-拓展Spring类型转换器

[TOC]

## 扩展的两种方式

- 实现转换器接口
  - org.springframework.core.convert.converter.Converter
  - org.springframework.core.convert.converter.ConverterFactory
  - org.springframework.core.convert.converter.GenericConverter
- 注册转换器实现
  - 通过org.springframework.context.support.ConversionServiceFactoryBean
    - 通过org.springframework.core.convert.ConversionService SPI方式	

## 实现转换器接口

### 使用Converter SPI的方式定义类型转换器

```java
package org.springframework.core.convert.converter;

public interface Converter<S, T> {

    T convert(S source);
}
```

参考

```java
package org.springframework.core.convert.support;

final class StringToInteger implements Converter<String, Integer> {

    public Integer convert(String source) {
        return Integer.valueOf(source);
    }
}
```

### 使用`ConverterFactory`

当需要集中整个类层次结构的转换逻辑时（例如，从字符串转换为枚举对象时），可以实现ConverterFactory

```
package org.springframework.core.convert.converter;

public interface ConverterFactory<S, R> {

    <T extends R> Converter<S, T> getConverter(Class<T> targetType);
}
```

参考实例

```java
package org.springframework.core.convert.support;

final class StringToEnumConverterFactory implements ConverterFactory<String, Enum> {

    public <T extends Enum> Converter<String, T> getConverter(Class<T> targetType) {
        return new StringToEnumConverter(targetType);
    }

    private final class StringToEnumConverter<T extends Enum> implements Converter<String, T> {

        private Class<T> enumType;

        public StringToEnumConverter(Class<T> enumType) {
            this.enumType = enumType;
        }

        public T convert(String source) {
            return (T) Enum.valueOf(this.enumType, source.trim());
        }
    }
}
```

### 使用`GenGenericConverterericConverter`

`GenericConverter`支持多种 11对应的转换类型

```java
package org.springframework.core.convert.converter;

public interface GenericConverter {

    public Set<ConvertiblePair> getConvertibleTypes();

    Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType);
}
```

### 使用`ConditionalGenericConverter`

如果你想在指定的逻辑下才进行转换，那么使用这个接口

```java
public interface ConditionalConverter {

    boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType);
}

public interface ConditionalGenericConverter extends GenericConverter, ConditionalConverter {
}
```

## 注册转换器实现

