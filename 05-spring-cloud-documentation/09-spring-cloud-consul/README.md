# Spring Cloud Consul

Å这个项目通过SpringBoot 的自动配置以及绑定到 Spring Environment 和其他编程模型的方式,提供了 Consul 项目的集成

官网: https://learn.hashicorp.com/consul

使用简单的几个注解就可以配置Consul 的组件到通用模型到你的分布式系统中,包括

- Service Discovery
- Control Bus and Configuration 控制总线与配置

可以结合 Spring Cloud Netflix 的

- Zuul 智能网关
- Ribbon 客户端负载均衡
- Hystrix Circuit Breaker (Hystrix) 断路器

## Raft 协议



http://thesecretlivesofdata.com/raft/