# 020-熔断器-CircuitBreaker-ReactiveCircuitBreaker

[TOC]

## 一言蔽之

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

