# 080-Spring类型转换在Environment中的运用

[TOC]

## 一言蔽之

Environment中的类型转换实际上是委托给了PropertySourcesPropertyResolver, 然后PropertySourcesPropertyResolver调用ConversionService来实现

## Environment底层实现

- 底层实现-org.springframework.core.env.PropertySourcesPropertyResolver
  - 核心方法- `convertValueIfNecessary(Object value, @Nullable Class<T> targetType) `

- 底层服务- org.springframework.core.convert.ConversionService
  - 默认实现-`org.springframework.core.convert.support.DefaultConversionService`



## 源码分析

AbstractEnvironment 聚合了PropertySourcesPropertyResolver来完成类型转换相关的功能

```java
public abstract class AbstractEnvironment implements ConfigurableEnvironment {
		...

	private final MutablePropertySources propertySources = new MutablePropertySources();

	private final ConfigurablePropertyResolver propertyResolver =
			new PropertySourcesPropertyResolver(this.propertySources);
}
```

我们可以看到在PropertySourcesPropertyResolver的解析过程中使用到了conversionService;

```java
public abstract class AbstractPropertyResolver implements ConfigurablePropertyResolver {

	protected final Log logger = LogFactory.getLog(getClass());

	@Nullable
	private volatile ConfigurableConversionService conversionService;


	@Nullable
	protected <T> T getProperty(String key, Class<T> targetValueType, boolean resolveNestedPlaceholders) {
		if (this.propertySources != null) {
      //遍历所有的数据源
			for (PropertySource<?> propertySource : this.propertySources) {
				if (logger.isTraceEnabled()) {
					logger.trace("Searching for key '" + key + "' in PropertySource '" +
							propertySource.getName() + "'");
				}
        //获取数据源里的值
				Object value = propertySource.getProperty(key);
				if (value != null) {
          
					if (resolveNestedPlaceholders && value instanceof String) {
            //解析
						value = resolveNestedPlaceholders((String) value);
					}
					logKeyFound(key, propertySource, value);
          //转换
					return convertValueIfNecessary(value, targetValueType);
				}
			}
		}
		if (logger.isTraceEnabled()) {
			logger.trace("Could not find key '" + key + "' in any property source");
		}
		return null;
	}

}

```

