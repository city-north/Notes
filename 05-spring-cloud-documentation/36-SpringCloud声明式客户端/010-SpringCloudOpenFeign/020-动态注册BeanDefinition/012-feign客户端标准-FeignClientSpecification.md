# 012-feign客户端标准-FeignClientSpecification

[TOC]

## 一言蔽之

SpringCloud 提供了一套兜底配置实现机制,  FeignClientSpecification 实现  NamedContextFactory.Specification 来将自己设置为默认兜底实现的BeanDefinition

## 简介

注册兜底实现(协议)的过程

```java
// FeignClientsRegistrar.java
private void registerClientConfiguration(BeanDefinitionRegistry registry, Object name,
    Object configuration) {
    // 使用BeanDefinitionBuilder来生成BeanDefinition，并注册到registry上
    BeanDefinitionBuilder builder = BeanDefinitionBuilder
        .genericBeanDefinition(FeignClientSpecification.class);
    builder.addConstructorArgValue(name); 				 //设置名称
    builder.addConstructorArgValue(configuration); //设置默认属性
    registry.registerBeanDefinition(
        name + "." + FeignClientSpecification.class.getSimpleName(),
        builder.getBeanDefinition());
}
```

## FeignClientSpecification

FeignClientSpecification类实现了NamedContextFactory.Specification接口，它是OpenFeign组件实例化的重要一环，它持有**自定义配置类提供的组件实例**，供OpenFeign使用。

将bean配置类包装成`FeignClientSpecification`，注入到容器。该对象非常重要，包含FeignClient需要的重试策略，超时策略，日志等配置，如果某个服务没有设置，则读取默认的配置。

## 设置默认的标准

上述代码中的两句添加构造参数, 实际上就是SpringCloud 兜底实现

```java
builder.addConstructorArgValue(name); 				 //设置名称
builder.addConstructorArgValue(configuration); //设置默认属性
```

```java
class FeignClientSpecification implements NamedContextFactory.Specification {
	// feign默认的名称
	private String name;
	// feign的默认配置
	private Class<?>[] configuration;

	public FeignClientSpecification(String name, Class<?>[] configuration) {
		this.name = name;
		this.configuration = configuration;
	}
	....
}
```

两种形式配置

- 注解@EnableFeignClients 的属性 defaultConfiguration
- yaml 配置文件

```yaml
feign:
  client:
    config:
      feignName:
        connectTimeout: 5000
        readTimeout: 5000
        loggerLevel: full
        errorDecoder: com.example.SimpleErrorDecoder
        retryer: com.example.SimpleRetryer
       defaultQueryParameters:
          query: queryValue
        defaultRequestHeaders:
          header: headerValue
        requestInterceptors:
          - com.example.FooRequestInterceptor
          - com.example.BarRequestInterceptor
        decode404: false
        encoder: com.example.SimpleEncoder
        decoder: com.example.SimpleDecoder
        contract: com.example.SimpleContract
```

