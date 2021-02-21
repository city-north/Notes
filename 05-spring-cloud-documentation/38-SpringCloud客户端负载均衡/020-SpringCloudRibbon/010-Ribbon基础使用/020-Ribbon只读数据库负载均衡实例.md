# 020-Ribbon只读数据库负载均衡实例.md

[TOC]

## 一言蔽之

我们学习了FeignLoadBalancer的原理和Ribbon的API之后，可以为任何需要负载均衡策略的项目添加Ribbon的集成。

## 需求

比如一个数据库中间件项目，它支持多个读库的数据读取，它希望在对多个读库进行数据读取时可以支持一定的负载均衡策略。那么，读者就可以通过集成Ribbon来实现读库之间的负载均衡。

- 首先，你需要定义DBServer类来继承Ribbon的Server类，用于存储只读数据库服务器的状态信息，比如说IP地址、数据库连接数、平均请求响应时间等，
- 然后定义一个DBLoadBalancer来继承BaseLoadBalancer类。

下述示例代码通过WeightedResponseTimeRule对DBServer列表进行负载均衡选择，然后使用自定义的DBPing来检测数据库是否可用。示例代码如下所示：

```java
public DBLoadBalancer buildFixedDBServerListLoadBalancer(List〈DBServer〉 servers) {
        IRule rule = new WeightedResponseTimeRule();
        IPing ping = new DBPing();
        DBLoadBalancer lb = new DBLoadBalancer(config, rule, ping);
        lb.setServersList(servers);
        return lb;
}
```

使用DBLoadBalancer的过程也很简单，通过LoadBalancerCommand的withLoadBalancer来使用它，然后在submit的回调函数中使用选出的数据库和SQL语句交给其他组件来执行SQL操作。DBConnectionLoadBalancer的具体实现如下所示：

```java
public class DBConnectionLoadBalancer {
    private final ILoadBalancer loadBalancer;
    private final RetryHandler retryHandler = new DefaultLoadBalancerRetryHandler (0, 1, true);
    public DBConnectionLoadBalancer(List〈DBServer〉 serverList) {
        loadBalancer = LoadBalancerBuilder.newBuilder().buildFixedDBServerListLoadBalancer(serverList);
    }
    public String executeSQL(final String sql) throws Exception {
        //使用LoadBalancerCommand来进行负载均衡，具体策略可以在Builder中进行设置
        return LoadBalancerCommand.〈String〉builder()
            .withLoadBalancer(loadBalancer)
            .build()
            .submit(new ServerOperation〈String〉() {
                @Override
                public Observable〈String〉 call(Server server) {
                    URL url;
                    try {
                        return Observable.just(DBManager.execute(server, sql));
                    } catch (Exception e) {
                        return Observable.error(e);
                    }
                }
```

```java
public DBLoadBalancer buildFixedDBServerListLoadBalancer(List〈DBServer〉 servers) {
        IRule rule = new WeightedResponseTimeRule();
        IPing ping = new DBPing();
        DBLoadBalancer lb = new DBLoadBalancer(config, rule, ping);
        lb.setServersList(servers);
        return lb;
}
```

使用DBLoadBalancer的过程也很简单，通过LoadBalancerCommand的withLoadBalancer来使用它，然后在submit的回调函数中使用选出的数据库和SQL语句`交给其他组件来执行SQL操作。DBConnectionLoadBalancer的具体实现如下所示：

```java
public class DBConnectionLoadBalancer {
    private final ILoadBalancer loadBalancer;
    private final RetryHandler retryHandler = new DefaultLoadBalancerRetryHandler (0, 1, true);
    public DBConnectionLoadBalancer(List〈DBServer〉 serverList) {
        loadBalancer = LoadBalancerBuilder.newBuilder().buildFixedDBServerListLoadBalancer(serverList);
    }
    public String executeSQL(final String sql) throws Exception {
        //使用LoadBalancerCommand来进行负载均衡，具体策略可以在Builder中进行设置
        return LoadBalancerCommand.〈String〉builder()
            .withLoadBalancer(loadBalancer)
            .build()
            .submit(new ServerOperation〈String〉() {
                @Override
                public Observable〈String〉 call(Server server) {
                    URL url;
                    try {
                        return Observable.just(DBManager.execute(server, sql));
                    } catch (Exception e) {
                        return Observable.error(e);
                    }
                });
                    }
                }
            }).toBlocking().first();
    }
}
```

ILoadBalancer通过一定的负载策略从读数据库列表中选出一个数据库来让DBManager执行SQL语句，通过这种方式，就可以实现读数据库的负载均衡机制。