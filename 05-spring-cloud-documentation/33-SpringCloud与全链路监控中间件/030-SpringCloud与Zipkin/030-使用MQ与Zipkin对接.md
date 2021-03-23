# 030-使用MQ与Zipkin对接

[TOC]

在上一小节中，服务调用链路追踪实现，采用的是HTTP方式进行通信，并将数据持久化到内存中。在整合Zipkin时，还可以通过消息中间件来对日志信息进行异步收集。

本小节可以做两点优化，

- 首先是数据从保存在内存中改为持久化到数据库；

- 其次是将HTTP通信改为MQ异步方式通信，通过集成RabbitMQ或者Kafka，让Zipkin客户端将信息输出到MQ中，同时Zipkin Server从MQ中异步地消费链路调用信息。

## Zipkin Server

在Finchley之前的版本，通常的做法是构建一个Zipkin Stream服务器。Finchley版本将会和之前的版本有所不同，因为从Edgware版本开始，Zipkin Stream服务器已弃用。在Finchley版本中，Zipkin Stream已被彻底删除。Spring Cloud Sleuth建议使用Zipkin的原生支持(RabbitMQ和Kafka)来进行基于消息的跨度发送：

```shell
$ RABBIT_ADDRESSES=localhost STORAGE_TYPE=mysql MYSQL_USER=root MYSQL_PASS=pwd \ MYSQL_HOST=localhost java -jar zipkin.jar
```

通过定义RABBIT_ADDRESSES和STORAGE_TYPE等环境变量，我们指定了

- 启动Zipkin Server时的存储类型为MySQL，收集链路信息的方式为RabbitMQ。
- 还有更多可选的环境变量配置，读者可以通过openzipkin的官网查阅https://github.com/openzipkin/zipkin/。

需要注意的是，使用MySQL的方式存储数据，启动前需要新建名为zipkin的数据库，并初始化好Zipkin的SQL语句，

 [zipkin.md](zipkin.md) 

启动好Zipkin Stream Server之后，下面将会改进两个客户端服务的配置，使得客户端服务能够异步发送追踪日志信息。

## 客户端服务改进

首先，客户端服务需要引入Spring Cloud Stream和sleuth-stream的依赖，如下所示：

```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.amqp</groupId>
    <artifactId>spring-rabbit</artifactId>
 <dependency>
```

其次更改配置文件，去掉HTTP通信的配置，并配置RabbitMQ相关的属性，具体配置如下所示：

````yaml
spring:
    rabbitmq:
        host: ${RABBIT_ADDR:localhost}
        port: ${RABBIT_PORT:5672}
        username: guest
        password: guest
````

## 结果测试

经过改进，我们可以正常访问Zipkin Server并展示采集到的链路调用信息，客户端服务API接口的响应时间也相对得到了改善，不会出现某次请求耗时特别长的情况。

为了验证MQ通信使得数据不丢失的特点，首先将数据库中的数据清空，刷新Zipkin Server的界面，可以看到不再有数据。然后将Zipkin Server程序关闭，再多次访问客户端服务的地址。之后，我们重启Zipkin Server程序，启动成功后访问UI界面，很快看到页面有数据可以选择了。以上的操作结果说明Zipkin Server重启后，从MQ中成功获取了在关闭这段时间里客户端服务之间产生的信息数据，链路调用监控变得更加健壮。
在上述的实现中，Zipkin Server的存储是基于JDBC数据库，在测试环境中部署一段时间之后，访问Zipkin UI将会变得很慢，究其原因，是链路监控产生的数据非常多，当多个应用程序运行一段时间之后，数据分析变得异常困难。

Zipkin Server的存储也支持ElasticSearch，基于ElasticSearch的配置只需要将存储方式变为“elasticsearch”，如下所示：

```
$ STORAGE_TYPE=elasticsearch ES_HOSTS=http://myhost:9200 java -jar zipkin.jar
```

通过如上的方式启动Zipkin Server，即可实现存储类型基于ElasticSearch。对于线上的链路信息收集，推荐使用ElasticSearch存储，谨慎使用JDBC数据库。