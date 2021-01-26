# 020-Eureka客户端

可以搭建包含Eurake Client依赖的Spring Boot项目。主要依赖有：
〈dependency〉 〈!--eureka-client相关依赖〉
    〈groupId〉org.springframework.cloud〈/groupId〉
    〈artifactId〉spring-cloud-starter-netflix-eureka-client〈/artifactId〉
〈/dependency〉
〈dependency〉
    〈groupId〉org.springframework.boot〈/groupId〉
    〈artifactId〉spring-boot-starter-web〈/artifactId〉
〈/dependency〉

启动类代码如下：
@SpringBootApplication
public class Chapter4EurekaClientApplication {
    public static void main(String[] args) {
        SpringApplication.run(Chapter4EurekaClientApplication.class, args);
    }
}

在application.yml添加eureka-client相关配置，代码如下所示：
eureka:
    instance:
        hostname: client

instance-id: ${spring.application.name}:${vcap.application.instance_id:${spring.application.instance_id:${random.value}}}
    client:
        service-url: // Eureka Server注册中心的地址，用于client与server进行交流
            defaultZone: http://localhost:8761/eureka/
server:
    port: 8765
spring:
    application:
        name: eureka-client

添加一个AskController向eureka-client-service请求sayHello的服务。通过使用可以进行负载均衡的RestTemplate向eureka-client-service发起打招呼的请求，并直接返回对应的响应结果。具体代码如下所示：