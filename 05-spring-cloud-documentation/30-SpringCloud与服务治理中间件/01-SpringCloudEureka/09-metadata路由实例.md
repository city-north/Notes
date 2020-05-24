# 基于 Metadata 路由实例

对于 Eureka 来说,最常见的就是通过metadata 属性,进行灰度 控制或者是不宕机升级.这里结合 Netfilix Ribbon 的例子,介绍

## ILoadBalancer 接口

Netflix Ribbon  的 LoadBalaner 接口定义了 loadBalancer 的几个基本方法,如下

```java
public interface ILoadBalancer {


	public void addServers(List<Server> newServers);
	

	public Server chooseServer(Object key);

	public void markServerDown(Server server);
	

	@Deprecated
	public List<Server> getServerList(boolean availableOnly);


  public List<Server> getReachableServers();

	public List<Server> getAllServers();
}

```

- chooseServer 用于从一对服务实例中进行过滤,选取一个 Server 出来,给客户端请求用

Ribbon 中, ILoadBalancer 选取 Server 的逻辑主要由一系列 IRule 来实现

## IRule 接口



```java
public interface IRule{
    /*
     * choose one alive server from lb.allServers or
     * lb.upServers according to key
     * 
     * @return choosen Server object. NULL is returned if none
     *  server is available 
     */

    public Server choose(Object key);
    
    public void setLoadBalancer(ILoadBalancer lb);
    
    public ILoadBalancer getLoadBalancer();    
}

```

最常见的IRule 接口有 RoundRobinRule , 采用轮询调度算法规则来选取 Server,

```
public Server choose (ILoadBalancer 1b, Object key)
```

## MetadataAwarePredicate

我们需要根据实例的 metadata 进行过滤,因此,可以自定义实现自己的 rule, Netflix 提供了 PredicateBasedRule , 可以基于 Guava 的 Predicate 进行过滤, jmnarloch

```
https://jmnarloch.wordpress.com/2015/11/25/spring-cloud-ribbon-dynamic-routing
```

```java
/**
 * A default implementation of {@link DiscoveryEnabledServer} that matches the instance against the attributes
 * registered through
 *
 * @author Jakub Narloch
 * @see DiscoveryEnabledPredicate
 */
public class MetadataAwarePredicate extends DiscoveryEnabledPredicate {

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean apply(DiscoveryEnabledServer server) {

        final RibbonFilterContext context = RibbonFilterContextHolder.getCurrentContext();
        final Set<Map.Entry<String, String>> attributes = Collections.unmodifiableSet(context.getAttributes().entrySet());
        final Map<String, String> metadata = server.getInstanceInfo().getMetadata();
        return metadata.entrySet().containsAll(attributes);
    }
}

```

这个 Predicate 将 Server 的 metadata 跟上下文传递 的 attributes 信息进行匹配,全部匹配才能返回 true

比如 attributes 的 map 有个 entry , key 是 env , value 是 canary , 如果请求上下文要求路由到 canary 实例,可以从 request url 参数 或者 header 中标识这个路由请求,然后携带到上下文中,然后携带到上下文中,最后由 Prediacte 进行判断,完成整个 ILoadBalancer 的 choose



