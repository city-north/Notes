# Spring Cloud OpenFeign

这个项目主要提供了 OpenFeign 项目集成到 SpringBoot 的自动化配置以及绑定到 Spring Environment 和其他 Spring 编程模型

[OpenFeign](https://github.com/OpenFeign/feign)

## 声明式 REST Client: Feign

Feign 是一个声明式web 服务客户端,目标是让 Web 服务客户端的编写更加简单,使用也非常方便,创建一个接口然后加上注解

通过可插拔的注解支持:

- Feign 注释
- JAX-RS 注释

同时 Feign 也支持 可插拔的编码和解码器.Spring Cloud 在使用`HttpMessageConverters`添加了 对 SpringMVC 的支持,同时,在使用 Feign 的时候集成了 Ribbon 和 Eureka 还有 SpringCloud LoadBalancer 提供了一款负载均衡的 http 客户端

## 如何引入 Feign

```xml
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-openfeign</artifactId>
        </dependency>
```

Example spring boot app

```java
@SpringBootApplication
@EnableFeignClients
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
```

**StoreClient.java**

```java
@FeignClient("stores")
public interface StoreClient {
    @RequestMapping(method = RequestMethod.GET, value = "/stores")
    List<Store> getStores();

    @RequestMapping(method = RequestMethod.POST, value = "/stores/{storeId}", consumes = "application/json")
    Store update(@PathVariable("storeId") Long storeId, Store store);
}
```

`@FeignClient`注解里的 String ,上例子中的"stores",是任意的客户端名称,主要是用来创建一个

- Ribbon 负载均衡器

或者

- Spring Cloud loadBalancer 

你可以使用 url 属性,绝对路径或者是一个 hostname

应用程序上下文中`bean`的名称是接口的完全限定名。要指定自己的别名值，可以使用`@FeignClient`注释的限定符值。

上面的负载平衡器客户机希望发现“stores”服务的物理地址。如果您的应用程序是一个Eureka客户端，那么它将在Eureka服务注册表中解析服务。

> 为了保持向后兼容性，被用作默认的负载平衡器实现。但是，Spring Cloud Netflix Ribbon现在处于维护模式，所以我们建议使用`Spring Cloud LoadBalancer`来代替。为此，设置`spring.cloud.loadbalker .ribbon`的值设置false。

## [Overriding Feign Defaults](https://cloud.spring.io/spring-cloud-static/current/reference/htmlsingle/#spring-cloud-feign-overriding-defaults)

`Spring Cloud Feign`支持的一个核心概念是`客户端 (client)`。每个`feign`客户端都是组件集成的一部分，这些组件协同工作以根据需要联系远程服务器，而集成有一个名称，您可以将其作为使用`@FeignClient`注释的应用程序开发人员提供。

SpringCloud 允许你完全控制 Feign 客户端,你可以使用额外的配置类`FeignClientsConfiguration`,在 feign 客户端上指定`configuration`属性

```java
@FeignClient(name = "stores", configuration = FooConfiguration.class)
public interface StoreClient {
    //..
}
```

同时,在`name`属性和`url`属性里支持占位符

```java
@FeignClient(name = "${feign.name}", url = "${feign.url}")
public interface StoreClient {
    //..
}
```

Spring Cloud Netflix 提供了下面的默认的 bean ,以支持feign (`BeanType` beanName: `ClassName`):

- `Decoder` feignDecoder: `ResponseEntityDecoder` (which wraps a `SpringDecoder`)
- `Encoder` feignEncoder: `SpringEncoder`
- `Logger` feignLogger: `Slf4jLogger`
- `Contract` feignContract: `SpringMvcContract`
- `Feign.Builder` feignBuilder: `HystrixFeign.Builder`
- `Client` feignClient: if Ribbon is in the classpath and is enabled it is a `LoadBalancerFeignClient`, otherwise if Spring Cloud LoadBalancer is in the classpath, `FeignBlockingLoadBalancerClient` is used. If none of them is in the classpath, the default feign client is used.

你可以指定使用`OkHttpClient`还是`ApacheHttpClient`

- `feign.okhttp.enabled`

- `feign.ok.httpclient.enabled`

  在 classpath 下有它们以后,你可以自定义 HTTP 客户端,提供一个 bean 类型为

- `org.apache.http.impl.client.CloseableHttpClient	`:当你使用 Apache

- `okhttp3.OkHttpClient`当你使用 OKHTTP 的时候



当创建 feign 客户端的时候,netflix 没有提供下面的类,但是这些类它们创建的时候需要,会从 ApplicationContext 中查找

- `Logger.Level`
- `Retryer`
- `ErrorDecoder`
- `Request.Options`
- `Collection`
- `SetterFactory`
- `QueryMapEncoder`

可以通过替换上面类型的 bean 然后添加上`@FeignClient`配置,例如:

```java
@Configuration
public class FooConfiguration {
    @Bean
    public Contract feignContract() {
        return new feign.Contract.Default();
    }

    @Bean
    public BasicAuthRequestInterceptor basicAuthRequestInterceptor() {
        return new BasicAuthRequestInterceptor("user", "password");
    }
}
```

这就使用`feign.Contract.Default`替代了`SpringMvcContract`然后添加了一个 `RequestInterceptor`到 `RequestInterceptor`集合

`@FeignClient` 也可以用配置文件来配置 :

application.yml

```yml
feign:
  client:
    config:
      feignName:
        connectTimeout: 5000
        readTimeout: 5000
        loggerLevel: full
        errorDecoder: com.example.SimpleErrorDecoder
        retryer: com.example.SimpleRetryer
        requestInterceptors:
          - com.example.FooRequestInterceptor
          - com.example.BarRequestInterceptor
        decode404: false
        encoder: com.example.SimpleEncoder
        decoder: com.example.SimpleDecoder
        contract: com.example.SimpleContract
```

默认的配置可以使用 `@EnableFeignClients`  的属性`defaultConfiguration`来指定,差异是这里的配置会引用到所有的 feign 客户端上

如果你更愿意使用配置文件去配置所有的`@FeignClient`,你可以创建一个配置属性,使用`default` feign 的名称

application.yml

```yml
feign:
  client:
    config:
      default:
        connectTimeout: 5000
        readTimeout: 5000
        loggerLevel: basic
```

如果既创建了`@Configuration`bean 和配置文件属性,配置文件的优先级更大,它会覆盖掉`@Configuration`的值,但是你如果想修改`@COniguration`的优先级,你可以

```properties
feign.client.default-to-properties=false
```

> 如果你需要一个`ThreadLocal` 绑定的值没到你的`RequestInterceptor`你需要设置Hystrix线程关联策略`SEMAPHORE`

## [Creating Feign Clients Manually](https://cloud.spring.io/spring-cloud-static/current/reference/htmlsingle/#creating-feign-clients-manually)

