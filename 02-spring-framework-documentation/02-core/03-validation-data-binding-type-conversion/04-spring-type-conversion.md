# Spring 类型转换

Spring3 `core.convert`包提供了一个通用的类型转换系统:

- 这个机制定义一个SPI 去实现类型转化逻辑以及一个 API 在运行时操作类型转换
- 这个机制可以作为`PropertyEditor`实现类的替换方案,将外部的 bean 属性值转化成需要的属性
- 你可以在你的应用中的任何地方调用 public API 去完成类型转化

## Converter SPI

SPI(Service Provider Interface),Java 提供了一套用来被第三方实现或者拓展的 API,经常用来启动框架拓展和替换组件,**为某个接口寻找服务实现的机制**

- 

实现类型转化逻辑的 SPI 非常简单,强类型:

```java
package org.springframework.core.convert.converter;

public interface Converter<S, T> {

    T convert(S source);
}
```

其中

- `S` : 被转换对象类型
- `T`: 转换目标类型

