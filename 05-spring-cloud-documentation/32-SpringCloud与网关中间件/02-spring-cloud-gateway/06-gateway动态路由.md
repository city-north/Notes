# Gateway动态路由

网关中有两个重要的概念,那就是路由配置和路由规则

- 路由规则是指配置某请求路径路由到指定的目的地址
- 路由规则是指匹配到路由配置之后,再根据路由规则进行转发

## 为什么要实现动态路由

因为在生产中,gateway 违所有请求流量的入口,在实际生产环境中为了保证高可用和高可靠,避免重启,所以需要实现动态路由

一般两种形式,

- 单机动态路由
- 集群下动态路由



## 集群下的动态路由实现

默认的 `RouteDefinitionWriter` 实现是`InMemoryRouteDefinationRepository `(默认是在内存里维护了路由信息)

有两种方式可以实现集群下的动态路由修改:

实现 `RouteDefinitionWriter` 接口和 `RouteDefinitionRepository` 接口

从`RouteDefinitionRepository`通过数据库或者从配置中心获取路由进行动态路由



## 单机

分别创建 

- GatewayRouteDefinition.java  网关路由模型
- GatewayPredicateDefinition.java 网关断言模型
- GatewayFilterDefintion.java 网关过滤器模型



```java
/**
 * Gateway的路由定义模型
 */
public class GatewayRouteDefinition {

    /**
     * 路由的Id
     */
    private String id;

    /**
     * 路由断言集合配置
     */
    private List<GatewayPredicateDefinition> predicates = new ArrayList<>();

    /**
     * 路由过滤器集合配置
     */
    private List<GatewayFilterDefinition> filters = new ArrayList<>();

    /**
     * 路由规则转发的目标uri
     */
    private String uri;

    /**
     * 路由执行的顺序
     */
    private int order = 0;

    public List<GatewayPredicateDefinition> getPredicates() {
        return predicates;
    }

    public void setPredicates(List<GatewayPredicateDefinition> predicates) {
        this.predicates = predicates;
    }


}
```



```java
/**
 * 路由断言定义模型
 */
public class GatewayPredicateDefinition {

    /**
     * 断言对应的Name
     */
    private String name;

    /**
     * 配置的断言规则
     */
    private Map<String, String> args = new LinkedHashMap<>();



}
```

```java
/**
 * 过滤器定义模型
 */
public class GatewayFilterDefinition {

    /**
     * Filter Name
     */
    private String name;

    /**
     * 对应的路由规则
     */
    private Map<String, String> args = new LinkedHashMap<>();

}

```

使用默认的`RouteDefinitionWriter`实现类实现内存的路由更改



```java
@Service
public class DynamicRouteServiceImpl implements ApplicationEventPublisherAware {

    @Autowired
    private RouteDefinitionWriter routeDefinitionWriter;

    private ApplicationEventPublisher publisher;


    /**
     * 增加路由
     * @param definition
     * @return
     */
    public String add(RouteDefinition definition) {
        routeDefinitionWriter.save(Mono.just(definition)).subscribe();
        this.publisher.publishEvent(new RefreshRoutesEvent(this));
        return "success";
    }


    /**
     * 更新路由
     * @param definition
     * @return
     */
    public String update(RouteDefinition definition) {
        try {
            this.routeDefinitionWriter.delete(Mono.just(definition.getId()));
        } catch (Exception e) {
            return "update fail,not find route  routeId: "+definition.getId();
        }
        try {
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            this.publisher.publishEvent(new RefreshRoutesEvent(this));
            return "success";
        } catch (Exception e) {
            return "update route  fail";
        }


    }
    /**
     * 删除路由
     * @param id
     * @return
     */
    public String delete(String id) {
        try {
            this.routeDefinitionWriter.delete(Mono.just(id));
            return "delete success";
        } catch (Exception e) {
            e.printStackTrace();
            return "delete fail";
        }

    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.publisher = applicationEventPublisher;
    }

    /**
     * spring:
     cloud:
     gateway:
     routes: #当访问http://localhost:8080/baidu,直接转发到https://www.baidu.com/
     - id: baidu_route
     uri: http://baidu.com:80/
     predicates:
     - Path=/baidu
     * @param args
     */

    public static void main(String[] args) {
        GatewayRouteDefinition definition = new GatewayRouteDefinition();
        GatewayPredicateDefinition predicate = new GatewayPredicateDefinition();
        Map<String, String> predicateParams = new HashMap<>(8);
        definition.setId("jd_route");
        predicate.setName("Path");
        predicateParams.put("pattern", "/jd");
        predicate.setArgs(predicateParams);
        definition.setPredicates(Arrays.asList(predicate));
        String uri="http://www.jd.com";
        //URI uri = UriComponentsBuilder.fromHttpUrl("http://www.baidu.com").build().toUri();
        definition.setUri(uri);
        System.out.println("definition:"+JSON.toJSONString(definition));


        RouteDefinition definition1 = new RouteDefinition();
        PredicateDefinition predicate1 = new PredicateDefinition();
        Map<String, String> predicateParams1 = new HashMap<>(8);
        definition1.setId("baidu_route");
        predicate1.setName("Path");
        predicateParams1.put("pattern", "/baidu");
        predicate1.setArgs(predicateParams1);
        definition1.setPredicates(Arrays.asList(predicate1));
        URI uri1 = UriComponentsBuilder.fromHttpUrl("http://www.baidu.com").build().toUri();
        definition1.setUri(uri1);
        System.out.println("definition1："+JSON.toJSONString(definition1));

    }


}
```