# SpringCloud Feign

在使用 SpringCloud 开发微服务应用的时候,各个服务都是以 HTTP 接口的形式对外提供服务,因此在服务消费者调用服务提供者时,底层通过 HTTP Client 的方式调用

- JDK 原生的 URLConnection
- Apache 的 HTTP client
- Netty 的异步 HTTP client
- Spring 的 RestTemplate
- OpenFeign 客户端

SpringCloud 对 Feign 进行了增强,使得 Feign 支持 SpringMVC 的注解,并整合了 Ribbon 等,从而让 Feign 的使用更加方便

## 什么是 Feign

- 生命式 WebService 客户端, 让 web service 客户端变得更加简单
- 支持编码器解码器
- SpringCloud Feign 支持 SpringMVC 注解
- 可以做到使用 HTTP 请求访问远程服务,就像是调用本地方法一样]

## Feign 的特性

- 可插拔的注解支持,包括 Feign 注解和 JAX-RS 注解
- 支持可插拔的 HTTP 编码器和解码器
- 支持 Hystrix 和它的 Fallback
- 支持 Ribbon 的负载均衡
- 支持 HTTP 请求和响应压缩

## 互见法

-  [如何引入 feign](../06-spring-cloud-open-feign/01-how-to-included-feign.md) 
-  [覆盖默认的配置](../06-spring-cloud-open-feign/02-overriding-feign-defaults.md) 
-  [手动创建 feign](../06-spring-cloud-open-feign/03-creating-feign-clients-manually.md) 
-  [hystrix-support](../06-spring-cloud-open-feign/04-feign-hystrix-support.md) 
-  [hystrix-fallbacks](../06-spring-cloud-open-feign/05-feign-hystrix-fallbacks.md) 
-  [06-feign-and-primary.md](../06-spring-cloud-open-feign/06-feign-and-primary.md) 
-  [07-feign-inheritance-support.md](../06-spring-cloud-open-feign/07-feign-inheritance-support.md) 
-  [08-feign-request-response-compression.md](../06-spring-cloud-open-feign/08-feign-request-response-compression.md) 
-  [09-feign-logging.md](../06-spring-cloud-open-feign/09-feign-logging.md) 
-  [10-feign-@querymap-support.md](../06-spring-cloud-open-feign/10-feign-@querymap-support.md) 
-  [11-hateoas-support.md](../06-spring-cloud-open-feign/11-hateoas-support.md) 

## Feign 的工作流程

- 主程序入口添加`@EnableFeignClients`注解开启对 Feign Client 扫描加载处理
- 定义接口并加`@FeignClents` 注解
- 当程序启动时,会进行包扫描,扫描所有`@FeignClients`标注的类,并将这些信息注入到 SpringIoc 容器中,当定义的 Feign 接口中的方法被调用的时候,通过 JDK 代理的方式,来生成具体的 requetTemplate 当生成代理时,Feign会为每个接口方法创建一个 RequestTemplate ,该对象封装了 HTTP 请求需要的全部信息,如请求参数名,请求方法等信息都是在这个过程中确定的
- 由 RequestTemplate 生成 RestTemplate , 然后把 Request 交给 Client去处理
  - Client 可以是 JDK 的 URLConnection
  - Client 可以是 Apache  的 HttpClient
  - Client 可以是 OKhttp
- Client 被封装到 LoadBalanceClient 类,这个类结合 Ribbon 负载均衡发起服务之间的调用



























