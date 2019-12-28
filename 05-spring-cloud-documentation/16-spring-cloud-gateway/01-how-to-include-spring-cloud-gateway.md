## 添加依赖

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-gateway</artifactId>
</dependency>
```

引入依赖后的开关,默认是开启的

```properties
spring.cloud.gateway.enabled=false
```

不要引入`spring-boot-starter-web` 会有冲突

**Spring Cloud Gateway使用的是Spring Boot和`Spring Webflux`提供的Netty底层环境，不能和传统的Servlet容器一起使用，也不能打包成一个WAR包。**