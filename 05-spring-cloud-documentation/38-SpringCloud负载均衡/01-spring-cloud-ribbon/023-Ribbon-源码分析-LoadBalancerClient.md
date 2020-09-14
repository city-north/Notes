# Ribbon源码分析 LoadBalancerClient

LoadBalancerClient是Ribbon项目的核心类之一，可以在RestTemplate发送网络请求时替代RestTemplate进行网络调用

```java
// LoadBalancerClient.java
public interface LoadBalancerClient extends ServiceInstanceChooser {
    // 从serviceId所代表的服务列表中选择一个服务器来发送网络请求
    〈T〉 T execute(String serviceId, LoadBalancerRequest〈T〉 request) throws IOException;
    〈T〉 T execute(String serviceId, ServiceInstance serviceInstance, LoadBalancerRequest〈T〉 request) throws IOException;
    // 构建网络请求URI
    URI reconstructURI(ServiceInstance instance, URI original);
}

LoadBalancerClient接口继承了ServiceInstanceChooser接口，其choose方法可以从服务器列表中依据负载均衡策略选出一个服务器实例。ServiceInstanceChooser的定义如下所示：
//实现该类来选择一个服务器用于发送请求
public interface ServiceInstanceChooser {
    /**
     * 根据serviceId从服务器列表中选择一个ServiceInstance
     **/
    ServiceInstance choose(String serviceId);
}
```

RibbonLoadBalancerClient是LoadBalancerClient的实现类之一，它的execute方法会首先使用ILoadBalancer来选择服务器实例(Server)，然后将该服务器实例封装成RibbonServer对象，最后再调用LoadBalancerRequest的apply方法进行网络请求的处理。excute方法的具体实现如下所示：

```java
//RibbonLoadBalancerClient.java
public 〈T〉 T execute(String serviceId, LoadBalancerRequest〈T〉 request) throws IOException {
    //每次发送请求都会获取一个ILoadBalancer,会涉及负载均衡规则(IRule)、服务器列表集群(ServerList)和检验服务是否存在(IPing)等细节实现
    ILoadBalancer loadBalancer = getLoadBalancer(serviceId);
    Server server = getServer(loadBalancer);
    if (server == null) {
        throw new IllegalStateException("No instances available for " + serviceId);
    }
    RibbonServer ribbonServer = new RibbonServer(serviceId, server, isSecure(server,
            serviceId), serverIntrospector(serviceId).getMetadata(server));
    return execute(serviceId, ribbonServer, request);
}
```

getLoadBalancer方法直接调用了SpringClientFactory的getLoadBalancer方法。SpringClientFactory是NamedContextFactory的实现类，关于NamedContextFactory的机制在第5章中已经详细讲解过了，通过它可以实现多套组件实例的管理，代码如下所示：

```java
//RibbonLoadBalancerClient.java
protected ILoadBalancer getLoadBalancer(String serviceId) {
    return this.clientFactory.getLoadBalancer(serviceId);
}
```


getServer方法则是直接调用了ILoadBalancer的chooseServer方法来使用负载均衡策略—从已知的服务器列表中选出一个服务器实例，其具体实现如下所示：

```java
//RibbonLoadBalancerClient.java
protected Server getServer(ILoadBalancer loadBalancer) {
    if (loadBalancer == null) {

) {
        return null;
    }
    return loadBalancer.chooseServer("default");
}
```

execute方法调用LoadBalancerRequest实例的apply方法，将之前根据负载均衡策略选择出来的服务器作为参数传递进去，进行真正的HTTP请求发送，代码如下所示：

```java
//RibbonLoadBalancerClient.java
public 〈T〉 T execute(String serviceId, ServiceInstance serviceInstance, LoadBalancerRequest〈T〉 request) throws IOException {
    Server server = null;
    if(serviceInstance instanceof RibbonServer) {
        server = ((RibbonServer)serviceInstance).getServer();
    }
    RibbonLoadBalancerContext context = this.clientFactory
            .getLoadBalancerContext(serviceId);
    RibbonStatsRecorder statsRecorder = new RibbonStatsRecorder(context, server);
    try {
        T returnVal = request.apply(serviceInstance);
        statsRecorder.recordStats(returnVal);
        return returnVal;
    }
catch (IOException ex) {
    ...
    }
    return null;
}
```

LoadBalancerRequest的apply方法的具体实现本书不再详细讲解，因为Ribbon最为重要的部分就是使用负载均衡策略来选择服务器，也就是ILoadBalancer的chooseServer方法的实现，本书会在接下来的小节里对其进行详细讲解。
