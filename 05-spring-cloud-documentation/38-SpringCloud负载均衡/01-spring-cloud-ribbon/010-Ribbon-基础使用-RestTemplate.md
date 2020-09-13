# Ribbon-基础使用-RestTemplate

Ribbon可以和RestTemplate一起只用，也可以集成到Feign中进行使用

## 目录

- [RestTemplate简介](#RestTemplate简介)
- [RestTemplate和Ribbon一起使用方式](#RestTemplate和Ribbon一起使用方式)
- [覆盖Ribbon提供的默认组件实例](#覆盖Ribbon提供的默认组件实例)
- [使用application.yml配置策略](#使用application.yml配置策略)

## RestTemplate简介

RestTemplate是Spring提供的同步HTTP网络客户端接口，它可以简化客户端与HTTP服务器之间的交互，并且它**强制使用RESTful风格。**

它会处理HTTP连接和关闭，只需要使用者提供服务器的地址(URL)和模板参数。

## RestTemplate和Ribbon一起使用方式

- Spring Cloud为客户端负载均衡创建了特定的注解 **@LoadBalanced**，我们只需要使用该注解修饰创建RestTemplate实例的@Bean函数，Spring Cloud就会让RestTemplate使用相关的负载均衡策略，默认情况下是使用Ribbon。

- 除了@LoadBalanced之外，Ribbon还提供@RibbonClient注解。该注解可以为Ribbon客户端声明名称和自定义配置。name属性可以设置客户端的名称，configuration属性则会设置Ribbon相关的自定义配置类，如下所示：

```java
@SpringBootApplication
@RestController
@RibbonClient(name = "say-hello", configuration = RibbonConfiguration.class)
public class RibbonApplication {
    
  @LoadBalanced
  @Bean
  RestTemplate restTemplate(){
    return new RestTemplate();
  }
    
  @Autowired
  RestTemplate restTemplate;
  @RequestMapping("/hi")
  public String hi(@RequestParam(value="name", defaultValue="Artaban") String name) {
    String greeting = this.restTemplate.getForObject("http://say-hello/greeting", String.class);
    return String.format("%s, %s!", greeting, name);
  }
}
```

如上面代码所示，由于RestTemplate的Bean实例化方法restTemplate被 **@LoadBalanced** 修饰，所以当调用restTemplate的getForObject方法发送HTTP请求时，会使用Ribbon进行负载均衡。

@RibbonClient修饰了代码中的RibbonApplication类，声明了一个名为say-hello的Ribbon客户端，并且设置它的配置类为RibbonConfiguration，其代码如下所示：

```java
public class RibbonConfiguration {
  @Autowired
  IClientConfig ribbonClientConfig;
    
   //替换掉默认的ping策略 NoOpPing
  @Bean
  public IPing ribbonPing(IClientConfig config) {
    return new PingUrl();
  }
    //替换掉默认的负载均衡策略 ZoneAvoidanceRule 
  @Bean
  public IRule ribbonRule(IClientConfig config) {
    return new AvailabilityFilteringRule();
  }
}
```

## 覆盖Ribbon提供的默认组件实例

**使用者可以通过配置类创建组件实例来覆盖Ribbon提供的默认组件实例。**

如上代码所示，RibbonConfiguration配置类重载了IPing和IRule两个组件的实例，通过@Bean函数创建了

- PingUrl实例
- AvailabilityFilteringRule实例

来替换Ribbon默认提供的

- NoOpPing实例 , 判断服务是否可用策略
- ZoneAvoidanceRule 实例，负载均衡策略

通过这种方式，使用者可以依据自己的需求，更改Ribbon的组件实例，这也是Ribbon高可扩展性和高可修改性的体现。

## 使用application.yml配置策略

- 使用listOfServers硬编码服务地址
- 关闭从eureka 上获取服务注册信息

使用者可以在application.yml文件中对Ribbon进行配置，比如设置服务端列表或者使用Eureka来获取服务端列表，如下所示：

```yaml
say-hello:
  ribbon:
    eureka:
   # 将Eureka关闭，则Ribbon无法从Eureka中获取服务端列表信息
  enabled: false
   # listOfServers可以设置服务端列表
  say-hello:
    ribbon:
        eureka:
    # 将Eureka关闭，则Ribbon无法从Eureka中获取服务端列表信息
    enabled: false
    # listOfServers可以设置服务端列表
    listOfServers:localhost:8090,localhost:9092,localhost:9999
    serverListRefreshInterval: 15000
```

- Ribbon可以和服务注册中心Eureka一起工作，从服务注册中心获取服务端的地址信息，也可以在配置文件中使用listOfServers字段来设置服务端地址。由于listOfServers字段可以随意指定服务端地址，所以使用者往往在项目开发和测试阶段使用该字段。

除了和RestTemplate进行配套使用之外，Ribbon还默认被集成到了OpenFeign中，当使用@FeignClient时，OpenFeign默认使用Ribbon来进行网络请求的负载均衡
