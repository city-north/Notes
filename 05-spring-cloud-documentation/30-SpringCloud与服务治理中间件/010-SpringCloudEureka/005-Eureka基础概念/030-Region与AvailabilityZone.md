# 030-Region与AvailabilityZone

[TOC]

## 为什么要设计Regin和Avaliablity

Eureka最初设计的目的是AWS(亚马逊网络服务系统)中用于部署分布式系统，所以首先对AWS上的区域(Regin)和可用区(Availability Zone)进行简单的介绍。

- 区域：AWS根据地理位置把某个地区的基础设施服务集合称为一个区域，区域之间相对独立。在架构图上，us-east-1c、us-east-1d、us-east-1e表示AWS中的三个设施服务区域，这些区域中分别部署了一个Eureka集群。

- 可用区：AWS的每个区域都是由多个可用区组成的，而一个可用区一般都是由多个数据中心(简单理解成一个原子服务设施)组成的。可用区与可用区之间是相互独立的，有独立的网络和供电等，保证了应用程序的高可用性。

  在上述的架构图中，一个可用区中可能部署了多个Eureka，一个区域中有多个可用区，这些Eureka共同组成了一个Eureka集群。

## Zooe 以及 Region 设计

由于 NetFlix 的服务大部分在 Amazon 上,因此 Eureka 的设置有一部分也基于 Amazon 的 Zone 和 Region 的基础设施上

Region: 代表一个独立的地理区间,比如 Eureka Server 默认设置了 4 个 Region

- us-east-1
- us-west-1
- us-west-2
- eu-west-1

![image-20200522130105602](../../../../assets/image-20200522130105602.png)

每一个 Region 下,分忧多个 AvailabilityZone ,一个 Region 对应多个 AvailabilityZone 

每个 Region 之间是相互独立以及隔离的,默认情况下,资源值在单个 Region 之间的 AvailabilityZone 之间复制,夸 Region 之间不会进行资源复制,Region 与 AvailiabilityZone 之间的关系如图

![image-20200522130713249](../../../../assets/image-20200522130713249.png)

> Availability Zone 可以看做是 Region 下的一个一个机房, 当一个机房挂掉,不影响其他机房

一个 `Availability Zone`可以设置多个实例,它们之间构成 peer节点,然后采用 peer to peer 的复制模式

