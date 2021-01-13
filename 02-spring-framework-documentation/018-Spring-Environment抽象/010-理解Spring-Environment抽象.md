# 010-理解Spring-Environment抽象

[TOC]

## Spring Environment抽象都包含什么

- 统一的SPring配置属性管理

Spring Framework 3.1 开始引入 Environment抽象, 它统一 Spring配置属性的存储, 包括占位符处理和类型转换, 不仅完整的个替换了 `PropertySourcesPlaceholderConfigurer` , 而且还支持更加丰富的配置属性源 `PropertySource`

- 条件化SpringBean 装配管理

通过 Environment Profiles 信息, 帮助 Spring容器提供条件化地装配Bean

## Envoronment抽象UML

![image-20210113123624597](../../assets/image-20210113123624597.png)

### Environment父接口PropertyResolver

PropertyResolver 从名称来看,是属性的解析器,主要包含的功能是对属性的解析

```java
public interface PropertyResolver {
  //是否包含属性
	boolean containsProperty(String key);
  //获取属性
	@Nullable
	String getProperty(String key);
	String getProperty(String key, String defaultValue);
  // 获取属性并尝试转换类型,如果返回null则说明转换失败
	@Nullable
	<T> T getProperty(String key, Class<T> targetType);
	<T> T getProperty(String key, Class<T> targetType, T defaultValue);
  //
	String getRequiredProperty(String key) throws IllegalStateException;
	<T> T getRequiredProperty(String key, Class<T> targetType) throws IllegalStateException;
  //处理占位符${...}
	String resolvePlaceholders(String text);
	String resolveRequiredPlaceholders(String text) throws IllegalArgumentException;
}

```

### Environment抽象

Environment抽象主要抽象了环境相关的行为,获取激活的Profile, 默认的ProfIle

```java
public interface Environment extends PropertyResolver {
	//激活的Profile
	String[] getActiveProfiles();
	//默认的Profile
	String[] getDefaultProfiles();

	//提供的profiles 是否是和 getActiveProfiles() 激活的Profile一致
	@Deprecated
	boolean acceptsProfiles(String... profiles);

	//提供的profiles 是否是和 getActiveProfiles() 激活的Profile一致
	boolean acceptsProfiles(Profiles profiles);
}
```

### ConfigurableEnvironment接口

ConfigurableEnvironment接口抽象了可配置的环境信息

- 设置默认的和激活的Profile
- 获取PropertySources属性源
- 获取Map属性
- 合并Environment

```java
public interface ConfigurableEnvironment extends Environment, ConfigurablePropertyResolver {
	void setActiveProfiles(String... profiles);
	void addActiveProfile(String profile);
	void setDefaultProfiles(String... profiles);
  //获取PropertySources属性源
	MutablePropertySources getPropertySources();
	
  Map<String, Object> getSystemProperties();
	Map<String, Object> getSystemEnvironment();

	void merge(ConfigurableEnvironment parent);
}

```

其父接口ConfigurablePropertyResolver 提供了PropertyResolver解析相关的功能

- 设置转换服务 

[110-统一类型服务转换器-ConversionService.md](../014-Spring类型转换/110-统一类型服务转换器-ConversionService.md) 

- 设置Placeholder的前缀
- 设置Placeholder的后缀

```java
public interface ConfigurablePropertyResolver extends PropertyResolver {

	ConfigurableConversionService getConversionService();
	void setConversionService(ConfigurableConversionService conversionService);
  //设置Placeholder的前缀
	void setPlaceholderPrefix(String placeholderPrefix);
  //设置Placeholder的后缀
	void setPlaceholderSuffix(String placeholderSuffix);
  //设置分隔符
	void setValueSeparator(@Nullable String valueSeparator);
  //设置遇到无法解析的嵌套属性是是否报错
	void setIgnoreUnresolvableNestedPlaceholders(boolean ignoreUnresolvableNestedPlaceholders);
	//设置必须的属性
  void setRequiredProperties(String... requiredProperties);
  //校验必须的属性
	void validateRequiredProperties() throws MissingRequiredPropertiesException;
}

```

