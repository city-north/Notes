# [Feign and `@Primary`](https://cloud.spring.io/spring-cloud-static/spring-cloud-openfeign/2.2.0.RELEASE/reference/html/#feign-and-primary)

当使用 Feign 和 Hystrix fallback 时,`ApplicationConetext`的多个相同类型的 bean, 这样的话`@Autowired`去注入的话就找不到确切的 bean, 我们可以使用`@Primary`,如果不想要这个特性,可以使用`primary=false`关闭

```java
@FeignClient(name = "hello", primary = false)
public interface HelloClient {
    // methods here
}
```

