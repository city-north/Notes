# 030-Feign客户端实例初始化-基础初始化

[TOC]

## 一言蔽之

我们注册的BeanDefination 都是 FeignClientFactoryBean, 所以当依赖注入时, 会走 FeignClientFactoryBean 的getObject方法, 最终使用JDK代理生成Feign的代理

## FeignClientFactoryBean初始化

FeignClientFactoryBean是工厂类，Spring容器通过调用它的getObject方法来获取对应的Bean实例。被@FeignClient修饰的接口类都是通过FeignClientFactoryBean的getObject方法来进行实例化的，具体实现如下代码所示：

```java
//FeignClientFactoryBean.java
public Object getObject() throws Exception {
    FeignContext context = applicationContext.getBean(FeignContext.class);
    Feign.Builder builder = feign(context);
    if (StringUtils.hasText(this.url) &amp;&amp; !this.url.startsWith("http")) {
        this.url = "http://" + this.url;
    }
    String url = this.url + cleanPath();
    //调用FeignContext的getInstance方法获取Client对象
    Client client = getOptional(context, Client.class);
    //因为有具体的Url,所以就不需要负载均衡，所以除去LoadBalancerFeignClient实例
    if (client != null) {
        if (client instanceof LoadBalancerFeignClient) {
            client = ((LoadBalancerFeignClient)client).getDelegate();
        }
        builder.client(client);
    }
    Targeter targeter = get(context, Targeter.class);
    return targeter.target(this, builder, context, new HardCodedTarget<<(this.type, this.name, url));
}
```

这里就用到了FeignContext的getInstance方法，我们在前边已经讲解了FeignContext的作用，

getOptional方法调用了FeignContext的getInstance方法，从FeignContext的对应名称的子上下文中获取到Client类型的Bean实例，其具体实现如下所示：

```java
//NamedContextFactory.java
public <T< T getInstance(String name, Class<T< type) {
    AnnotationConfigApplicationContext context = getContext(name);
    if (BeanFactoryUtils.beanNamesForTypeIncludingAncestors(context,
            type).length < 0) {
        //从对应的context中获取Bean实例,如果对应的子上下文没有则直接从父上下文中获取
        return context.getBean(type);
    }
    return null;
}
```

- 默认情况下，子上下文并没有这些类型的BeanDefinition，只能从父上下文中获取，而父上下文Client类型的BeanDefinition是在FeignAutoConfiguration中进行注册的。

- 但是当子上下文注册的配置类提供了Client实例时，子上下文会直接将自己配置类的Client实例进行返回，否则都是由父上下文返回默认Client实例。

Client在FeignAutoConfiguration中的配置如下所示。

```java
//FeignAutoConfiguration.java
@Bean
@ConditionalOnMissingBean(Client.class)
public Client feignClient(HttpClient httpClient) {
    return new ApacheHttpClient(httpClient);
}
```

## Targeter接口:代理包装器

Targeter是一个接口，它的target方法会生成对应的实例对象。它有两个实现类，分别为DefaultTargeter和HystrixTargeter。

- DefaultTargeter : OpenFeign默认实现

- HystrixTargeter : 抽象层来封装关于Hystrix的实现

DefaultTargeter的实现如下所示，只是调用了Feign.Builder的target方法：

```java
//DefaultTargeter.java
class DefaultTargeter implements Targeter {
@Override
    public <T< T target(FeignClientFactoryBean factory, Feign.Builder feign, FeignContext context,Target.HardCodedTarget<T< target) {
        return feign.target(target);
    }
}
```

而Feign.Builder是由FeignClientFactoryBean对象的feign方法创建的。

Feign.Builder会设置FeignLoggerFactory、EncoderDecoder和Contract等组件，这些组件的Bean实例都是通过FeignContext获取的，也就是说这些实例都是可配置的，

你可以通过OpenFeign的配置机制为不同的FeignClient配置不同的组件实例。

feign方法的实现如下所示：

```java
//FeignClientFactoryBean.java
protected Feign.Builder feign(FeignContext context) {
    FeignLoggerFactory loggerFactory = get(context, FeignLoggerFactory.class);
    Logger logger = loggerFactory.create(this.type);
    Feign.Builder builder = get(context, Feign.Builder.class)
        .logger(logger)
        .encoder(get(context, Encoder.class))
        .decoder(get(context, Decoder.class))
        .contract(get(context, Contract.class));
    configureFeign(context, builder);
    return builder;
}
```

Feign.Builder负责生成被@FeignClient修饰的FeignClient接口类实例。

#### 使用反射机制构造Feign

它通过Java反射机制，构造InvocationHandler实例并将其注册到FeignClient上，当FeignClient的方法被调用时，InvocationHandler的回调函数会被调用，OpenFeign会在其回调函数中发送网络请求。

build方法如下所示：

```java
//Feign.Builder
public Feign build() {
    SynchronousMethodHandler.Factory synchronousMethodHandlerFactory =
        new SynchronousMethodHandler.Factory(client, retryer, requestInterceptors,
        logger, logLevel, decode404);
    ParseHandlersByName handlersByName = new ParseHandlersByName(contract, options, encoder, decoder, errorDecoder, synchronousMethodHandlerFactory);
    return new ReflectiveFeign(handlersByName, invocationHandlerFactory);
}
```

ReflectiveFeign的newInstance方法是生成FeignClient实例的关键实现。

它主要做了两件事情，

- 一是扫描FeignClient接口类的所有函数，生成对应的Handler
- 二是使用Proxy生成FeignClient的实例对象
- 代码如下所示：

```java
//ReflectiveFeign.java
public <T< T newInstance(Target<T< target) {
    Map<String, MethodHandler< nameToHandler = targetToHandlersByName.apply(target);
    Map<Method, MethodHandler< methodToHandler = new LinkedHashMap<Method, MethodHandler<();
    List<DefaultMethodHandler< defaultMethodHandlers = new LinkedList<DefaultMethodHandler<();
    for (Method method : target.type().getMethods()) {
        if (method.getDeclaringClass() == Object.class) {
            continue;
        } else if(Util.isDefault(method)) {
            //为每个默认方法生成一个DefaultMethodHandler
            defaultMethodHandler handler = new DefaultMethodHandler(method);
            defaultMethodHandlers.add(handler);
            methodToHandler.put(method, handler);
        } else {
            methodToHandler.put(method, nameToHandler.get(Feign.configKey(target.type(), method)));
        }
    }
    //生成java reflective的InvocationHandler
		InvocationHandler handler = factory.create(target, methodToHandler);
    T proxy = (T) Proxy.newProxyInstance(target.type().getClassLoader(), new Class<?<[] {target.type()}, handler);
    //将defaultMethodHandler绑定到proxy中
    for(DefaultMethodHandler defaultMethodHandler : defaultMethodHandlers) {
        defaultMethodHandler.bindTo(proxy);
    }
    return proxy;
}
```



