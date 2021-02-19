# 030-@LoadBalanced-客户端负载均衡

[TOC]

## @LoadBalanced简单使用

创建RestTemplate实例的时候，使用@LoadBalanced注解可以将RestTemplate自动配置为使用负载均衡的状态。@LoadBalanced将使用Ribbon为RestTemplate执行负载均衡策略。
创建负载均衡的RestTemplate不再能通过自动配置来创建，必须通过配置类创建，具体实例如下所示：

```java
@Configuration
public class MyConfiguration {
    @LoadBalanced
    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
public class MyApplication {
    @Autowired
    private RestTemplate restTemplate;
    public String getMyApplicationName() {
        //使用restTemplate访问my-application微服务的/name接口
          String name = restTemplate.getForObject("http://my-application/name", String.class);
        return name;
    }
}
```

URI需要使用服务名来指定需要访问应用服务，Ribbon客户端将通过服务名从服务发现应用处获取具体的服务地址来创建一个完整的网络地址，以实现网络调用。

