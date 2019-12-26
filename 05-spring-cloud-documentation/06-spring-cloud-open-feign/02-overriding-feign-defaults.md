# 覆盖 Feign 的默认

`Spring Cloud Feign`支持的一个核心概念是`客户端 (client)`。每个`feign`客户端都是组件集成的一部分，这些组件协同工作以根据需要联系远程服务器，而集成有一个名称，您可以将其作为使用`@FeignClient`注释的应用程序开发人员提供。

Spring Cloud 使用`FeignClientsConfiguration`根据需要为每个指定的客户端创建一个新的集成作为`ApplicationContext`。它包含了一个`feign.Decoder`和一个`feign.Encoder`和一个`Feign.Contract`

你可以使用属性`contractId` 来覆盖它

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

application.yml

```yaml
# To disable Hystrix in Feign
feign:
  hystrix:
    enabled: false

# To set thread isolation to SEMAPHORE
hystrix:
  command:
    default:
      execution:
        isolation:
          strategy: SEMAPHORE
```

如果你想要用同一个名称或者 url创建多个 feign 客户端,用来只想同一个服务但是配置又不一样,这个时候你需要使用 contextId 属性

```java
@FeignClient(contextId = "fooClient", name = "stores", configuration = FooConfiguration.class)
public interface FooClient {
    //..
}
```

```java
@FeignClient(contextId = "barClient", name = "stores", configuration = BarConfiguration.class)
public interface BarClient {
    //..
}
```



### 代码实例 

[05-netflix-eureka-consumer-8005](../00-code/note-spring-cloud/05-netflix-eureka-consumer-8005) 

```java
/**
 * 自定义简单的拦截器,将 userName 和 password 写入到 header中
 *
 * @author EricChen 2019/12/26 21:11
 */
@Component
public class MyUserAuthInterceptor implements MyFeignRequestInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyUserAuthInterceptor.class);


    @Override
    public void apply(RequestTemplate template) {
        Map<String, Collection<String>> queries = template.queries();
        Collection<String> username = queries.get("username");
        Collection<String> password = queries.get("password");
        template.header("username",username);
        template.header("password",password);
    }
}

```

```java
/**
 * FeignClient 实例
 *
 * @author EricChen 2019/12/25 15:35
 */
@FeignClient(value = "eccto-provider", configuration = ConsumerFeignConfiguration.class, fallback = ConsumerFeignFallback.class)
public interface ConsumerFeign {

    @GetMapping("/hello?username=eric&password=123")
    String helloWorld();


    @GetMapping("/?username=eric&password=123")
    String index();
}

```

```java
/**
 * description
 *
 * @author EricChen 2019/12/26 20:55
 */
@Configuration
public class ConsumerFeignConfiguration {
//    @Bean
//    public Contract feignContract() {
//        return new feign.Contract.Default();
//    }

    /**
     * 添加自定义拦截器,可以拦截一些权限的校验信息
     * @return
     */
    @Bean
    public CompositeRequestInterceptor basicAuthRequestInterceptor(List<MyFeignRequestInterceptor> myFeignRequestInterceptors) {
        return new CompositeRequestInterceptor(myFeignRequestInterceptors);
    }
}

```

```java
/**
 * 组合自定义的拦截器
 *
 * @author EricChen 2019/12/26 21:00
 */
public class CompositeRequestInterceptor implements RequestInterceptor {

    private List<MyFeignRequestInterceptor> feignRequestInterceptors;

    public CompositeRequestInterceptor(List<MyFeignRequestInterceptor> myFeignRequestInterceptors) {
        feignRequestInterceptors  = Optional.ofNullable(myFeignRequestInterceptors).orElse(new ArrayList<>());

    }


    @Override
    public void apply(RequestTemplate requestTemplate) {
        feignRequestInterceptors.forEach(myFeignRequestInterceptor -> myFeignRequestInterceptor.apply(requestTemplate));
    }
}

```

