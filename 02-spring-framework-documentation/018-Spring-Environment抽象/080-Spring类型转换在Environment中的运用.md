# 080-Spring类型转换在Environment中的运用

[TOC]

## Environment底层实现

- 底层实现-org.springframework.core.env.PropertySourcesPropertyResolver
  - 核心方法- convertValueIfNecessary(Object value, @Nullable Class<T> targetType) 

- 底层服务- org.springframework.core.convert.ConversionService
  - 默认实现-org.springframework.core.convert.support.DefaultConversionService



## 源码分析



```java

	private final ConfigurablePropertyResolver propertyResolver =
			new PropertySourcesPropertyResolver(this.propertySources);



	@Override
	public <T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException {
		return this.propertyResolver.getRequiredProperty(key, targetType);
	}


```

