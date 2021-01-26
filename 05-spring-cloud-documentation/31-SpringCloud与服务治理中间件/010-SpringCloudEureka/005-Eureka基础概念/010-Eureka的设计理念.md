# 010-Eureka的设计理念

[TOC]

## Eureka架构图

![image-20201011191104917](../../../../assets/image-20201011191104917.png)

## 注册中心进行服务注册的3个核心问题

- [服务实例如何注册到服务中心](#服务实例如何注册到服务中心)
- [服务实例如何从服务中心剔除](#服务实例如何从服务中心剔除)
- [服务实例信息一致性](#服务实例信息一致性)

### 问题1:服务实例如何注册到服务中心

本质上是服务启动的时候,调用 Eureka Server 的 REST API 的 register 方法,去注册这个应用的实例

### 问题2:服务实例如何从服务中心剔除

Eureka 采用的是心跳模式

正常情况下,服务实例在关闭应用的时候,通过钩子方法或者其他生命周期回调去调用 Eureka Server 的 REST API 的 de-register 方法来删除自身服务实例的信息

服务实例挂掉的情况下: 使用心跳包来证明服务实例还是存活的,是健康的,是可以条用的;

### 问题3:服务实例信息一致性

服务注册以及发现中心不可能是单点的,势必是一个集群,那么服务实例注册信息如何在这个集群里保证一致呢?

- AP优于CP
- Peer to Peer 架构
- Zone以及Region设计
- self-preservation设计

### AP 优于 CP

分布式系统领域有个重要的 CAP 理论

| CAP                 | 解释       | 详解                                                         |
| ------------------- | ---------- | ------------------------------------------------------------ |
| Consistency         | 数据一致性 | 数据在存在多副本的情况下,可能由于网络,机器故障,软件系统等问题导致数据写入部分副本成功,部分副本失败,进而造成副本之间数据不一致,存在冲突,满足一致性要求对数据的更新操作完成之后,多副本的数据保持一致 |
| Availability        | 可用性     | 在任何时候,客户端对集群进行读写操作时,请求能够正常相应,即在一定的延时内完成 |
| Partition Tolerance | 分区容忍性 | 发生通讯故障的时候,整个集群被分割为多个无法相互通讯的分区时,集群仍然可用 |

对于分布式系统来说,一般网络条件相对不可控,出现网络分区是不可避免的,所以系统必须具备**分区容忍性**

**在分布式系统中,P 是一个客观存在的事实**

往往分布式系统设计在 AP 和 CP 之间选择

对于 Zookeeper ,它是"CP"的,之所以 C 加引号是因为 Zookeeper 默认并不是严格的强一致,比如客户端 A 提交了一个写操作,Zookeeper 在过半数节点操作成功之后就返回,此时假设客户端 B 的读操作请求到的是 A 写操作尚未同步到的节点,那么读取的就不是客户端 A写操作成功之后的数据

## Peer to Peer 架构

一般来说,分布式系统的数据在多个副本之间的复制方式,可分为主从复制和对等复制

### 主从复制

主从复制也就是广为人知的 Master-Slave 模式,一个主,多个从,

- 所有对数据的写操作都提交到主,然后主同步到从
- 可以细分为同步更新,异步更新,同步异步混合更新

值得注意的是

对于主从复制模式来讲,写操作的压力都在主副本上,它是整个系统的瓶颈,但是从副本可以帮主副本分担读请求

### 对等复制

Peer to Peer 模式,副本之间不分主从,任何副本都可以接收读写操作,然后每个副本之间互相进行数据更新

值得注意的是

- 任何副本都可以接收写请求,不存在写操作的压力瓶颈

#### 客户端

客户端一般通过如下配置 EurekaServer 的 peer 节点

```java
eureka:
  client:
    serviceUrl:
      # 注册中心地址
      defaultZone: http://ip:8000/eureka,http://ip:8002/eureka
      prefer-same-zone-eureka: true
```

- prefer-same-zone-eureka , 当有多个分区下,优先选择与应用实例所在分区一样的其他服务的实例,如果没有默认使用 defaultZone
- 客户端使用 quarantineSet 维护了一个不可用的 Eureka Server ,进行请求的时候,优先从可用列表中选择,如果请求失败则切换到下一个 Eureka Server 下进行充实,默认的重试次数是 3
- 为了防止每个 Client端都按照配置文件制定的顺序进行请求,造成 Eureka Server 节点请求分布不均衡的情况,Client 端有个定时任务,默认五分钟执行一次,来刷新并随机化 Eureka Server 的列表

#### 服务端

##### 注册中心之间的互相注册

Eureka Server 本身依赖的 Eureka Client , 也就是每个 Eureka Server 是作为其他 Eureka Server 的 Client ,在单个 Eureka Server 启动的时候,会有一个 syncUp 的操作,通过 Eureka Client 请求其他 EurekaServer 节点中的一个节点获取注册的应用实例信息,然后复制到其他 peer节点

##### 如何避免复制之间的死循环

Eureka Server 在执行复制操作的时候,使用 `HEADER_REPLICATION` 的 http header 来讲这个请求操作与普通应用实例的正常操作区分开来,这样其他 peer 节点接受到请求的时候,就不会再对它的 peer 节点进行复制操作,避免死循环

##### 数据同步 peer to peer 

前面说了 Eureka 是 peer to peer 模式的复制,有一个重要的问题就是复制的时候的冲突问题,Eureka 的两个解决方案

- LastDirtyTimestamp 
- heartbeat

##### LastDirtyTimestamp

开关 是`SyncWhenTimestampDiffers`配置,默认是开启的

- 当 `lastDirtyTimestamp` 值大于 Server 本地该实例的 `lastDirtyTimestamp` 值的时候,则表示 EurekaServer 之间出现了数据冲突,这个时候会返回 404,要求该实例重新进行注册
- 当 `lastDirtyTimestamp`  值小于 Server 本地该实例的 `lastDirtyTimestamp` 值,如果是 peer 节点的复制请求,则表示数据出现冲突,返回 409 给 peer 节点,要求其同步自己的最新的数据信息

peer 节点之间的互相复制并不能保证所有操作都能成功,因此 Eureka 还通过应用实例与 Server 之间的 heartbeat 也就是 renewLease 操作来进行数据的最终修修复,即如果发现了应实例数据与某个 Server 的数据出现不一致,则 server 返回 404,应用实例重新注册 register

### 总结一下

实际上服务端 peer-to-peer 进行复制的时候,使用的是最近时间戳和心跳包的方式

