# 020-Eureka服务注册中心核心概念

[TOC]

## 配置文件

```yaml
server:
    port: 8761
eureka:
    instance:
        hostname: standalone
        instance-id: ${spring.application.name}:${vcap.application.instance_id:$ {spring.application.instance_id:${random.value}}}
    client:
        register-with-eureka: false　# 表明该服务不会向Eureka Server注册自己的信息
        fetch-registry: false # 表明该服务不会向Eureka Server获取注册信息

service-url:　# Eureka Server注册中心的地址，用于client与server进行交流
            defaultZone: http://${eureka.instance.hostname}:${server.port}/eureka/
spring:
    application:
        name: eureka-service
```

## InstanceId概念

- InstanceId是Eureka服务的唯一标记，主要用于区分同一服务集群的不同实例

一般来讲，一个Eureka服务实例默认注册的InstanceId是它的主机名(即一个主机只有一个服务)。

但是这样会引发一个问题，一台主机不能启动多个属于同一服务的服务实例。为了解决这种情况，spring-cloud-netflix-eureka提供了一个合理的实现，如上面代码中的InstanceId设置样式。通过设置random.value可以使得每一个服务实例的InstanceId独一无二，从而可以唯一标记它自身。

## Eureka集群部署

Eureka Server既可以独立部署，也可以集群部署。在集群部署的情况下，Eureka Server间会进行注册表信息同步的操作，这时被同步注册表信息的Eureka Server将会被其他同步注册表信息的Eureka Server称为peer。

请注意，上述配置中的service-url指向的注册中心为实例本身。通常来讲，一个Eureka Server也是一个Eureka Client，它会尝试注册自己，所以需要至少一个注册中心的URL来定位对等点peer。如果不提供这样一个注册端点，注册中心也能工作，但是会在日志中打印无法向peer注册自己的信息。在独立(Standalone)Eureka Server的模式下，Eureka Server一般会关闭作为客户端注册自己的行为。

## 心跳包检测

Eureka Server与Eureka Client之间的联系主要通过心跳的方式实现。心跳(Heartbeat)即Eureka Client定时向Eureka Server汇报本服务实例当前的状态，维护本服务实例在注册表中租约的有效性。

## Eureka保持服务实例列表更新

### EurekaServer服务端

Eureka Server需要随时维持最新的服务实例信息，所以在注册表中的每个服务实例都需要定期发送心跳到Server中以使自己的注册保持最新的状态(数据一般直接保存在内存中)。

### Eureka客户端

为了避免Eureka Client在每次服务间调用都向注册中心请求依赖服务实例的信息，Eureka Client将定时从Eureka Server中拉取注册表中的信息，并将这些信息缓存到本地，用于服务发现。

## EurekaServer的actuator

启动Eureka Server后，应用会有一个主页面用来展示当前注册表中的服务实例信息并同时暴露一些基于HTTP协议的端点在/eureka路径下，这些端点将由Eureka Client用于注册自身、获取注册表信息以及发送心跳等。

| **Operation**                                                | **HTTP action**                                              | **Description**                                              |
| ------------------------------------------------------------ | ------------------------------------------------------------ | ------------------------------------------------------------ |
| Register new application instancezhu                         | POST /eureka/v2/apps/**appID**                               | Input: JSON/XML payload HTTP Code: 204 on success            |
| De-register application instance                             | DELETE /eureka/v2/apps/**appID**/**instanceID**              | HTTP Code: 200 on success                                    |
| Send application instance heartbeat                          | PUT /eureka/v2/apps/**appID**/**instanceID**                 | HTTP Code: * 200 on success * 404 if **instanceID** doesn’t exist |
| Query for all instances                                      | GET /eureka/v2/apps                                          | HTTP Code: 200 on success Output: JSON/XML                   |
| Query for all **appID** instances                            | GET /eureka/v2/apps/**appID**                                | HTTP Code: 200 on success Output: JSON/XML                   |
| Query for a specific **appID**/**instanceID**                | GET /eureka/v2/apps/**appID**/**instanceID**                 | HTTP Code: 200 on success Output: JSON/XML                   |
| Query for a specific **instanceID**                          | GET /eureka/v2/instances/**instanceID**                      | HTTP Code: 200 on success Output: JSON/XML                   |
| Take instance out of service                                 | PUT /eureka/v2/apps/**appID**/**instanceID**/status?value=OUT_OF_SERVICE | HTTP Code: * 200 on success * 500 on failure                 |
| Move instance back into service (remove override)            | DELETE /eureka/v2/apps/**appID**/**instanceID**/status?value=UP (The value=UP is optional, it is used as a suggestion for the fallback status due to removal of the override) | HTTP Code: * 200 on success * 500 on failure                 |
| Update metadata                                              | PUT /eureka/v2/apps/**appID**/**instanceID**/metadata?key=value | HTTP Code: * 200 on success * 500 on failure                 |
| Query for all instances under a particular **vip address**   | GET /eureka/v2/vips/**vipAddress**                           | * HTTP Code: 200 on success Output: JSON/XML * 404 if the **vipAddress** does not exist. |
| Query for all instances under a particular **secure vip address** | GET /eureka/v2/svips/**svipAddress**                         | * HTTP Code: 200 on success Output: JSON/XML * 404 if the **svipAddress** does not exist. |