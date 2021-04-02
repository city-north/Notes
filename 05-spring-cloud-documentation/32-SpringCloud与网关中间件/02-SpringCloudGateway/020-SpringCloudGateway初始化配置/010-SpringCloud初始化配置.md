# 010-SpringCloud初始化配置

[TOC]

在引入Spring Cloud Gateway的依赖后，Starter的jar包将会自动初始化一些类：

- GatewayLoadBalancerClientAutoConfiguration，客户端负载均衡配置类。
- GatewayRedisAutoConfiguration，Redis的自动配置类。
- GatewayDiscoveryClientAutoConfiguration，服务发现自动配置类。
- GatewayClassPathWarningAutoConfiguration，WebFlux依赖检查的配置类。
- GatewayAutoConfiguration，核心配置类，配置路由规则、过滤器等。

主要看一下涉及的网关属性配置定义，很多对象的初始化都依赖于应用服务中配置的网关属性，GatewayProperties是网关中主要的配置属性类，代码如下所示：

```java
@ConfigurationProperties(GatewayProperties.PREFIX)
@Validated
public class GatewayProperties {
	/**
	 * Properties prefix.
	 */
	public static final String PREFIX = "spring.cloud.gateway";
	/**
	 * 路由列表
	 */
	@NotNull
	@Valid
	private List<RouteDefinition> routes = new ArrayList<>();

	/**
	 * List of filter definitions that are applied to every route.
	 */
	private List<FilterDefinition> defaultFilters = new ArrayList<>();

	private List<MediaType> streamingMediaTypes = Arrays.asList(MediaType.TEXT_EVENT_STREAM,
			MediaType.APPLICATION_STREAM_JSON);

	/**
	 * Option to fail on route definition errors, defaults to true. Otherwise, a warning
	 * is logged.
	 */
	private boolean failOnRouteDefinitionError = true;
...

}

```

GatewayProperties中有三个属性，分别是路由、默认过滤器和MediaType的配置，在之前的基础应用的例子中演示了配置的前两个属性。

- routes是一个列表，对应的对象属性是路由定义RouteDefinition；

- defaultFilters是默认的路由过滤器，会应用到每个路由中；
- streamingMediaTypes默认支持两种类型： 
  - APPLICATION_STREAM_JSON
  - TEXT_EVENT_STREAM。

