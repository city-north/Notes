# 020-熔断器-CircuitBreaker-ReactiveCircuitBreaker

[TOC]

## 一言蔽之

抽象的断路器, 子类实现 run方法定制具体的需要断路器保护的逻辑

```java
public interface CircuitBreaker {

   default <T> T run(Supplier<T> toRun) {
      return run(toRun, throwable -> {
         throw new NoFallbackAvailableException("No fallback available.", throwable);
      });
   };
   <T> T run(Supplier<T> toRun, Function<Throwable, T> fallback);

}
```

