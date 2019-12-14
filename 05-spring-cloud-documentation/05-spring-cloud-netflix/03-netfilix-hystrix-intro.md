# Spring Cloud Netflix Hystrix 介绍

Spring 容错保护

在微服务架构中， 我们将系统拆分成了很多服务单元， 各单元的应用间通过服务注册 与订阅的方式互相依赖.

存在着那么多的服务单元， 若 一个单元出现故障， 就很容易因依赖 关系而引发故障的蔓延，最终导致整个系统的瘫痪， 这样的架构相较传统架构更加不稳定。 为了解决这样的问题， 产生了断路器等一系列的服务保护机制。

## 微服务架构"雪崩问题"

在服务之间调用的链路上由于网络原因、资源繁忙或者自身的原因，服务并不能保证100%可用，如果单个服务出 现问题，调用这个服务就会出现线程阻塞，导致响应时间过长或不可用，此时若有大量的请求涌入，容器的线程资 源会被消耗完毕，导致服务瘫痪。服务与服务之间的依赖性，故障会传播，会对整个微服务系统造成灾难性的严重 后果，这就是服务故障的“雪崩”效应

## 分布式系统的断路器模式

在分布式架构中， 断路器模式的作用也是类似的， 当某个服务单元发生故障(类似用电器发生短路后).通过断路器的故障监控(类似于熔断保险丝)在调用方返回一个错误响应， 而不是长时间的等待。 这样就不会使得线程因调用故障服务被长时间占用不释放，避免了故障在分布式系统中的蔓延

## 配置Hystrix断路器

### [Default Configuration](https://cloud.spring.io/spring-cloud-static/Hoxton.RELEASE/reference/htmlsingle/#default-configuration)

通过自定义 Bean`HystrixCircuitBreakerFactory`或者`ReactiveHystrixCircuitBreakerFactory`,

下面`configureDefault`方法提供了默认的配置:

```java
@Bean
public Customizer<HystrixCircuitBreakerFactory> defaultConfig() {
    return factory -> factory.configureDefault(id -> HystrixCommand.Setter
            .withGroupKey(HystrixCommandGroupKey.Factory.asKey(id))
            .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
            .withExecutionTimeoutInMilliseconds(4000)));
}
```

### [Reactive Example](https://cloud.spring.io/spring-cloud-static/Hoxton.RELEASE/reference/htmlsingle/#reactive-example)

```java
@Bean
public Customizer<ReactiveHystrixCircuitBreakerFactory> defaultConfig() {
    return factory -> factory.configureDefault(id -> HystrixObservableCommand.Setter
            .withGroupKey(HystrixCommandGroupKey.Factory.asKey(id))
            .andCommandPropertiesDefaults(HystrixCommandProperties.Setter()
                    .withExecutionTimeoutInMilliseconds(4000)));
}
```

### [Specific Circuit Breaker Configuration](https://cloud.spring.io/spring-cloud-static/Hoxton.RELEASE/reference/htmlsingle/#specific-circuit-breaker-configuration)

你可以创建一个`Customize`Bean,传入`HystrixCircuitBreakerFactory`来自定义断路器

```java
@Bean
public Customizer<HystrixCircuitBreakerFactory> customizer() {
    return factory -> factory.configure(builder -> builder.commandProperties(
                    HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(2000)), "foo", "bar");
}
```

### [Reactive Example](https://cloud.spring.io/spring-cloud-static/Hoxton.RELEASE/reference/htmlsingle/#reactive-example-2)

```java
@Bean
public Customizer<ReactiveHystrixCircuitBreakerFactory> customizer() {
    return factory -> factory.configure(builder -> builder.commandProperties(
                    HystrixCommandProperties.Setter().withExecutionTimeoutInMilliseconds(2000)), "foo", "bar");
}
```

## 引入项目

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
</dependency>
```

添加`@EnableCircuitBreaker`

```java
@EnableCircuitBreaker 
@EnableDiscoveryClient 
@SpringBootApplication
public class EurekaConsumerExample {
    public static void main(String[] args) {
        SpringApplication.run(EurekaConsumerExample.class, args);
    }
}
```



可以直接使用`@SpringCloudApplication` 默认会添加`@EnableCircuitBreaker`

```
@SpringCloudApplication
public class EurekaConsumerExample {
    public static void main(String[] args) {
        SpringApplication.run(EurekaConsumerExample.class, args);
    }
}

```

