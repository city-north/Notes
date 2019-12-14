# Client Size Load Balancer:Ribbon

客户端负载均衡,Ribben

## 简介

- Spring Cloud Ribbon 是一个基于 HTTP 和 TCP 的客户端负载均衡工具,基于 Netflix Ribbon 实现
- 将面向服务的 REST 模板请求自动转化成客户端负载均衡的服务调用
- Ribbon 是工具类框架,不需要单独部署,但是SpringCloud 构建的微服务和基础设置都使用到

- Feign 基于 Ribbon 实现的工具

## 服务端负载均衡

负载均衡 是对系统的高可用、 网络压力的缓解和处理能力扩容的重要手段之 一 。

- 软件负载均衡

硬件负载均衡 主要通过在服务器节点之间安装专门用于负载均衡的设备，比如 F5 等;

- 硬件负载均衡

软件负载均衡则 是通过在服务器上安装一 些具有均衡负载功能或模块的软件来完成请求分发工作， 比如 Nginx 等

### 特点

- 硬件负载均衡的设备或是软件负载均衡的软件模块都会维护一 个下挂可用的服务端清 单，通过心跳检测来剔除故障的服务端节点以保证清单中都是可以正常访问的服务端节点
- 当客户端发送请求到负载均衡设备的时候 ， 该设备按某种算法(比如线性轮询、 按权重负载、 按流量负载等)从维护的可用服务端清单中取出 一 台服务端的地址， 然后进行转发。



## 客户端负载均衡

客户端负载均衡和服务端负载均衡最大的不同点在千上面所提到的服务清单所存储 的位置。

- 在客户端负载均衡中， 所有客户端节点都维护着自己要访问的服务端清单， 而这些 服务端的清单来自于服务注册中心

- 同服务端负载均衡的架构类似， 在客户端负载均衡中也需要心跳去维护服务端清单的健康性

## 引入 Ribbon

- 服务提供者只需要启动多个服务实例并注册到一个注册中心或是多个相关联的服务 注册中心。
-  服务消费者直接通过调用被`@LoadBalanced `注解修饰过的 RestTemplate 来实现面 向服务的接口调用。

这样，我们就可以将服务提供者的高可用以及服务消费者的负载均衡调用一 起实现了。

## 代码实例

- [16-gateway-80](../00-code/note-spring-cloud/16-gateway-80) 

- [04-netflix-eureka-provider-8004](../00-code/note-spring-cloud/04-netflix-eureka-provider-8004) 

```java
/**
 * 02-netflix-eureka-client-8002 和  04-netflix-eureka-client-8004 都注册到了 Eureka
 *
 * 通过使用     @LoadBalanced 注解完成客户端负载均衡的实例
 *
 * @author EricChen 2019/12/13 14:43
 */
@RestController
public class RibbonExampleController {

    private static final String REST_URL_PREFIX = "http://study.eccto.cn:8002";
    private static final String EUREKA_REST_URL_PREFIX = "http://EUREKA-CLIENT";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/hello")
    public String hello() {
        return restTemplate.getForObject(EUREKA_REST_URL_PREFIX + "/hello", String.class);
    }


}
```

## 重试机制

SpringCloud整合了SpringRetry来增强RestTernplate的重试能力， 对于开发者来 说只需通过简单的配置， 原来那些通过RestTemplate实现的服务访问就会自动根据配 置来实现重试策略。

