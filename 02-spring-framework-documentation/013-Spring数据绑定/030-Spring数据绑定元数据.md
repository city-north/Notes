# 030-Spring数据绑定元数据

[TOC]

## DataBinder的元数据-PropertyValues

 [030-SpringBean属性元信息-PropertyValue.md](../009-Spring配置元信息/030-SpringBean属性元信息-PropertyValue.md) 

## PropertyValues的来源

主要是XML.将字符串类型的属性转化成具体的类型

| 特征         | 说明                                                         |
| ------------ | ------------------------------------------------------------ |
| 数据来源     | BeanDefinition, 主要来源XML资源配置BeanDefinition            |
| 数据结构     | 由一个或者多个PropertyValue组成                              |
| 成员结构     | PropertyValue包含属性名称,以及属性值(包括原始值和类型转换后的值) |
| 常见实现     | MutablePropertyValues                                        |
| Web拓展实现  | ServletConfigPropertyValues<br />SerletRequestParameterPropertyValues<br /> |
| 相关声明周期 | InstantiationAwareBeanPostProcessor#postProcessProperties<br />[080-SpringBean实例化后阶段.md](../008-SpringBean生命周期/080-SpringBean实例化后阶段.md) |

## 源码

#### PropertyValues的来源

通常情况下 MutablePropertyValue是从BeanDefiniton中获取的,一般情况是从xml中读取后产生的,因为 如果是注解@Bean声明的Bean就不需要进行类型转换和绑定的操作了

```java
public interface BeanDefinition extends AttributeAccessor, BeanMetadataElement {	
		MutablePropertyValues getPropertyValues();
}
```

#### Propertyvalue源码

Propertyvalue 是一个数据结构,包含了key-value形式,包含了属性名以及对应的值

```java
public class PropertyValue extends BeanMetadataAttributeAccessor implements Serializable {

	private final String name;

  //原始值
	@Nullable
	private final Object value;

  //是否是可选的
	private boolean optional = false;

  //是否已经转换
	private boolean converted = false;

  //转换后的值
	@Nullable
	private Object convertedValue;

	/**是否有必要进行转换 */
	@Nullable
	volatile Boolean conversionNecessary;

	/**  */
	@Nullable
	transient volatile Object resolvedTokens;
}
```

