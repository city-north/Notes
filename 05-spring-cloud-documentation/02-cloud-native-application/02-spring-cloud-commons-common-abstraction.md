# Spring Cloud Commons : Common Abstractions

The common abstraction 包包含了许多服务的抽象:

- service discovery
- load balancing
- circuit breakers

但是实现由各个不同的项目区实现,如 `Eureka` or `Consul`

## [ `@EnableDiscoveryClient` 注解](https://cloud.spring.io/spring-cloud-static/current/reference/htmlsingle/#discovery-client)

这个注解主要寻找的是通过`META-INF/spring.factories` 文件`DiscoveryClient`和`ReactiveDiscoveryClient`接口的实现类, 配置的 key如下

```
org.springframework.cloud.client.discovery.EnableDiscoveryClient
```

SpringCloud 默认会提供 blocking and reactive 的服务发现客户端,你可以设置关闭或者开启

```properties
spring.cloud.discovery.blocking.enabled=false
spring.cloud.discovery.reactive.enabled=false
```

彻底关闭服务发现

```properties
spring.cloud.discovery.enabled=false
```

默认情况下`DiscoveryClient`的实现类自动注册了本地 SpringBoot 服务器到 服务发现,你可以使用属性关闭自动注册

```java
@EnableDiscoveryClient(autoRegister = false)
```



![image-20191223170249035](assets/image-20191223170249035.png)

系统中的默认实现类

- `DiscoveryClient (org.springframework.cloud.client.discovery)` 
- `CompositeDiscoveryClient (org.springframework.cloud.client.discovery.composite)`

组合客户端,用于组合其余两种

- `NoopDiscoveryClient (org.springframework.cloud.client.discovery.noop)`

没有在 classpath 下发现实现类的时候,会使用这个客户端

- `SimpleDiscoveryClient (org.springframework.cloud.client.discovery.simple)`

使用配置文件作为服务实例的源



> @EnableDiscoveryClient 不再需要.了,你可以设置一个DiscoveryClient 的实现类到 classpath来做到将 SpringBoot 应用注册到服务发现服务器

#### [Health Indicator](https://cloud.spring.io/spring-cloud-static/current/reference/htmlsingle/#health-indicator)

在 Commons 封装了健康监测,我们都知道, 一般情况下,微服务需要实现SpringBoot 中的`HealthIndicator`.Commons 中封装了一个叫做`DiscoveryHealthIndicator`的实现类

- `DiscoveryHealthIndicator (org.springframework.cloud.client.discovery.health)`
  - `EurekaHealthIndicator (org.springframework.cloud.netflix.eureka)`Eureka 实现
  - `DiscoveryClientHealthIndicator (org.springframework.cloud.client.discovery.health)`默认实现

我们可以关闭健康监测

```
spring.cloud.discovery.client.composite-indicator.enabled=false
```

关闭通用的服务发现客户端的健康监测:

```properties
spring.cloud.discovery.client.health-indicator.enabled=false
```

关闭`DiscoveryClientHealthIndicator`中的描述字段

```properties
spring.cloud.discovery.client.health-indicator.include-description=false
```

#### [Ordering `DiscoveryClient` instances](https://cloud.spring.io/spring-cloud-static/current/reference/htmlsingle/#ordering-discoveryclient-instances)

当使用多个服务发现客户单的时候可以使用排序功能,它们都实现了 Orderd 接口,默认为 0

![image-20191223171725085](assets/image-20191223171725085.png)

- ConsulDiscoveryClient
- EurekaDiscoveryClient
- ZookeeperDiscoveryClient

```
spring.cloud.{clientIdentifier}.discovery.order
```

#### 服务注册

Commons 提供了`ServiceRegistry`接口,提供了方法来进行注册和取消注册

```java
public interface ServiceRegistry<R extends Registration> {

	/**
	 * Registers the registration. A registration typically has information about an
	 * instance, such as its hostname and port.
	 * @param registration registration meta data
	 */
	void register(R registration);

	/**
	 * Deregisters the registration.
	 * @param registration registration meta data
	 */
	void deregister(R registration);

  (...)
}
```

我们可以直接把他当做 bean 来使用:

```java
@Configuration
@EnableDiscoveryClient(autoRegister=false)
public class MyConfiguration {
    private ServiceRegistry registry;

    public MyConfiguration(ServiceRegistry registry) {
        this.registry = registry;
    }

    // called through some external process, such as an event or a custom actuator endpoint
    public void register() {
        Registration registration = constructRegistration();
        this.registry.register(registration);
    }
}
```

每一个注册中心实现项目都有自己的实现类

- `ZookeeperRegistration` used with `ZookeeperServiceRegistry`
- `EurekaRegistration` used with `EurekaServiceRegistry`
- `ConsulRegistration` used with `ConsulServiceRegistry`

#### 自动注册

默认开启,想关闭的话

```
@EnableDiscoveryClient(autoRegister=false)
```

```properties
spring.cloud.service-registry.auto-registration.enabled=false
```

#### 自动注册事件

注册时会触发两个事件:

- `InstancePreRegisteredEvent`实例注册之前
- `InstanceRegisteredEvent`实例注册之后触发

你可以添加一个`ApplicationListener`来监听它们

#### 服务注册的 Endpoints 端点

#### 