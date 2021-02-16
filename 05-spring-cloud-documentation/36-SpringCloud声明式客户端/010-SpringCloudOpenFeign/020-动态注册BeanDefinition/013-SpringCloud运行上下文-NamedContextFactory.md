# 013-SpringCloud运行上下文-NamedContextFactory

[TOC]



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

