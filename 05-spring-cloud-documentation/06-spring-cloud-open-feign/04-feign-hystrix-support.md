# Feign 的 Hystrix 支持

如果 Hystrix 在 classpath 下,并且`feign.hystrix.enable = true`,Feign 会用一个断路器包装所有的方法,返回一个`com.netflix.hystrix.HystrixCommand`也是可以的,你可以使用 reactive patterns (调用.toObservable() 或者 `.toserve()`或者是同步使用`.queue()`)

要在每个客户端基础上禁用Hystrix支持，请创建一个普通的vanilla `Feign.Builder`,`Prototype`类型

```java
@Configuration
public class FooConfiguration {
        @Bean
    @Scope("prototype")
    public Feign.Builder feignBuilder() {
        return Feign.builder();
    }
}
```

在Spring Cloud Dalston发布之前，如果Hystrix在类路径上，那么在默认情况下，Feign会将所有方法封装在一个断路器中。在Spring Cloud Dalston中更改了此默认行为，以支持选择进入方法。