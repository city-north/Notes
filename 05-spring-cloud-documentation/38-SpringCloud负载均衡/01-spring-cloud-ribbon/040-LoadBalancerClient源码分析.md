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