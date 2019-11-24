# Spring 类型转换(SpringTypeConverstion)

快速实战 [50-conversion-service.md](../00-tutorials/07-data-binding-validation-conversion-and-formatting/50-conversion-service.md) 

- Converter SPI

- 使用`GenericConverter`

- #### The `ConversionService` API

Spring3 `core.convert`包提供了一个通用的类型转换系统:

- 这个机制定义一个SPI 去实现类型转化逻辑以及一个 API 在运行时操作类型转换
- 这个机制可以作为`PropertyEditor`实现类的替换方案,将外部的 bean 属性值转化成需要的属性
- 你可以在你的应用中的任何地方调用 public API 去完成类型转化

## Converter SPI

SPI(Service Provider Interface),Java 提供了一套用来被第三方实现或者拓展的 API,经常用来启动框架拓展和替换组件,**为某个接口寻找服务实现的机制**

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

### 下面是例子

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

## 使用`GenericConverter`

当你需要一个复杂的`Converter`实现类的时时候,考虑使用`GenericConverter`接口

- 提供一种更加灵活的类型转换,而非强类型
- `convert`方法三个参数分别是原对象,源类型解释器以及目标类型解释器

```java
package org.springframework.core.convert.converter;

public interface GenericConverter {

    public Set<ConvertiblePair> getConvertibleTypes();

    Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType);
}
```

实现`GenericConverter`.`getConvertibleTypes`方法返回一个支持的 source 类型与目标类型的匹配,

实现`convert(Object, TypeDescriptor, TypeDescriptor)`定义你的转换逻辑,

- 源`TypeDescriptor`中维护了一个被转换的值
- 目标`TypeDescrptor`中维护将要设置目标属性

一个好例子是`ArrayToCollectionConverter`

```java
final class ArrayToCollectionConverter implements ConditionalGenericConverter {

	private final ConversionService conversionService;


	public ArrayToCollectionConverter(ConversionService conversionService) {
		this.conversionService = conversionService;
	}


	@Override
	public Set<ConvertiblePair> getConvertibleTypes() {
		return Collections.singleton(new ConvertiblePair(Object[].class, Collection.class));
	}

	@Override
	public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
		return ConversionUtils.canConvertElements(
				sourceType.getElementTypeDescriptor(), targetType.getElementTypeDescriptor(), this.conversionService);
	}

	@Override
	public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
		if (source == null) {
			return null;
		}

		int length = Array.getLength(source);
		TypeDescriptor elementDesc = targetType.getElementTypeDescriptor();
		Collection<Object> target = CollectionFactory.createCollection(targetType.getType(),
				(elementDesc != null ? elementDesc.getType() : null), length);

		if (elementDesc == null) {
			for (int i = 0; i < length; i++) {
				Object sourceElement = Array.get(source, i);
				target.add(sourceElement);
			}
		}
		else {
			for (int i = 0; i < length; i++) {
				Object sourceElement = Array.get(source, i);
				Object targetElement = this.conversionService.convert(sourceElement,
						sourceType.elementTypeDescriptor(sourceElement), elementDesc);
				target.add(targetElement);
			}
		}
		return target;
	}

}
```

## 使用`ConditionalGenericConverter` 

有时候,你想要使用一个`Converter`在指定的条件下才去转换,例如

- 有指定注解的时候进行转换
- 当目标类有指定的方法(例如 `static valueof`方法)时才进行转换

这个时候你需要`ConditionalGenericConverter`,作为`GenericConverter`和`ConditionalConverter`的结合体

```java
public interface ConditionalConverter {

    boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType);
}

public interface ConditionalGenericConverter extends GenericConverter, ConditionalConverter {
}
```

Spring 中的一个例子是`EntityConverter`,

- 它将一个`persistent entity identifier ` 转化为`entity reference`
- 只有当目标类有声明一个静态``findAccount(Long)`方法的时候才进行转换

## 配置一个`ConversionService`

一个`ConversionService`是一个无状态类,应用启动时进行初始化,在多个线程中共享,在 Spring 应用中,每个`ApplicationContext`你只需要配置一个`ConversionService`实例,Spring 会获取`ConversionService`然后当框架需要时进行类型转化,你也可以注入`ConversionService`到你的 bean 中,直接调用他

xml 方式注册

```xml
<bean id="conversionService"
    class="org.springframework.context.support.ConversionServiceFactoryBean"/>
```

你可以设置一些自己的转换器

```xml
<bean id="conversionService"
        class="org.springframework.context.support.ConversionServiceFactoryBean">
    <property name="converters">
        <set>
            <bean class="example.MyCustomConverter"/>
        </set>
    </property>
</bean>
```

## 代码中使用`ConversionService`

注入 你的 bean 直接使用

```java
@Service
public class MyService {

    public MyService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    public void doIt() {
        this.conversionService.convert(...)
    }
}
```

多数情况下,你直接调用`convert`方法指定目标类型`targetType`,但是,在很多复杂结构下无法使用,例如在集合类中,如果你想转换一个 `List<Interger>`成`List<String>`,你就需要提供 一个源和目标的格式定义

幸运的是,`TypeDescriptor`提供多种方式可以使用

```java
DefaultConversionService cs = new DefaultConversionService();

List<Integer> input = ...
cs.convert(input,
    TypeDescriptor.forObject(input), // List<Integer> type descriptor
    TypeDescriptor.collection(List.class, TypeDescriptor.valueOf(String.class)));
```

## 值得注意的是

- `DefaultConversionService`自动注册了转换器可以适配大多数环境

包括了集合转换器,`scalar`转换器以及基础的对象到 String(object->string)的转换器

- 你可以使用`ConverterRegistry`注册相同的转换器,通过`DefaultConversionService`类的静态方法`addDefaultConverters`
- 转换器可以在集合中复用,无需创建一个特殊的转换器,将`Collection<S>`转换为`Collection<T>`