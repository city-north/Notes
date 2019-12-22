# Spring Cloud Circuit Breaker

Spring Cloud Circuit breaker provides an abstraction across different circuit breaker implementations. It provides a consistent API to use in your applications, letting you, the developer, choose the circuit breaker implementation that best fits your needs for your application.

Spring Cloud supports the following circuit-breaker implementations:

- [Netfix Hystrix](https://github.com/Netflix/Hystrix)
- [Resilience4J](https://github.com/resilience4j/resilience4j)
- [Sentinel](https://github.com/alibaba/Sentinel)
- [Spring Retry](https://github.com/spring-projects/spring-retry)