# [Eureka Clients](https://cloud.spring.io/spring-cloud-static/Hoxton.RELEASE/reference/htmlsingle/#service-discovery-eureka-clients)

> Service Discovery is one of the key tenets of a microservice-based architecture. Trying to hand-configure each client or some form of convention can be difficult to do and can be brittle. Eureka is the Netflix Service Discovery Server and Client. The server can be configured and deployed to be highly available, with each server replicating state about the registered services to the others.

- 服务发现是微服务架构非常关键的原则
- Eureka 分为服务端与客户端,可以将服务器配置和部署为高可用性，每个服务器将注册服务的状态复制到其他服务器。

## 使用 Eureka注册

最简单的 Eureka 客户端

```java
@SpringBootApplication
@RestController
public class Application {

    @RequestMapping("/")
    public String home() {
        return "Hello world";
    }

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class).web(true).run(args);
    }

}
```

appliccation.yml

```yaml
eureka:
  client:
    serviceUrl:
      defaultZone: http://localhost:8761/eureka/
```

> In the preceding example, "defaultZone" is a magic string fallback value that provides the service URL for any client that does not express a preference (in other words, it is a useful default).

- defaultZone 设置注册中心地址

> The default application name (that is, the service ID), virtual host, and non-secure port (taken from the `Environment`) are `${spring.application.name}`, `${spring.application.name}` and `${server.port}`, respectively.

- 默认的应用名,从`Environment`中获取的服务 ID,服务名以及服务端口
- `${spring.application.name}`, `${spring.application.name}` and `${server.port}`, 

> Having `spring-cloud-starter-netflix-eureka-client` on the classpath makes the app into both a Eureka “instance” (that is, it registers itself) and a “client” (it can query the registry to locate other services). The instance behaviour is driven by `eureka.instance.*` configuration keys, but the defaults are fine if you ensure that your application has a value for `spring.application.name` (this is the default for the Eureka service ID or VIP).