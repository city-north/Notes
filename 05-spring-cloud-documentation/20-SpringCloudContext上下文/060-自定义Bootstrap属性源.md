# 060-自定义Bootstrap属性源

[TOC]



默认的Bootstrap外部配置属性源是Spring Cloud Config Server，即使用配置中心加载外部属性。

但是用户也可以通过将PropertySourceLocator类型的Bean实例添加到Bootstrap上下文(在spring.factories添加对应的配置类)来添加额外的属性来源。通过这种方法可以从不同的服务器或者数据库中加载额外的属性，如下所示：

```java
@Configuration
public class CustomPropertySourceLocator implements PropertySourceLocator {
    @Override
    public PropertySource〈?〉 locate(Environment environment) {
        return new MapPropertySource("customProperty",
            Collections.〈String, Object〉singletonMap("property.from.sample.custom.source", "worked as intended"));
    }
}
```



上述代码中传入的Environment参数用于创建应用上下文，它具有Spring Boot提供的属性源，可以使用它们来加载特定的属性源(例如重新设置spring.application.name)。可以在META-INF/spring.factories文件中添加如下记录来配置属性源：

```
org.springframework.cloud.bootstrap.BootstrapConfiguration=\
    sample.custom.CustomPropertySourceLocator
```

上述配置令应用程序可以使用CustomPropertySourceLocator作为其属性源。

