# 020-feign客户端扫描类信息-registerFeignClients

[TOC]

@EnableFeignClients的自定义配置类是被@Configuration注解修饰的配置类，它会提供一系列组装FeignClient的各类组件实例。

这些组件包括：

- Client
- Targeter
- Decoder
- Encoder
- Contract

## 入口

入口还是注册方法

```java
@Override
public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {
  //从EnableFeignClients的属性值来构建Feign的自定义Configuration进行注册
  registerDefaultConfiguration(metadata, registry);
  //扫描package，注册被@FeignClient修饰的接口类的Bean信息
  registerFeignClients(metadata, registry);
}
```

## 扫描类路径生成对应的FeignClient对应的BeanDefintion

该方法主要是扫描类路径，对所有的FeignClient生成对应的`BeanDefinition`:

```java
//FeignClientsRegistrar.java
public void registerFeignClients(AnnotationMetadata metadata,BeanDefinitionRegistry registry) {
    //生成自定义的ClassPathScanningProvider
    ClassPathScanningCandidateComponentProvider scanner = getScanner();
    scanner.setResourceLoader(this.resourceLoader);
    Set〈String〉 basePackages;
    //获取EnableFeignClients所有属性的键值对
    Map〈String, Object〉 attrs = metadata
            .getAnnotationAttributes(EnableFeignClients.class.getName());
    //依照Annotation来进行TypeFilter，只会扫描出被FeignClient修饰的类
    AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(
            FeignClient.class);
    final Class〈?〉[] clients = attrs == null ? null
            : (Class〈?〉[]) attrs.get("clients");
    //如果没有设置clients属性，那么需要扫描basePackage，所以设置了AnnotationTypeFilter,
           并且去获取basePackage
    if (clients == null || clients.length == 0) {
        scanner.addIncludeFilter(annotationTypeFilter);
        basePackages = getBasePackages(metadata);
    }
    //代码有删减，遍历上述过程中获取的basePackages列表
    for (String basePackage : basePackages) {
        //获取basepackage下的所有BeanDefinition
      Set〈BeanDefinition〉 candidateComponents = scanner.findCandidateComponents(basePackage);
        for (BeanDefinition candidateComponent : candidateComponents) {
            if (candidateComponent instanceof AnnotatedBeanDefinition) {
                AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();
                //从这些BeanDefinition中获取FeignClient的属性值
                Map〈String, Object〉 attributes = annotationMetadata
                        .getAnnotationAttributes(
                                FeignClient.class.getCanonicalName());
                String name = getClientName(attributes);
                //对单独某个FeignClient的configuration进行配置
                registerClientConfiguration(registry, name,
                        attributes.get("configuration"));
                //注册FeignClient的BeanDefinition
                registerFeignClient(registry, annotationMetadata, attributes);
            }
        }
    }
}
```

如上述代码所示，

- FeignClientsRegistrar的registerFeignClients方法依据@EnableFeignClients的属性获取要扫描的包路径信息
- 获取这些包下所有被@FeignClient注解修饰的接口类的BeanDefinition
- 最后调用registerFeignClient动态注册BeanDefinition