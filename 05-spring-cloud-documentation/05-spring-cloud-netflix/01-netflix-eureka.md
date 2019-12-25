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



## 代码

-  [01-netflix-eureka-server](../00-code/note-spring-cloud/01-netflix-eureka-server) 
-  [02-netflix-eureka-client](../00-code/note-spring-cloud/02-netflix-eureka-client) 