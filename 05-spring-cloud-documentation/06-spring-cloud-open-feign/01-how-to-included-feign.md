# 如何引入 Feign

maven 引入方式

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

## 代码实例

```java
/**
 * FeignClient 实例
 *
 * @author EricChen 2019/12/25 15:35
 */
@FeignClient(value = "eccto-provider", fallback = ConsumerFeignFallback.class)
public interface ConsumerFeign {

    @GetMapping("/hello")
    String helloWorld();
}

```

