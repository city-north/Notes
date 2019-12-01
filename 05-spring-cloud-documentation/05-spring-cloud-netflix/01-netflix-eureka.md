# Service Discory Eureka Server

Spring Cloud Eureka 是对 Netflix 的 Eureka 做了封装,主要用于服务治理

- Spring Cloud Eureka 提供, Eureka Server客户端与 Eureka Client

- 客户端内置一个轮询(round-robin) 负载算法的负载均衡器,应用启动后每 30 秒发送一次心跳
- 服务端(默认 90 秒)多个心跳周期内都没有接收到某个节点的心跳,服务注册表中移除这个服务节点

## 自我保护机制(Self-protection)

- Eureka Server 90秒内没有节后到某个微服务的心跳,Eureka Server 会从服务列表中将实例注销
- 当短时间内丢失过多客户端时(有可能本身Eureka 所在服务器网络故障),就会触发我保护模式
- 开启后,Eureka Server 就会保护服务注册表中的信息,不再删除注册表的数据
- 网络恢复时,Eureka Server 会退出自我保护模式

![image-20191130230726075](assets/image-20191130230726075.png)

## [Standalone Mode](https://cloud.spring.io/spring-cloud-static/Hoxton.RELEASE/reference/htmlsingle/#spring-cloud-eureka-server-standalone-mode)

单机模式

> The combination of the two caches (client and server) and the heartbeats make a standalone Eureka server fairly resilient to failure, as long as there is some sort of monitor or elastic runtime (such as Cloud Foundry) keeping it alive. In standalone mode, you might prefer to switch off the client side behavior so that it does not keep trying and failing to reach its peers. The following example shows how to switch off the client-side behavior:
>
> application.yml (Standalone Eureka Server)
>
> ```
> server:
>   port: 8761
> 
> eureka:
>   instance:
>     hostname: localhost
>   client:
>     registerWithEureka: false
>     fetchRegistry: false
>     serviceUrl:
>       defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
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

`application.yml (Three Peer Aware Eureka Servers)

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



## 代码

-  [01-netflix-eureka-server](../00-code/note-spring-cloud/01-netflix-eureka-server) 
-  [02-netflix-eureka-client](../00-code/note-spring-cloud/02-netflix-eureka-client) 