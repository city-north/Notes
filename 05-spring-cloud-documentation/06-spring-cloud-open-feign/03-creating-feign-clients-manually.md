# 手动创建 Feign 客户端

除了使用注解的方式创建 Feign 客户端,还可以使用 Feign Builder API,下面的例子展示了创建两个 Feign 客户端,使用不同的拦截器 Interceptor

```java
@Import(FeignClientsConfiguration.class)
class FooController {

    private FooClient fooClient;

    private FooClient adminClient;

        @Autowired
    public FooController(Decoder decoder, Encoder encoder, Client client, Contract contract) {
        this.fooClient = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(contract)
                .requestInterceptor(new BasicAuthRequestInterceptor("user", "user"))
                .target(FooClient.class, "https://PROD-SVC");

        this.adminClient = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(contract)
                .requestInterceptor(new BasicAuthRequestInterceptor("admin", "admin"))
                .target(FooClient.class, "https://PROD-SVC");
    }
}
```

- FeignClientsConfiguration 类时 Spring Cloud Netflix 配置的默认配置类
- `PROD-SVC`是客户端将要请求的服务的名称
- `Contract`对象定义了什么注解和值在接口上是有效的,注入的`Contract`对象提供了对 Spring MVC 注解的支持,而不是默认的 Feign 默认注解



