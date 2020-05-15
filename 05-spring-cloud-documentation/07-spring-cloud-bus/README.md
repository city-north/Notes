# Spring Cloud Bus 消息总线

> Spring Cloud Bus links the nodes of a distributed system with a lightweight message broker. This broker can then be used to broadcast state changes (such as configuration changes) or other management instructions. A key idea is that the bus is like a distributed actuator for a Spring Boot application that is scaled out. However, it can also be used as a communication channel between apps. This project provides starters for either an AMQP broker or Kafka as the transport.

- Spring Cloud Bus 使用一个轻量级的消息中间件连接分布式系统的各个节点
- 中间件可以用来广播状态变化(例如配置更改) 
- bus 就像是一个分布式的 执行器`actuator`
- bus可以用来 app 之间的通讯

## quick start 

> Spring Cloud Bus works by adding Spring Boot autconfiguration if it detects itself on the classpath. To enable the bus, add `spring-cloud-starter-bus-amqp` or `spring-cloud-starter-bus-kafka` to your dependency management. Spring Cloud takes care of the rest. Make sure the broker (RabbitMQ or Kafka) is available and configured. When running on localhost, you need not do anything. If you run remotely, use Spring Cloud Connectors or Spring Boot conventions to define the broker credentials, as shown in the following example for Rabbit:

> application.yml
>
> ```
> spring:
>   rabbitmq:
>     host: mybroker.com
>     port: 5672
>     username: user
>     password: secret
> ```
>
> 

> The bus currently supports sending messages to all nodes listening or all nodes for a particular service (as defined by Eureka). The `/bus/*` actuator namespace has some HTTP endpoints. Currently, two are implemented. The first, `/bus/env`, sends key/value pairs to update each node’s Spring Environment. The second, `/bus/refresh`, reloads each application’s configuration, as though they had all been pinged on their `/refresh` endpoint.



### [2.2. Bus Env Endpoint](https://cloud.spring.io/spring-cloud-static/spring-cloud-bus/2.2.1.RELEASE/reference/html/#bus-env-endpoint)

>  The `/actuator/bus-env` endpoint updates each instances environment with the specified key/value pair across multiple instances.

>  To expose the `/actuator/bus-env` endpoint, you need to add following configuration to your application:

```properties
management.endpoints.web.exposure.include=bus-env
```

>  The `/actuator/bus-env` endpoint accepts `POST` requests with the following shape:

```json
{
    "name": "key1",
    "value": "value1"
}
```

## [3. Addressing an Instance](https://cloud.spring.io/spring-cloud-static/spring-cloud-bus/2.2.1.RELEASE/reference/html/#addressing-an-instance)

>  Each instance of the application has a service ID, whose value can be set with `spring.cloud.bus.id` and whose value is expected to be a colon-separated list of identifiers, in order from least specific to most specific. The default value is constructed from the environment as a combination of the `spring.application.name` and `server.port` (or `spring.application.index`, if set). The default value of the ID is constructed in the form of `app:index:id`, where:
>
> - `app` is the `vcap.application.name`, if it exists, or `spring.application.name`
> - `index` is the `vcap.application.instance_index`, if it exists, `spring.application.index`, `local.server.port`, `server.port`, or `0` (in that order).
> - `id` is the `vcap.application.instance_id`, if it exists, or a random value.
>
> The HTTP endpoints accept a “destination” path parameter, such as `/bus-refresh/customers:9000`, where `destination` is a service ID. If the ID is owned by an instance on the bus, it processes the message, and all other instances ignore it.



## [4. Addressing All Instances of a Service](https://cloud.spring.io/spring-cloud-static/spring-cloud-bus/2.2.1.RELEASE/reference/html/#addressing-all-instances-of-a-service)

The “destination” parameter is used in a Spring `PathMatcher` (with the path separator as a colon — `:`) to determine if an instance processes the message. Using the example from earlier, `/bus-env/customers:**` targets all instances of the “customers” service regardless of the rest of the service ID.

## [5. Service ID Must Be Unique](https://cloud.spring.io/spring-cloud-static/spring-cloud-bus/2.2.1.RELEASE/reference/html/#service-id-must-be-unique)

The bus tries twice to eliminate processing an event — once from the original `ApplicationEvent` and once from the queue. To do so, it checks the sending service ID against the current service ID. If multiple instances of a service have the same ID, events are not processed. When running on a local machine, each service is on a different port, and that port is part of the ID. Cloud Foundry supplies an index to differentiate. To ensure that the ID is unique outside Cloud Foundry, set `spring.application.index` to something unique for each instance of a service.

## [6. Customizing the Message Broker](https://cloud.spring.io/spring-cloud-static/spring-cloud-bus/2.2.1.RELEASE/reference/html/#customizing-the-message-broker)

Spring Cloud Bus uses [Spring Cloud Stream](https://cloud.spring.io/spring-cloud-stream) to broadcast the messages. So, to get messages to flow, you need only include the binder implementation of your choice in the classpath. There are convenient starters for the bus with AMQP (RabbitMQ) and Kafka (`spring-cloud-starter-bus-[amqp|kafka]`). Generally speaking, Spring Cloud Stream relies on Spring Boot autoconfiguration conventions for configuring middleware. For instance, the AMQP broker address can be changed with `spring.rabbitmq.*` configuration properties. Spring Cloud Bus has a handful of native configuration properties in `spring.cloud.bus.*` (for example, `spring.cloud.bus.destination` is the name of the topic to use as the external middleware). Normally, the defaults suffice.

To learn more about how to customize the message broker settings, consult the Spring Cloud Stream documentation.

## [7. Tracing Bus Events](https://cloud.spring.io/spring-cloud-static/spring-cloud-bus/2.2.1.RELEASE/reference/html/#tracing-bus-events)

Bus events (subclasses of `RemoteApplicationEvent`) can be traced by setting `spring.cloud.bus.trace.enabled=true`. If you do so, the Spring Boot `TraceRepository` (if it is present) shows each event sent and all the acks from each service instance. The following example comes from the `/trace` endpoint:

```json
{
  "timestamp": "2015-11-26T10:24:44.411+0000",
  "info": {
    "signal": "spring.cloud.bus.ack",
    "type": "RefreshRemoteApplicationEvent",
    "id": "c4d374b7-58ea-4928-a312-31984def293b",
    "origin": "stores:8081",
    "destination": "*:**"
  }
  },
  {
  "timestamp": "2015-11-26T10:24:41.864+0000",
  "info": {
    "signal": "spring.cloud.bus.sent",
    "type": "RefreshRemoteApplicationEvent",
    "id": "c4d374b7-58ea-4928-a312-31984def293b",
    "origin": "customers:9000",
    "destination": "*:**"
  }
  },
  {
  "timestamp": "2015-11-26T10:24:41.862+0000",
  "info": {
    "signal": "spring.cloud.bus.ack",
    "type": "RefreshRemoteApplicationEvent",
    "id": "c4d374b7-58ea-4928-a312-31984def293b",
    "origin": "customers:9000",
    "destination": "*:**"
  }
}
```

The preceding trace shows that a `RefreshRemoteApplicationEvent` was sent from `customers:9000`, broadcast to all services, and received (acked) by `customers:9000` and `stores:8081`.

To handle the ack signals yourself, you could add an `@EventListener` for the `AckRemoteApplicationEvent` and `SentApplicationEvent` types to your app (and enable tracing). Alternatively, you could tap into the `TraceRepository` and mine the data from there.

> Any Bus application can trace acks. However, sometimes, it is useful to do this in a central service that can do more complex queries on the data or forward it to a specialized tracing service.

## [8. Broadcasting Your Own Events](https://cloud.spring.io/spring-cloud-static/spring-cloud-bus/2.2.1.RELEASE/reference/html/#broadcasting-your-own-events)

The Bus can carry any event of type `RemoteApplicationEvent`. The default transport is JSON, and the deserializer needs to know which types are going to be used ahead of time. To register a new type, you must put it in a subpackage of `org.springframework.cloud.bus.event`.

To customise the event name, you can use `@JsonTypeName` on your custom class or rely on the default strategy, which is to use the simple name of the class.

### [8.1. Registering events in custom packages](https://cloud.spring.io/spring-cloud-static/spring-cloud-bus/2.2.1.RELEASE/reference/html/#registering-events-in-custom-packages)

If you cannot or do not want to use a subpackage of `org.springframework.cloud.bus.event` for your custom events, you must specify which packages to scan for events of type `RemoteApplicationEvent` by using the `@RemoteApplicationEventScan` annotation. Packages specified with `@RemoteApplicationEventScan` include subpackages.

For example, consider the following custom event, called `MyEvent`:

```java
package com.acme;

public class MyEvent extends RemoteApplicationEvent {
    ...
}
```

You can register that event with the deserializer in the following way:

```java
package com.acme;

@Configuration
@RemoteApplicationEventScan
public class BusConfiguration {
    ...
}
```

Without specifying a value, the package of the class where `@RemoteApplicationEventScan` is used is registered. In this example, `com.acme` is registered by using the package of `BusConfiguration`.

You can also explicitly specify the packages to scan by using the `value`, `basePackages` or `basePackageClasses` properties on `@RemoteApplicationEventScan`, as shown in the following example:

```java
package com.acme;

@Configuration
//@RemoteApplicationEventScan({"com.acme", "foo.bar"})
//@RemoteApplicationEventScan(basePackages = {"com.acme", "foo.bar", "fizz.buzz"})
@RemoteApplicationEventScan(basePackageClasses = BusConfiguration.class)
public class BusConfiguration {
    ...
}
```

All of the preceding examples of `@RemoteApplicationEventScan` are equivalent, in that the `com.acme` package is registered by explicitly specifying the packages on `@RemoteApplicationEventScan`.

## [9. Configuration properties](https://cloud.spring.io/spring-cloud-static/spring-cloud-bus/2.2.1.RELEASE/reference/html/#configuration-properties)

To see the list of all Bus related configuration properties please check [the Appendix page](https://cloud.spring.io/spring-cloud-static/spring-cloud-bus/2.2.1.RELEASE/reference/html/appendix.html).