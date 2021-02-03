# 011-feign客户端注册器-组装组件阶段-registerDefaultConfiguration

[TOC]

## 一言蔽之

registerDefaultConfiguration 方法主要用来注册默认的配置, 将@EnableFeignClients 注解上的属性生成一个BeanDefination

## 入口

```java
@Override
public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
  //从EnableFeignClients的属性值来构建Feign的自定义Configuration进行注册
  registerDefaultConfiguration(metadata, registry);
  //扫描package，注册被@FeignClient修饰的接口类的Bean信息
  registerFeignClients(metadata, registry);
}
```

## 注册EnableFeignClients默认配置

```java
//FeignClientsRegistrar.java
private void registerDefaultConfiguration(AnnotationMetadata metadata,BeanDefinitionRegistry registry) {
//获取到metadata中关于EnableFeignClients的属性值键值对
Map〈String, Object>defaultAttrs=metadata.getAnnotationAttributes(EnableFeignClients.class.getName(), true);
    // 如果EnableFeignClients配置了defaultConfiguration类，那么才进行下一步操作，如果没有，会使用默认的FeignConfiguration
    if (defaultAttrs != null && defaultAttrs.containsKey("defaultConfiguration")) {
        String name;
        if (metadata.hasEnclosingClass()) {
            name = "default." + metadata.getEnclosingClassName();
        }
        else {
            name = "default." + metadata.getClassName();
        }
        registerClientConfiguration(registry, name,defaultAttrs.get("defaultConfiguration"));
    }
}
```

registerDefaultConfiguration方法会判断@EnableFeignClients注解是否设置了defaultConfiguration属性。

- 如果有，则将调用registerClientConfiguration方法，进行BeanDefinitionRegistry的注册。

registerClientConfiguration方法的代码如下所示

```java
// FeignClientsRegistrar.java
private void registerClientConfiguration(BeanDefinitionRegistry registry, Object name,
    Object configuration) {
    // 使用BeanDefinitionBuilder来生成BeanDefinition，并注册到registry上
    BeanDefinitionBuilder builder = BeanDefinitionBuilder
        .genericBeanDefinition(FeignClientSpecification.class);
    builder.addConstructorArgValue(name);
    builder.addConstructorArgValue(configuration);
    registry.registerBeanDefinition(
        name + "." + FeignClientSpecification.class.getSimpleName(),
        builder.getBeanDefinition());
}
```

BeanDefinitionRegistry是Spring框架中用于动态注册BeanDefinition信息的接口，调用其registerBeanDefinition方法可以将BeanDefinition注册到Spring容器中，其中name属性就是注册BeanDefinition的名称。

## FeignClientSpecification

可以看到构造的Bean类型是 FeignClientSpecification  , 是一个标准

 [012-feign客户端标准-FeignClientSpecification.md](012-feign客户端标准-FeignClientSpecification.md) 