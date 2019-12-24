# Spring Cloud Circuit Breaker

Spring Cloud Circuit breaker 提供了一个抽象, 在这个抽象下,有多种实现类,提供了统一的 API 调用,开发者可以选择适合自己的实现方式

### 支持的断路器实现

- [Netfix Hystrix](https://github.com/Netflix/Hystrix)
- [Resilience4J](https://github.com/resilience4j/resilience4j)
- [Sentinel](https://github.com/alibaba/Sentinel)
- [Spring Retry](https://github.com/spring-projects/spring-retry)

## 核心概念

为了创建一个断路器,需要使用`CircuitBreakerFactory` API,在 classpath 下引入一个实现类,那么会默认配置好这个实现

使用这个 API 的示例

```java
@Service
public static class DemoControllerService {
    private RestTemplate rest;
    private CircuitBreakerFactory cbFactory;

    public DemoControllerService(RestTemplate rest, CircuitBreakerFactory cbFactory) {
        this.rest = rest;
        this.cbFactory = cbFactory;
    }

    public String slow() {
        return cbFactory.create("slow").run(() -> rest.getForObject("/slow", String.class), throwable -> "fallback");
    }

}
```

The `CircuitBreakerFactory.create` API creates an instance of a class called `CircuitBreaker`. The `run` method takes a `Supplier` and a `Function`. The `Supplier` is the code that you are going to wrap in a circuit breaker. The `Function` is the fallback that is executed if the circuit breaker is tripped. The function is passed the `Throwable` that caused the fallback to be triggered. You can optionally exclude the fallback if you do not want to provide one.

## Reactive 的断路器

If Project Reactor is on the class path, you can also use `ReactiveCircuitBreakerFactory` for your reactive code. The following example shows how to do so:

```java
@Service
public static class DemoControllerService {
    private ReactiveCircuitBreakerFactory cbFactory;
    private WebClient webClient;


    public DemoControllerService(WebClient webClient, ReactiveCircuitBreakerFactory cbFactory) {
        this.webClient = webClient;
        this.cbFactory = cbFactory;
    }

    public Mono<String> slow() {
        return webClient.get().uri("/slow").retrieve().bodyToMono(String.class).transform(
        it -> cbFactory.create("slow").run(it, throwable -> return Mono.just("fallback")));
    }
}
```

The `ReactiveCircuitBreakerFactory.create` API creates an instance of a class called `ReactiveCircuitBreaker`. The `run` method takes a `Mono` or a `Flux` and wraps it in a circuit breaker. You can optionally profile a fallback `Function`, which will be called if the circuit breaker is tripped and is passed the `Throwable` that caused the failure.

### 相关的配置要看专门的专题

- [Hystrix](https://cloud.spring.io/spring-cloud-static/spring-cloud-netflix/reference/html/#_circuit_breaker_spring_cloud_circuit_breaker_with_hystrix)
- [Resilience4J](https://cloud.spring.io/spring-cloud-static/spring-cloud-circuitbreaker/reference/html/spring-cloud-circuitbreaker.html#_configuring_resilience4j_circuit_breakers)
- [Sentinal](https://cloud.spring.io/spring-cloud-static/spring-cloud-circuitbreaker/reference/html/spring-cloud-circuitbreaker.html#_configuring_sentinel_circuit_breakers)
- [Spring Retry](https://cloud.spring.io/spring-cloud-static/spring-cloud-circuitbreaker/reference/html/spring-cloud-circuitbreaker.html#_configuring_spring_retry_circuit_breakers)

