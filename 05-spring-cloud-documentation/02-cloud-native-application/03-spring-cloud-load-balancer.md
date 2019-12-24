# Spring Cloud LoadBalancer

Spring Cloud 提供一个自己实现的客户端负载均衡器抽象和实现,负载均衡的机制`ReactiveLoadBalancer` interface ,有一个`Round-Robin-based ` (基于 RoundRobin 算法的) 的实现类

目前支持一个基于服务发现的`ServiceInstanceListSupplier`实现类,获取可用的示例,通过一个服务发现客户端

## Spring Cloud LoadBalancer 集成

为了能够方便地使用 SpringCloud LoadBalancer ,我们提供了

- `ReactorLoadBalancerExchangeFilterFunction`可以和`WebClient`结合使用
- `BlockingLoadBalancerClient`和`RestTemolate` 结合使用

示例:

- [Spring RestTemplate as a Load Balancer Client](https://cloud.spring.io/spring-cloud-static/current/reference/htmlsingle/#rest-template-loadbalancer-client)
- [Spring WebClient as a Load Balancer Client](https://cloud.spring.io/spring-cloud-static/current/reference/htmlsingle/#webclinet-loadbalancer-client)
- [Spring WebFlux WebClient with `ReactorLoadBalancerExchangeFilterFunction`](https://cloud.spring.io/spring-cloud-static/current/reference/htmlsingle/#webflux-with-reactive-loadbalancer)

## [Passing Your Own Spring Cloud LoadBalancer Configuration](https://cloud.spring.io/spring-cloud-static/current/reference/htmlsingle/#custom-loadbalancer-configuration)

使用`@LoadBalancerClient`注解配置你自己的负载均衡客户端配置

```java
@Configuration
@LoadBalancerClient(value = "stores", configuration = CustomLoadBalancerConfiguration.class)
public class MyConfiguration {

    @Bean
    @LoadBalanced
    public WebClient.Builder loadBalancedWebClientBuilder() {
        return WebClient.builder();
    }
}
```

You can use this feature to instantiate different implementations of `ServiceInstanceListSupplier` or `ReactorLoadBalancer`, either written by you, or provided by us as alternatives (for example `ZonePreferenceServiceInstanceListSupplier`) to override the default setup.

You can see an example of a custom configuration [here](https://cloud.spring.io/spring-cloud-static/current/reference/htmlsingle/#zoned-based-custom-loadbalancer-configuration).