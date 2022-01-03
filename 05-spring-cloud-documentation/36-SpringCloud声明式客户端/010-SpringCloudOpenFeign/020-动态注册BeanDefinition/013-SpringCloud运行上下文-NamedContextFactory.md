# 013-SpringCloud运行上下文-NamedContextFactory

[TOC]

## 简介

Spring-cloud-commons 中参考了 spring-cloud-netflix 的设计，引入了 NamedContextFactory 机制，一般用于对于**不同微服务的客户端模块使用不同的** **子 ApplicationContext** 进行配置。

spring-cloud-commons 是 Spring Cloud 对于微服务基础组件的抽象。在一个微服务中，**调用微服务 A 与调用微服务 B 的配置可能不同。**

- 比较简单的例子就是，A 微服务是一个简单的用户订单查询服务，接口返回速度很快，B 是一个报表微服务，接口返回速度比较慢。这样的话我们就不能对于调用微服务 A 和微服务 B 使用相同的**超时时间配置**。

- 还有就是，我们可能对于服务 A 通过注册中心进行发现，对于服务 B 则是通过 DNS 解析进行服务发现，所以对于不同的微服务我们可能**使用不同的组件**，在 Spring 中就是使用不同类型的 Bean。

在这种需求下，不同微服务的客户端**有不同的以及相同的配置**，**有不同的 Bean，也有相同的 Bean**。所以，我们可以针对每一个微服务将他们的 Bean 所处于 ApplicationContext 独立开来，**不同微服务客户端使用不同的 ApplicationContext**。NamedContextFactory 就是用来实现这种机制的。

## NamedContextFactorys是什么

Spring Cloud框架使用NamedContextFactory创建一系列的运行上下文(ApplicationContext)，来让对应的Specification在这些上下文中创建实例对象。

这样使得各个子上下文中的实例对象相互独立，互不影响，可以方便地通过子上下文管理一系列不同的实例对象。

NamedContextFactory有三个功能，

- 一是创建AnnotationConfigApplicationContext**子**上下文
- 二是在子上下文中创建并获取Bean实例
- 三是当子上下文消亡时清除其中的Bean实例





![image-20210203125648796](../../../../assets/image-20210203125648796.png)



## 核心方法

```java
//构造方法
NamedContextFactory#NamedContextFactory(Class<?> defaultConfigType, String propertySourceName,String propertyName) 
//设置配置
NamedContextFactory#setConfigurations
//获取上下文名称
NamedContextFactory#getContextNames
//销毁
NamedContextFactory#destroy
```

## 测试类







## FeignContext

- 在OpenFeign中，FeignContext继承了NamedContextFactory，用于存储各类OpenFeign的组件实例

- FeignAutoConfiguration是OpenFeign的自动配置类，它会提供FeignContext实例。并且将之前注册的FeignClientSpecification通过setConfigurations方法设置给FeignContext实例。这里处理了默认配置类FeignClientsConfiguration和自定义配置类的替换问题。
  - 如果FeignClientsRegistrar没有注册自定义配置类，那么configurations将不包含FeignClientSpecification对象，
  - 否则会在setConfigurations方法中进行默认配置类的替换

FeignAutoConfiguration的相关代码如下所示：

```java
//FeignAutoConfiguration.java
@Autowired(required = false)
private List<FeignClientSpecification> configurations = new ArrayList<>();
@Bean
public FeignContext feignContext() {
    FeignContext context = new FeignContext();
    context.setConfigurations(this.configurations);
    return context;
}
//FeignContext.java
public class FeignContext extends NamedContextFactory<FeignClientSpecification> {
    public FeignContext() {
        //将默认的FeignClientConfiguration作为参数传递给构造函数
        super(FeignClientsConfiguration.class, "feign", "feign.client.name");
    }
}
```

### createContext

NamedContextFactory是FeignContext的父类，其createContext方法会创建具有名称的Spring的AnnotationConfigApplicationContext实例作为当前上下文的子上下文。

这些AnnotationConfigApplicationContext实例可以管理OpenFeign组件的不同实例。NamedContextFactory的实现如下代码所示：

```java
//NamedContextFactory.java
protected AnnotationConfigApplicationContext createContext(String name) {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    //获取该name所对应的configuration,如果有的话，就注册都子context中
    if (this.configurations.containsKey(name)) {
        for (Class<?> configuration : this.configurations.get(name)
                .getConfiguration()) {
            context.register(configuration);
        }
    }
    // 注册default的Configuration，也就是FeignClientsRegistrar类的registerDefaultConfiguration
      // 方法中注册的Configuration
    for (Map.Entry<String, C> entry : this.configurations.entrySet()) {()) {
        if (entry.getKey().startsWith("default.")) {
            for (Class<?> configuration : entry.getValue().getConfiguration()) {
                context.register(configuration);
            }
        }
    }
    // 注册PropertyPlaceholderAutoConfiguration和FeignClientsConfiguration配置类
    context.register(PropertyPlaceholderAutoConfiguration.class,this.defaultConfigType);
    // 设置子context的Environment的propertySource属性源
    // propertySourceName = feign; propertyName = feign.client.name
    context.getEnvironment().getPropertySources().addFirst(new MapPropertySource(
            this.propertySourceName,
            Collections.<String, Object> singletonMap(this.propertyName, name)));
    // 所有context的parent都相同，这样的话，一些相同的Bean可以通过parent context来获取
    if (this.parent != null) {
        context.setParent(this.parent);
    }
    context.setDisplayName(generateDisplayName(name));
    context.refresh();
    return context;
}
```

### DisposableBean

而由于NamedContextFactory实现了DisposableBean接口，当NamedContextFactory实例消亡时，Spring框架会调用其destroy方法，清除掉自己创建的所有子上下文和自身包含的所有组件实例。

NamedContextFactory的destroy方法如下所示：

```java
//NamedContextFactory.java
@Override
public void destroy() {
    Collection<AnnotationConfigApplicationContext> values = this.contexts.values();
    for(AnnotationConfigApplicationContext context : values) {
        context.close();
    }

this.contexts.clear();
}
```

NamedContextFactory会创建出AnnotationConfigApplicationContext实例，并以name作为唯一标识，然后每个AnnotationConfigApplicationContext实例都会**注册部分配置类**，从而可以给出一系列的基于配置类生成的组件实例，这样就可以基于name来管理一系列的组件实例，为不同的FeignClient准备不同配置组件实例，比如说Decoder、Encoder等。

