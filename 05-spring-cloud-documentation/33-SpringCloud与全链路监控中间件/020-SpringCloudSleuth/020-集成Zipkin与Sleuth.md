# 020-集成Zipkin与Sleuth

[TOC]

```xml
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-zipkin</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-sleuth</artifactId>
        </dependency>
```

```yaml
spring:
  sleuth:
    sampler:
      probability: ${ZIPKIN_RATE:1}
  rabbitmq:
    host: localhost
    port: 5672
    username: guest  # 使用MQ通讯
    password: guest
  zipkin:
    base-url: http://172.22.3.25:9411
```

