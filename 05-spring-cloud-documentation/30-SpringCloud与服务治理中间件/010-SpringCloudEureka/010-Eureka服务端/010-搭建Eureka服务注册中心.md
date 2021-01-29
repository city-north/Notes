# 010-搭建Eureka服务注册中心

[TOC]

## 搭建服务注册中心步骤

- 引入Eureka相关依赖
- 在启动类中添加注解@EnableEurekaServer
- application.yml配置文件中添加配置

可以搭建包含Eurake Server依赖的Spring Boot项目。主要依赖如下：

## 引入Eureka相关依赖

```xml
 <!--eureka-client相关依赖-->
<dependency>
  <groupId>org.springframework.cloud</groupId>
  <artifactId>spring-cloud-starter-netflix-eureka-server</artifactId>
</dependency>
<dependency> <!--actuator相关依赖-->
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
<dependency> <!--Spring Web MVC相关依赖-->
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

## 在启动类中添加注解@EnableEurekaServer

```java
@SpringBootApplication
//会为项目自动配置必须的配置类，标识该服务为注册中心
@EnableEurekaServer
public class Chapter4EurekaServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(Chapter4EurekaServerApplication.class, args);
    }
}
```

## application.yml配置文件中添加配置

在application.yml配置文件中添加以下配置，配置注册中心的端口和标识自己为Eureka Server：

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


