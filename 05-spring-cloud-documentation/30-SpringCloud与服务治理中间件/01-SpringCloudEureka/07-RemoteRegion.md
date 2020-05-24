# 构建 Multi Zone Eureka Server

## Eureka Server 实例

这里我们启动了四个 Eureka Server 实例, 配置 两个 zone, zone1 及 zone2 , 每个 zone 都有两个 Eureka Server 的实例, 这两个 zone 配置在同一个 region : region-east 上

> src/main/resources/application-zone1a.yml

```
server:
  port: 8081
spring:
  application:
    name: client
eureka:
  instance:
    metadataMap.zone: zone1
  client:
    register-with-eureka: true
    fetch-registry: true
    region: region-east
    service-url:
      zone1: http://localhost:8761/eureka/,http://localhost:8762/eureka/
      zone2: http://localhost:8763/eureka/,http://localhost:8764/eureka/
    availability-zones:
      region-east: zone1,zone2
```

> src/main/resources/application-zone1b.yml

```
server:
  port: 8762
spring:
  application:
    name: eureka-server
eureka:
  instance:
    hostname: localhost
    preferIpAddress: true
    metadataMap.zone: zone1
  client:
    register-with-eureka: true
    fetch-registry: true
    region: region-east
    service-url:
      zone1: http://localhost:8761/eureka/,http://localhost:8762/eureka/
      zone2: http://localhost:8763/eureka/,http://localhost:8764/eureka/
    availability-zones:
      region-east: zone1,zone2
  server:
      waitTimeInMsWhenSyncEmpty: 0
      enableSelfPreservation: false
```

> src/main/resources/application-zone2a.yml

```xml
server:
  port: 8763
spring:
  application:
    name: eureka-server
eureka:
  instance:
    hostname: localhost
    preferIpAddress: true
    metadataMap.zone: zone2
  client:
    register-with-eureka: true
    fetch-registry: true
    region: region-east
    service-url:
      zone1: http://localhost:8761/eureka/,http://localhost:8762/eureka/
      zone2: http://localhost:8763/eureka/,http://localhost:8764/eureka/
    availability-zones:
      region-east: zone1,zone2
  server:
      waitTimeInMsWhenSyncEmpty: 0
      enableSelfPreservation: false
```

> src/main/resources/application-zone2b.yml

```xml
server:
  port: 8764
spring:
  application:
    name: eureka-server
eureka:
  instance:
    hostname: localhost
    preferIpAddress: true
    metadataMap.zone: zone2
  client:
    register-with-eureka: true
    fetch-registry: true
    region: region-east
    service-url:
      zone1: http://localhost:8761/eureka/,http://localhost:8762/eureka/
      zone2: http://localhost:8763/eureka/,http://localhost:8764/eureka/
    availability-zones:
      region-east: zone1,zone2
  server:
      waitTimeInMsWhenSyncEmpty: 0
      enableSelfPreservation: false
```

上面配置了四个 Eureka Server 的配置文件,可以看到我们通过 `eureka.instance.metadataMap.zone` 设置了每个实例所属的 zone, 接下来分别使用者四个 profile 启动这四个 Eureka Server ,如下

```
mvn spring-boot:run -Dspring.profiles.active=zone1a
mvn spring-boot:run -Dspring.profiles.active=zone1b
mvn spring-boot:run -Dspring.profiles.active=zone2a
mvn spring-boot:run -Dspring.profiles.active=zone2b
```

## Eureka Client 实例

这里我们配置两个 Eureka Client , 分别属于 zone1 及 zone2 , 其配置文件



