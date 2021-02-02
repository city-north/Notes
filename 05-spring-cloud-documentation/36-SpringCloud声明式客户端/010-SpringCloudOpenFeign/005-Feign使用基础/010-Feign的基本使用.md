# 010-Feign的基本使用

[TOC]

## 服务注册中心

OpenFeign可以配合Eureka等服务注册中心同时使用。Eureka作为服务注册中心，为OpenFeign提供服务端信息的获取，比如说服务的IP地址和端口。

## 服务提供者

Spring Cloud OpenFeign是声明式RESTful网络请求客户端，所以对服务提供者的实现方式没有任何影响。也就是说，服务提供者只需要提供对外的网络请求接口就可，至于其具体实现既可以使用 Spring MVC，也可以使用 Jersey。只需要确保该服务提供者被注册到服务注册中心上即可。

```java
@RestController
@RequestMapping("/feign-service")
public class FeignServiceController {
  @RequestMapping(value = "/instance/{serviceId}", method = RequestMethod.GET)
  public Instance getInstanceByServiceId(@PathVariable("serviceId") String serviceId){
    return new Instance(serviceId);
  }
}
```

如上述代码所示，通过@RestController和@RequestMapping声明了获取Instance资源的网络接口。

除了实现网络API接口之外，还需要将该服务注册到Eureka Server上。需要在application.yml文件中设置服务注册中心的相关信息和该应用的名称，相关配置如下所示：

```yaml
eureka:
    instance:
        instance-id: server:1
    client:
        service-url:
            default-zone: http://localhost:8761/eureka/
spring:
    application:
        name: feign-service
server:
    port: 9000
```

## 服务消费者

OpenFeign是声明式RESTful客户端，所以构建OpenFeign项目的关键在于构建服务消费者。通过下面的方法可以创建一个Spring Cloud OpenFeign的服务消费者项目。

首先需要在pom文件中添加Eureka和OpenFeign相关的依赖。然后在工程的入口类上添加@EnableFeignClients注解开启Spring Cloud OpenFeign的自动化配置功能，代码如下所示：

```java
@SpringBootApplication
@EnableFeignClients
public class FeignClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChapterFeignClientApplication.class, args);
    }
}
```

@EnableFeignClients就像是一个开关，只有使用了该注解，OpenFeign相关的组件和配置机制才会生效。

@EnableFeignClients还可以对OpenFeign相关组件进行自定义配置，它的方法和原理会在本章的源码分析章节再做具体的讲解。
接下来需要定义一个FeignServiceClient接口，通过@FeignClient注解来指定调用的远程服务名称。这一类被@FeignClient注解修饰的接口类一般被称为FeignClient。

在FeignClient接口类中，可以使用@RequestMapping定义网络请求相关的方法，如下所示：

```java
@FeignClient("feign-service")
@RequestMapping("/feign-service")
public interface FeignServiceClient {
    @RequestMapping(value = "/instance/{serviceId}", method = RequestMethod.GET)
    public Instance getInstanceByServiceId(@PathVariable("serviceId") String serviceId);
}
```

如上面代码片段所显示的，如果你调用FeignServiceClient对象的getInstanceByServiceId方法，那么OpenFeign就会向feign-service服务的/feign-service/instance/{serviceId}接口发送网络请求。

最后，在服务消费端项目的application.yml文件中配置Eureka服务注册中心的相关属性，具体配置如下所示：

```yaml
eureka:
    instance:
        instance-id: ${spring.application.name}:${vcap.application.instance_id:${spring.application.instance_id:${random.value}}}
    client:
        service-url:
            default-zone: http://localhost:8761/eureka/
spring:
    application:
        name: feign-client
server:
    port: 8770
```

相信读者通过搭建OpenFeign的项目，已经对OpenFeign的相关使用原理有了一定的了解，这个过程将对理解OpenFeign相关的工作原理大有裨益。

