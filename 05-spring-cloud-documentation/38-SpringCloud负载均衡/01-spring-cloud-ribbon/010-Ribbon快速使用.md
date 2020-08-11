# Robbon快速使用

对于使用,我们通常有两种方式

- [使用 @LoadBalancer 注解 注释 RestTemplate](#LoadBalancer)
- [使用  LoadBalancerClient](#LoadBalancerClient)

## LoadBalancer

#### 声明

```java
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder){
        return restTemplateBuilder.build();
    }

```

#### 注入使用

```java
		@Autowired
    RestTemplate restTemplate;

    @GetMapping("/user/{id}")
    public String findById(@PathVariable("id")int id){
        //TODO
        // 调用订单的服务获得订单信息
        // HttpClient  RestTemplate  OkHttp   JDK HttpUrlConnection
        return restTemplate.getForObject("http://spring-cloud-order-service/orders",String.class);
    }
```

根据 ServiceID 会实现自动负载均衡 ,这个服务列表甚至是可以写在配置文件

```yaml
spring-cloud-order-service.ribbon.listOfServers=\
  localhost:8080,localhost:8082
```

#### 源码分析

-  [Ribbon核心Bean的初始化](020-Ribbon核心Bean的初始化.md) 
-  [LoadBalancer源码分析](030-LoadBalancer源码分析.md) 

## LoadBalancerClient

#### 声明

```java
    @Autowired
    LoadBalancerClient loadBalancerClient;
```

#### 使用

```java
    @GetMapping("/user/{id}")
    public String findById(@PathVariable("id")int id){
        //TODO
        // 调用订单的服务获得订单信息
        // HttpClient  RestTemplate  OkHttp   JDK HttpUrlConnection
       	ServiceInstance serviceInstance=loadBalancerClient.choose("spring-cloud-order-service");
        String url=String.format("http://%s:%s",serviceInstance.getHost(),serviceInstance.getPort()+"/orders");
        return restTemplate.getForObject(url,String.class);
    }

```

可以看到上面的 loadBalancerClient , 使用 choose 方法选了了一个服务,并用 String 拼接了地址并用 Resttemplate 访问

#### 源码分析

-  [Ribbon核心Bean的初始化](020-Ribbon核心Bean的初始化.md) 

-  [LoadBalancerClient源码分析](040-LoadBalancerClient源码分析.md) 

