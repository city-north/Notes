# Spring 类型转换



```java
package org.springframework.core.convert.converter;

public interface Converter<S, T> {

    T convert(S source);
}	
```

为了创建自定义的converter.实现这个`Converter`接口,形参 source 不能为空,所以你创建的这个 `Converter`

应该抛出**非检查型异常**(是指**编译器**不会检查这类异常),大部分情况是抛出一个`IllegalArgumentException`去报告传入参数是否符合条件,请确保你自己的实现类是**线程安全的**

` core.convert.support`包内内置了一些方便使用的额实现类,包括 String /number 和其他常见类型.下面列表`StringToInteger`类,是一个典型的`Converter`实现类

```java
package org.springframework.core.convert.support;

final class StringToInteger implements Converter<String, Integer> {

    public Integer convert(String source) {
        return Integer.valueOf(source);
    }
}
```

## ConverterFactory

当你需要集中转换一个指定类型的类的层次结构,例如,当转换 `String `到`Enum`类型.你可以实现`ConvertFactory`类

```java
package org.springframework.core.convert.converter;

public interface ConverterFactory<S, R> {

    <T extends R> Converter<S, T> getConverter(Class<T> targetType);
}
```

将`String`转化为一个`Enum`

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

## GenericConverter

当您需要复杂的转换器实现时，请考虑使用`GenericConverter`接口。`GenericConverter`具有比`Converter`更灵活但类型更弱的签名，支持在多个源和目标类型之间进行转换。此外，`GenericConverter`提供了可用的源和目标字段上下文，您可以在实现转换逻辑时使用它们。这样的上下文允许由字段注释或字段签名上声明的通用信息驱动类型转换。下面的清单显示了`GenericConverter的`接口定义:

```java
package org.springframework.core.convert.converter;

public interface GenericConverter {

    public Set<ConvertiblePair> getConvertibleTypes();

    Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType);
}
```

一个使用`GenericConverter`好的例子是转换 java array 和 collection ,`ArrayToCollectionConverter` 

会自省目标 collection 类型的类型,这允许在目标字段上设置集合之前，将源数组中的每个元素转换为集合元素类型。



### 使用`ConditionalGenericConverter`

有时候,你想在指定条件下转化器才进行转换,例如,您可能希望仅在目标字段上出现特定注释时才运行转换器，或者仅在目标类上定义了特定方法(例如静态valueOf方法)时才运行转换器。`ConditionalGenericConverter`是`GenericConverter`和`ConditionalConverter`接口的结合，允许您定义这样的自定义匹配标准:

```
public interface ConditionalConverter {

    boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType);
}

public interface ConditionalGenericConverter extends GenericConverter, ConditionalConverter {
}
```

#### `ConversionService` API

```java
package org.springframework.core.convert;

public interface ConversionService {

    boolean canConvert(Class<?> sourceType, Class<?> targetType);

    <T> T convert(Object source, Class<T> targetType);

    boolean canConvert(TypeDescriptor sourceType, TypeDescriptor targetType);

    Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType);

}
```

大多数`ConversionService`实现还实现了`ConverterRegistry`，它为注册转换器提供了SPI。在内部，`ConversionService`实现委托给它注册的转换器来执行类型转换逻辑。

在`core.convert`中提供了一个健壮的`ConversionService`实现。支持包。`GenericConversionService`是适合在大多数环境中使用的通用实现。`ConversionServiceFactory`为创建公共的`ConversionService`配置提供了一个方便的工厂。

#### 使用ConversionService

```
@Service
public class MyService {

    @Autowired
    public MyService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public void doIt() {
        this.conversionService.convert(...)
    }
}
```

对于大多数使用场景,你可以使用`convert`方法,指定一个目标类型,但是它不能处理更复杂的类型，比如参数化元素的集合。例如，如果希望以编程方式将整数列表转换为字符串列表，则需要提供源类型和目标类型的正式定义。

```
DefaultConversionService cs = new DefaultConversionService();

List<Integer> input = ....
cs.convert(input,
    TypeDescriptor.forObject(input), // List<Integer> type descriptor
    TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(String.class)));
```

注意，`DefaultConversionService`自动注册适用于大多数环境的转换器。这包括集合转换器、标量转换器和基本的对象到字符串转换器。通过在`DefaultConversionService`类上使用静态`addDefaultConverters`方法，可以向任何`ConverterRegistry`注册相同的转换器。

值类型的转换器被数组和集合重用，因此不需要创建特定的转换器来将S集合转换为T集合，假设标准集合处理是合适的。