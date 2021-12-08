# 030-自定义操作-Costomizer

[TOC]

## Costomizer简介

Costomizer自定义操作, 给CurcuitBrekerFactory默认的配置数据

CircuitBreaker 的定义和自定义的CircuitBreaker 对外提供方法一致, 并且多了一个有默认 fallback Function的重载方法

```java
public interface Customizer<TOCUSTOMIZE> {

	void customize(TOCUSTOMIZE tocustomize);

}
```

这个接口非常简单,  这里的TOCUSTOMIZE 表示自定义的行为, 通过自定义给HystrixCircuitBreakerFactory 默认的配置

```java
@Bean
public Customizer<HystrixCircuitBreakerFactory> customizer() {
    return factory -> {
        factory.configureDefault(id -> HystrixCommand.Setter.withGroupKey(
            HystrixCommandGroupKey.Factory.asKey(id))
            .andCommandPropertiesDefaults(
                HystrixCommandProperties.Setter()
                    .withMetricsRollingStatisticalWindowInMilliseconds(1000)
                    .withCircuitBreakerRequestVolumeThreshold(3)
                    .withCircuitBreakerErrorThresholdPercentage(100)
                    .withCircuitBreakerSleepWindowInMilliseconds(10000)
                    .withExecutionTimeoutInMilliseconds(5000)
            )
        );
    };
}
```

