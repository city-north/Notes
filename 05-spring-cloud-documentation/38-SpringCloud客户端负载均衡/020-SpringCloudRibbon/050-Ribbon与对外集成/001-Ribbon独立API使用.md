# 001-Ribbon独立API使用

[TOC]

## 一言蔽之

Ribbon除了与RestTemplate和OpenFegin一同使用之外，还可以依靠自己独立的API接口来实现特定需求。

## API构建ILoadBalancer

比如下面的代码中，

- 使用LoadBalancerBuilder的buildFixedServerListLoadBalancer创建出ILoadBalancer实例，然后使用LoadBalancerCommand.Builder的接口生成LoadBalancerCommand实例来发送网络请求。

- 通过调用LoadBalancerCommand的submit方法传入匿名的ServerOperation来完成网络请求的发送。
- LoadBalancerCommand的submit方法会使用你配置的负载均衡策略来选出一个服务器，然后调用匿名的ServerOperation的call方法，将选出的服务器传入，来进行网络传输的处理和操作。

```java
public class URLConnectionLoadBalancer {
    private final ILoadBalancer loadBalancer;
    private final RetryHandler retryHandler = new DefaultLoadBalancerRetryHandler(0, 1, true);
    public URLConnectionLoadBalancer(List〈Server〉 serverList) {
        //使用LoadBalancerBuilder的接口来创建ILoadBalancer实例
        loadBalancer = LoadBalancerBuilder.newBuilder().buildFixedServerListLoadBalancer(serverList);
    }
    public String call(final String path) throws Exception {
        //使用LoadBalancerCommand.Builder接口来配置Command实例，然后在回调方法中使用选中的服务器信息发送HTTP请求
        return LoadBalancerCommand.〈String〉builder()
                .withLoadBalancer(loadBalancer)
                .withRetryHandler(retryHandler)
                .build()
                .submit(new ServerOperation〈String〉() {
            @Override
            public Observable〈String〉 call(Server server) {
                URL url;
                try {
                    url = new URL("http://" + server.getHost() + ":" + server.getPort() + path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    return Observable.just(conn.getResponseMessage());
                } catch (Exception e) {
                    return Observable.error(e);
                }
            }
        }).toBlocking().first();
    }
    public LoadBalancerStats getLoadBalancerStats() {
        return ((BaseLoadBalancer) loadBalancer).getLoadBalancerStats();
    }
    public static void main(String[] args) throws Exception {
        URLConnectionLoadBalancer urlLoadBalancer = new URLConnectionLoadBalancer (Lists.newArrayList(new Server("www.google.com", 80),),
                new Server("www.linkedin.com", 80),
                new Server("www.yahoo.com", 80)));
        for (int i = 0; i 〈 6; i++) {
            System.out.println(urlLoadBalancer.call("/"));
        }
        System.out.println("=== Load balancer stats ===");
        System.out.println(urlLoadBalancer.getLoadBalancerStats());
    }
}
```

FeignLoadBalancer就是使用Ribbon的LoadBalancerCommand来实现有关OpenFeign的网络请求，只不过FeignLoadBalancer将网络请求交给其Client实例处理，而上边例子中的代码是交给HttpURLConnection处理。

使用Ribbon的独立API可以在任何项目中使用Ribbon所提供的负载均衡机制。

