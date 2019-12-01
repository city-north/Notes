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

- eureka 客户端配置以 `eureka.instance`开头
- 只要你配置了`spring.application.name`,那么默认值都可以直接使用

> To disable the Eureka Discovery Client, you can set `eureka.client.enabled` to `false`. Eureka Discovery Client will also be disabled when `spring.cloud.discovery.enabled` is set to `false`.

- `eureka.client.enabled`设置为 false ,客户端关闭

- `spring.cloud.discovery.enabled`设置为 false ,客户端自动关闭

## [Authenticating with the Eureka Server](https://cloud.spring.io/spring-cloud-static/Hoxton.RELEASE/reference/htmlsingle/#authenticating-with-the-eureka-server)

> HTTP basic authentication is automatically added to your eureka client if one of the `eureka.client.serviceUrl.defaultZone` URLs has credentials embedded in it (curl style, as follows: `user:password@localhost:8761/eureka`). For more complex needs, you can create a `@Bean` of type `DiscoveryClientOptionalArgs` and inject `ClientFilter` instances into it, all of which is applied to the calls from the client to the server.

- 设置属性`eureka.client.serviceUrl.defaultZone`为`user:password@localhost:8761/eureka`以开启默认的用户名密码
- 如果要更复杂,配置一个`DiscoveryClientOptionalArgs`的 Bean ,注入`ClientFilter`实例

> ​	Because of a limitation in Eureka, it is not possible to support per-server basic auth credentials, so only the first set that are found is used.

- 由于Eureka的限制，不可能支持集群服务器的基本身份验证凭据，因此只使用找到的第一个值。

## 状态页以及健康检查

> The status page and health indicators for a Eureka instance default to `/info` and `/health` respectively, which are the default locations of useful endpoints in a Spring Boot Actuator application. You need to change these, even for an Actuator application if you use a non-default context path or servlet path (such as `server.servletPath=/custom`). The following example shows the default values for the two settings:
>
> ```yaml
> eureka:
>   instance:
>     statusPageUrlPath: ${server.servletPath}/info
>     healthCheckUrlPath: ${server.servletPath}/health
> ```

- Eureka实例的默认检查是 `/info` 和`/health`
- 如果要更改参考上面配置文件

> These links show up in the metadata that is consumed by clients and are used in some scenarios to decide whether to send requests to your application, so it is helpful if they are accurate.

这些链接显示在客户端使用的元数据中，并在某些场景中用于决定是否向应用程序发送请求，因此如果这些链接是准确的，就会很有帮助。

## [Registering a Secure Application](https://cloud.spring.io/spring-cloud-static/Hoxton.RELEASE/reference/htmlsingle/#registering-a-secure-application)

HTTPS

> If your app wants to be contacted over HTTPS, you can set two flags in the `EurekaInstanceConfig`:
>
> - `eureka.instance.[nonSecurePortEnabled]=[false]`
> - `eureka.instance.[securePortEnabled]=[true]`
>
> Doing so makes Eureka publish instance information that shows an explicit preference for secure communication. The Spring Cloud `DiscoveryClient` always returns a URI starting with `https` for a service configured this way. Similarly, when a service is configured this way, the Eureka (native) instance information has a secure health check URL.

- 如果需要使用 HTTPS 参考上面代码

> Because of the way Eureka works internally, it still publishes a non-secure URL for the status and home pages unless you also override those explicitly. You can use placeholders to configure the eureka instance URLs, as shown in the following example:
>
> ```yaml
> eureka:
>   instance:
>     statusPageUrl: https://${eureka.hostname}/info
>     healthCheckUrl: https://${eureka.hostname}/health
>     homePageUrl: https://${eureka.hostname}/
> ```
>
> (Note that `${eureka.hostname}` is a native placeholder only available in later versions of Eureka. You could achieve the same thing with Spring placeholders as well — for example, by using `${eureka.instance.hostName}`.)

- Eureka 内部 home 页和 status 页依然使用的是 http ,所以要配置以上

- `${eureka.hostname} `新版本才能使用,你可以使用Spring placeholders`${eureka.instance.hostName}`

## [ Eureka’s Health Checks](https://cloud.spring.io/spring-cloud-static/Hoxton.RELEASE/reference/htmlsingle/#eurekas-health-checks)

健康检查

> By default, Eureka uses the client heartbeat to determine if a client is up. Unless specified otherwise, the Discovery Client does not propagate the current health check status of the application, per the Spring Boot Actuator. Consequently, after successful registration, Eureka always announces that the application is in 'UP' state. This behavior can be altered by enabling Eureka health checks, which results in propagating application status to Eureka. As a consequence, every other application does not send traffic to applications in states other then 'UP'. The following example shows how to enable health checks for the client:
>
> application.yml
>
> ```
> eureka:
>   client:
>     healthcheck:
>       enabled: true
> ```

- 默认 eureka 使用心跳包检测客户端是否启动
- 开启客户端的心跳检查,Eureka 会使用 SpringBoot 的的健康检查把状态专递给服务端
- 在客户端启动心跳检查参考上面I按配置

> `eureka.client.healthcheck.enabled=true` should only be set in `application.yml`. Setting the value in `bootstrap.yml` causes undesirable side effects, such as registering in Eureka with an `UNKNOWN` status.

- `eureka.client.healthcheck.enabled=true`这个属性应该设置在 application.yml 里
- 如果设置在`bootstrap.yml`里会引起例如注册进的服务状态是 UNKOWN的副作用

> If you require more control over the health checks, consider implementing your own `com.netflix.appinfo.HealthCheckHandler`.

- 使用`com.netflix.appinfo.HealthCheckHandler` 如果你要控制健康检查

## [Eureka Metadata for Instances and Clients](https://cloud.spring.io/spring-cloud-static/Hoxton.RELEASE/reference/htmlsingle/#eureka-metadata-for-instances-and-clients)

元数据

> It is worth spending a bit of time understanding how the Eureka metadata works, so you can use it in a way that makes sense in your platform. There is standard metadata for information such as hostname, IP address, port numbers, the status page, and health check. These are published in the service registry and used by clients to contact the services in a straightforward way. Additional metadata can be added to the instance registration in the `eureka.instance.metadataMap`, and this metadata is accessible in the remote clients. In general, additional metadata does not change the behavior of the client, unless the client is made aware of the meaning of the metadata. There are a couple of special cases, described later in this document, where Spring Cloud already assigns meaning to the metadata map.

- 有标准的元数据信息，如主机名、IP地址、端口号、状态页和健康检查。
- 你可以在`eureka.instance.metadataMap`中添加附件的元信息

##### [Using Eureka on Cloud Foundry](https://cloud.spring.io/spring-cloud-static/Hoxton.RELEASE/reference/htmlsingle/#using-eureka-on-cloud-foundry)

Cloud Foundry has a global router so that all instances of the same app have the same hostname (other PaaS solutions with a similar architecture have the same arrangement). This is not necessarily a barrier to using Eureka. However, if you use the router (recommended or even mandatory, depending on the way your platform was set up), you need to explicitly set the hostname and port numbers (secure or non-secure) so that they use the router. You might also want to use instance metadata so that you can distinguish between the instances on the client (for example, in a custom load balancer). By default, the `eureka.instance.instanceId` is `vcap.application.instance_id`, as shown in the following example:

application.yml

```
eureka:
  instance:
    hostname: ${vcap.application.uris[0]}
    nonSecurePort: 80
```

Depending on the way the security rules are set up in your Cloud Foundry instance, you might be able to register and use the IP address of the host VM for direct service-to-service calls. This feature is not yet available on Pivotal Web Services ([PWS](https://run.pivotal.io/)).

##### [Using Eureka on AWS](https://cloud.spring.io/spring-cloud-static/Hoxton.RELEASE/reference/htmlsingle/#using-eureka-on-aws)

If the application is planned to be deployed to an AWS cloud, the Eureka instance must be configured to be AWS-aware. You can do so by customizing the [EurekaInstanceConfigBean](https://github.com/spring-cloud/tree/master/spring-cloud-netflix-eureka-client/src/main/java/org/springframework/cloud/netflix/eureka/EurekaInstanceConfigBean.java) as follows:

```java
@Bean
@Profile("!default")
public EurekaInstanceConfigBean eurekaInstanceConfig(InetUtils inetUtils) {
  EurekaInstanceConfigBean b = new EurekaInstanceConfigBean(inetUtils);
  AmazonInfo info = AmazonInfo.Builder.newBuilder().autoBuild("eureka");
  b.setDataCenterInfo(info);
  return b;
}
```

##### [Changing the Eureka Instance ID](https://cloud.spring.io/spring-cloud-static/Hoxton.RELEASE/reference/htmlsingle/#changing-the-eureka-instance-id)

A vanilla Netflix Eureka instance is registered with an ID that is equal to its host name (that is, there is only one service per host). Spring Cloud Eureka provides a sensible default, which is defined as follows:

```
${spring.cloud.client.hostname}:${spring.application.name}:${spring.application.instance_id:${server.port}}}
```

An example is `myhost:myappname:8080`.

By using Spring Cloud, you can override this value by providing a unique identifier in `eureka.instance.instanceId`, as shown in the following example:

application.yml

```
eureka:
  instance:
    instanceId: ${spring.application.name}:${vcap.application.instance_id:${spring.application.instance_id:${random.value}}}
```

With the metadata shown in the preceding example and multiple service instances deployed on localhost, the random value is inserted there to make the instance unique. In Cloud Foundry, the `vcap.application.instance_id` is populated automatically in a Spring Boot application, so the random value is not needed.

## [Using the EurekaClient](https://cloud.spring.io/spring-cloud-static/Hoxton.RELEASE/reference/htmlsingle/#using-the-eurekaclient)

使用 Eureka 客户端

>Once you have an application that is a discovery client, you can use it to discover service instances from the [Eureka Server](https://cloud.spring.io/spring-cloud-static/Hoxton.RELEASE/reference/htmlsingle/#spring-cloud-eureka-server). One way to do so is to use the native `com.netflix.discovery.EurekaClient` (as opposed to the Spring Cloud `DiscoveryClient`), as shown in the following example:
>
>```java
>@Autowired
>private EurekaClient discoveryClient;
>
>public String serviceUrl() {
>    InstanceInfo instance = discoveryClient.getNextServerFromEureka("STORES", false);
>    return instance.getHomePageUrl();
>}
>```

- 如果你的应用是一个注册中心客户端,可以使用`com.netflix.discovery.EurekaClient`实例去发现其他服务

> Do not use the `EurekaClient` in a `@PostConstruct` method or in a `@Scheduled` method (or anywhere where the `ApplicationContext` might not be started yet). It is initialized in a `SmartLifecycle` (with `phase=0`), so the earliest you can rely on it being available is in another `SmartLifecycle` with a higher phase.

> You can also use the `org.springframework.cloud.client.discovery.DiscoveryClient`, which provides a simple API (not specific to Netflix) for discovery clients, as shown in the following example:
>
> ```
> @Autowired
> private DiscoveryClient discoveryClient;
> 
> public String serviceUrl() {
>     List<ServiceInstance> list = discoveryClient.getInstances("STORES");
>     if (list != null && list.size() > 0 ) {
>         return list.get(0).getUri();
>     }
>     return null;
> }
> ```

- 上面的例子展示了输出所有注册客户单信息的 url

## [Why Is It so Slow to Register a Service?](https://cloud.spring.io/spring-cloud-static/Hoxton.RELEASE/reference/htmlsingle/#why-is-it-so-slow-to-register-a-service)

> Being an instance also involves a periodic heartbeat to the registry (through the client’s `serviceUrl`) with a default duration of 30 seconds. A service is not available for discovery by clients until the instance, the server, and the client all have the same metadata in their local cache (so it could take 3 heartbeats). You can change the period by setting `eureka.instance.leaseRenewalIntervalInSeconds`. Setting it to a value of less than 30 speeds up the process of getting clients connected to other services. In production, it is probably better to stick with the default, because of internal computations in the server that make assumptions about the lease renewal period.

- 注册中心的一个周期心跳.默认 30 秒, 3 个周期如果没有响应判断这个服务挂掉(90s)
- `eureka.instance.leaseRenewalIntervalInSeconds`设置周期,设置小于 30 秒可以将客户端连接到其他服务的过程
- 在生产环境中，最好还是使用默认值，因为服务器中的内部计算会对租约的续订周期做出假设

## [Zones](https://cloud.spring.io/spring-cloud-static/Hoxton.RELEASE/reference/htmlsingle/#zones)

>  If you have deployed Eureka clients to multiple zones, you may prefer that those clients use services within the same zone before trying services in another zone. To set that up, you need to configure your Eureka clients correctly.

如果您已经将Eureka客户端部署到多个区域，那么您可能希望这些客户端在尝试其他区域中的服务之前先使用相同区域中的服务。要设置它，您需要正确地配置Eureka客户机。

> First, you need to make sure you have Eureka servers deployed to each zone and that they are peers of each other. See the section on [zones and regions](https://cloud.spring.io/spring-cloud-static/Hoxton.RELEASE/reference/htmlsingle/#spring-cloud-eureka-server-zones-and-regions) for more information.

首先，您需要确保将Eureka服务器部署到每个区域，并且它们彼此是对等的

> Next, you need to tell Eureka which zone your service is in. You can do so by using the `metadataMap` property. For example, if `service 1` is deployed to both `zone 1` and `zone 2`, you need to set the following Eureka properties in `service 1`:

接下来，您需要告诉Eureka您的服务所在的区域。您可以通过使用`metadataMap`属性来实现这一点。例如，如果`service 1`同时部署在`zone 1`和`zone 2`，你需要在“服务1”中设置以下Eureka属性:

**Service 1 in Zone 1**

```
eureka.instance.metadataMap.zone = zone1
eureka.client.preferSameZoneEureka = true
```

**Service 1 in Zone 2**

```
eureka.instance.metadataMap.zone = zone2
eureka.client.preferSameZoneEureka = true
```

#### [5.1.12. Refreshing Eureka Clients](https://cloud.spring.io/spring-cloud-static/Hoxton.RELEASE/reference/htmlsingle/#refreshing-eureka-clients)

>  By default, the `EurekaClient` bean is refreshable, meaning the Eureka client properties can be changed and refreshed. When a refresh occurs clients will be unregistered from the Eureka server and there might be a brief moment of time where all instance of a given service are not available. One way to eliminate this from happening is to disable the ability to refresh Eureka clients. To do this set `eureka.client.refresh.enable=false`.

- 默认 `EurekaClient`  bean 是可以刷新的,这意味着客户端属性可以被改变并且刷新
- 当发生刷新时，客户端将从Eureka服务器取消注册，可能会有一段短暂的时间，某个给定服务的所有实例都不可用。
- 消除这种情况的一种方法是禁用刷新Eureka客户机的功能:`eureka.client.refresh.enable=false`