# 120-基于注解拓展Spring配置属性源

[TOC]

## 简介

注解@PropertySource的解析也是在类配置处理器ConfigurationClassPostProcessor中实现的,解析完添加到Environment中

## 注解@PropertySource实现

org.springframework.context.annotation.PropertySource实现原理

- 入口 - org.springframework.context.annotation.ConfigurationClassParser#doProcessConfigurationClass
- org.springframework.context.annotation.ConfigurationClassParser#processPropertySource

4.3新增语义

- 配置属性字符编码-encoding
- org.springframework.core.io.support.PropertySourceFactory

适配对象- org.springframework.core.env.**CompositePropertySource**

#### 解析核心类

```java
org/springframework/context/annotation/ConfigurationClassPostProcessor
```

ConfigurationClassPostProcessor解析配置文件类,解析标注在配置类上的@PropertySource

```
doProcessConfigurationClass:270, ConfigurationClassParser (org.springframework.context.annotation)
processConfigurationClass:245, ConfigurationClassParser (org.springframework.context.annotation)
parse:202, ConfigurationClassParser (org.springframework.context.annotation)
parse:170, ConfigurationClassParser (org.springframework.context.annotation)
processConfigBeanDefinitions:325, ConfigurationClassPostProcessor (org.springframework.context.annotation)
postProcessBeanDefinitionRegistry:242, ConfigurationClassPostProcessor (org.springframework.context.annotation)
invokeBeanDefinitionRegistryPostProcessors:275, PostProcessorRegistrationDelegate (org.springframework.context.support)
invokeBeanFactoryPostProcessors:95, PostProcessorRegistrationDelegate (org.springframework.context.support)
invokeBeanFactoryPostProcessors:706, AbstractApplicationContext (org.springframework.context.support)
refresh:532, AbstractApplicationContext (org.springframework.context.support)
main:58, AnnotatedSpringIoCContainerMetadataConfigurationDemo (org.geekbang.thinking.in.spring.configuration.metadata)
```

#### 核心解析类

```
org.springframework.context.annotation.ConfigurationClassParser#doProcessConfigurationClass
```

```java
@Nullable
protected final SourceClass doProcessConfigurationClass(ConfigurationClass configClass, SourceClass sourceClass)
  throws IOException {

  if (configClass.getMetadata().isAnnotated(Component.class.getName())) {
    // Recursively process any member (nested) classes first
    processMemberClasses(configClass, sourceClass);
  }

  // Process any @PropertySource annotations
  //解析所有标注有PropertySources.class和PropertySource.class 注解的类
  for (AnnotationAttributes propertySource : AnnotationConfigUtils.attributesForRepeatable(
    sourceClass.getMetadata(), PropertySources.class,
    org.springframework.context.annotation.PropertySource.class)) {
    if (this.environment instanceof ConfigurableEnvironment) {
      //执行解析逻辑
      processPropertySource(propertySource);
    }
    else {
      logger.info("Ignoring @PropertySource annotation on [" + sourceClass.getMetadata().getClassName() +
                  "]. Reason: Environment must implement ConfigurableEnvironment");
    }
  }
```

#### 解析详情

```java
private void processPropertySource(AnnotationAttributes propertySource) throws IOException {
  String name = propertySource.getString("name");
  //获取名称
  if (!StringUtils.hasLength(name)) {
    name = null;
  }
  //获取编码
  String encoding = propertySource.getString("encoding");
  if (!StringUtils.hasLength(encoding)) {
    encoding = null;
  }
  String[] locations = propertySource.getStringArray("value");
  //指定location
  Assert.isTrue(locations.length > 0, "At least one @PropertySource(value) location is required");
  boolean ignoreResourceNotFound = propertySource.getBoolean("ignoreResourceNotFound");

  //获取工厂,我们可以自定义工厂
  Class<? extends PropertySourceFactory> factoryClass = propertySource.getClass("factory");
  //如果过不存在,则使用默认的工厂
  PropertySourceFactory factory = (factoryClass == PropertySourceFactory.class ?
                                   DEFAULT_PROPERTY_SOURCE_FACTORY : BeanUtils.instantiateClass(factoryClass));
	//判断解析底子
  for (String location : locations) {
    try {
      String resolvedLocation = this.environment.resolveRequiredPlaceholders(location);
      //获取资源
      Resource resource = this.resourceLoader.getResource(resolvedLocation);
      //添加PropertySource到Environment
      addPropertySource(factory.createPropertySource(name, new EncodedResource(resource, encoding)));
    }
  }
}
```

#### 添加PropertySource到Environment

```java
private void addPropertySource(PropertySource<?> propertySource) {
  String name = propertySource.getName();
  //从Environment中获取PropertySource并添加进去
  MutablePropertySources propertySources = ((ConfigurableEnvironment) this.environment).getPropertySources();
  if (this.propertySourceNames.contains(name)) {
    // We've already added a version, we need to extend it
    PropertySource<?> existing = propertySources.get(name);
    if (existing != null) {
      PropertySource<?> newSource = (propertySource instanceof ResourcePropertySource ?
                                     ((ResourcePropertySource) propertySource).withResourceName() : propertySource);
      if (existing instanceof CompositePropertySource) {
        ((CompositePropertySource) existing).addFirstPropertySource(newSource);
      }
      else {
        if (existing instanceof ResourcePropertySource) {
          existing = ((ResourcePropertySource) existing).withResourceName();
        }
        CompositePropertySource composite = new CompositePropertySource(name);
        composite.addPropertySource(newSource);
        composite.addPropertySource(existing);
        propertySources.replace(name, composite);
      }
      return;
    }
  }

  if (this.propertySourceNames.isEmpty()) {
    propertySources.addLast(propertySource);
  }
  else {
    String firstProcessed = this.propertySourceNames.get(this.propertySourceNames.size() - 1);
    propertySources.addBefore(firstProcessed, propertySource);
  }
  this.propertySourceNames.add(name);
}

```

