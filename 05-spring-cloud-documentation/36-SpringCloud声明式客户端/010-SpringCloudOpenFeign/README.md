# 010-SpringCloudOpenFeign

[TOC]

## Feign的工作流程图示

![image-20200726132143323](../../../assets/image-20200726132143323.png)

## Feign工作的基本流程

- 在微服务开发时,我们会在主程序使用 @EnableFeignClients 注解开启对 Feign Client 扫描加载处理,根据 Feign Client 的规范,定义接口并添加 @FeignClients 注解

- 当程序启动时,会进行包扫描,扫描所有标注 @FeignClients 的注解的类, 并将这些信息注入到 SpringIOC 容器中,当定义 Feign 接口的方法被调用时, 通过 **JDK 代理的方式** 生成具体的RestTemplate 

  > 在生成代理的时候,Feign 会为每一个接口方法创建一个 RequestTemplate 对象, 该对象封装了 HTTP 请求需要的全部信息,如  请求参数名   请求方法 等信息都是在这个过程中确定的

- 然后由 RequestTemplate 生成 Request , 然后把 Request 交给 Client 去处理

  Client 可以是

  - JDK 原生的URLConnection
  - Apache的Http Client
  - 也可以是 OKhttp

- 最后 Client 被封装到 LoadBalanceClient 类, 这个类交给 Ribbon 负载均衡发起服务之间的调用