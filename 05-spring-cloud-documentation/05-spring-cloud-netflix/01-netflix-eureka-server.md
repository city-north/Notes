# Eureka Server

## 高可用

Eureka Server 没有后端存储,但是注册中心的服务实例都会发送心跳包以保证它们的注册信息时最新的

客户端也有一个基于内存的缓存 Eureka 注册实例,所以它们不需要每次请求都去注册中心获取最新的服务信息

默认情况下,每个 Eureka 服务器也是一个 Eureka 客户端,你可以指定一个服务的 URL 去配对,如果你不指定其他 Eureka,log 日志会输出

## [Standalone Mode](https://cloud.spring.io/spring-cloud-static/Hoxton.RELEASE/reference/htmlsingle/#spring-cloud-eureka-server-standalone-mode)

单机模式

> The combination of the two caches (client and server) and the heartbeats make a standalone Eureka server fairly resilient to failure, as long as there is some sort of monitor or elastic runtime (such as Cloud Foundry) keeping it alive. In standalone mode, you might prefer to switch off the client side behavior so that it does not keep trying and failing to reach its peers. The following example shows how to switch off the client-side behavior:
>
> application.yml (Standalone Eureka Server)
>
> ```
> server:
> port: 8761
> 
> eureka:
> instance:
>  hostname: localhost
> client:
>  registerWithEureka: false
>  fetchRegistry: false
>  serviceUrl:
>    defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
> ```
>
> Notice that the `serviceUrl` is pointing to the same host as the local instance.



## [Peer Awareness](https://cloud.spring.io/spring-cloud-static/Hoxton.RELEASE/reference/htmlsingle/#spring-cloud-eureka-server-peer-awareness)

Eureka can be made even more resilient and available by running multiple instances and asking them to register with each other. In fact, this is the default behavior, so all you need to do to make it work is add a valid `serviceUrl` to a peer, as shown in the following example:

application.yml (Two Peer Aware Eureka Servers)

```yaml
---
spring:
  profiles: peer1
eureka:
  instance:
    hostname: peer1
  client:
    serviceUrl:
      defaultZone: https://peer2/eureka/

---
spring:
  profiles: peer2
eureka:
  instance:
    hostname: peer2
  client:
    serviceUrl:
      defaultZone: https://peer1/eureka/
```

In the preceding example, we have a YAML file that can be used to run the same server on two hosts (`peer1` and `peer2`) by running it in different Spring profiles. You could use this configuration to test the peer awareness on a single host (there is not much value in doing that in production) by manipulating `/etc/hosts` to resolve the host names. In fact, the `eureka.instance.hostname` is not needed if you are running on a machine that knows its own hostname (by default, it is looked up by using `java.net.InetAddress`).

You can add multiple peers to a system, and, as long as they are all connected to each other by at least one edge, they synchronize the registrations amongst themselves. If the peers are physically separated (inside a data center or between multiple data centers), then the system can, in principle, survive “split-brain” type failures. You can add multiple peers to a system, and as long as they are all directly connected to each other, they will synchronize the registrations amongst themselves.

`application.yml (Three Peer Aware Eureka Servers)`

```yaml
eureka:
  client:
    serviceUrl:
      defaultZone: https://peer1/eureka/,http://peer2/eureka/,http://peer3/eureka/

---
spring:
  profiles: peer1
eureka:
  instance:
    hostname: peer1

---
spring:
  profiles: peer2
eureka:
  instance:
    hostname: peer2

---
spring:
  profiles: peer3
eureka:
  instance:
    hostname: peer3
```

