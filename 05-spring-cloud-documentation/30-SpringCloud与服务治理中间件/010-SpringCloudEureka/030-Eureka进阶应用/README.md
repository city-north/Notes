# 030-Eureka服务注册和发现

[TOC]

## 简介

Eureka为Spring Cloud提供了高可用的服务发现与注册组件，利用Eureka，Spring Cloud开发者能够更快地融入到微服务的开发中。

Eureka Server作为服务注册中心，为Eureka Client提供服务注册和服务发现的能力，它既可单机部署，也可以通过集群的方式进行部署，通过自我保护机制和集群同步复制机制保证Eureka的高可用性和网络分区容忍性，保证Eureka Server集群的注册表数据的最终一致性；

Eureka Client方便了与Eureka Server的交互，它与Eureka Server的一切交互，包括服务注册、发送心跳续租、服务下线和服务发现，都是在后台自主完成的，简化了开发者的开发工作。

当然Eureka也存在缺陷。由于集群间的同步复制是通过HTTP的方式进行，基于网络的不可靠性，集群中的Eureka Server间的注册表信息难免存在不同步的时间节点，不满足CAP中的C(数据一致性)。