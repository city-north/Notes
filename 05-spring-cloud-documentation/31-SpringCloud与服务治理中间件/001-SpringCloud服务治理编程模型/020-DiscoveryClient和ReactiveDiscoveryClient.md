# 020-DiscoveryClient和ReactiveDiscoveryClient

[TOC]

## DiscoveryClient简介

DiscoveryClient和 ReactiveDiscoveryClient 代表 Consumer 从注册中心发现Provider的服务发现操作, 接口定义如下

```java
/**
 * Represents read operations commonly available to discovery services such as Netflix
 * Eureka or consul.io.
 */
public interface DiscoveryClient extends Ordered {

	/**
	 * 默认的优先级,多个 DiscoveryClient 存在的情况下以优先级排序
	 */
	int DEFAULT_ORDER = 0;

	/**
	 * A human-readable description of the implementation, used in HealthIndicator.
	  具体服务发现组件的描述信息, 在 HealthIndicator中会被用到
	 */
	String description();

	/**
	 * Gets all ServiceInstances associated with a particular serviceId.
   	根据服务名查询所有的服务实例
   * @param serviceId The serviceId to query. 服务名
	 * @return A List of ServiceInstance. 服务实例集合
	 */
	List<ServiceInstance> getInstances(String serviceId);

	/**
		返回注册中心所有的服务名
	 * @return All known service IDs.
	 */
	List<String> getServices();

	/**
		具体的服务发现组件的优先级, 默认为0
	 * Default implementation for getting order of discovery clients.
	 * @return order
	 */
	@Override
	default int getOrder() {
		return DEFAULT_ORDER;
	}
}
```

## DiscoveryClient不同实现

| 组件名                               | 实现类                                                       |                    |
| ------------------------------------ | ------------------------------------------------------------ | ------------------ |
| spring-cloud-commons                 | org.springframework.cloud.client.discovery.DiscoveryClient   |                    |
|                                      | org.springframework.cloud.client.discovery.composite.CompositeDiscoveryClient | 组合模式           |
|                                      | org.springframework.cloud.client.discovery.simple.SimpleDiscoveryClient | 使用properties存储 |
| spring-cloud-netflix-eureka-client   | org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient |                    |
| spring-cloud-alibaba-nacos-discovery | com.alibaba.cloud.nacos.discovery.NacosDiscoveryClient       |                    |

## ReactiveDiscoveryClient简介

```java
/**
 * Represents read operations commonly available to discovery services such as Netflix
 * Eureka or consul.io.
 */
public interface ReactiveDiscoveryClient extends Ordered {

	int DEFAULT_ORDER = 0;

	String description();

	Flux<ServiceInstance> getInstances(String serviceId);

	Flux<String> getServices();

	@Override
	default int getOrder() {
		return DEFAULT_ORDER;
	}

}
```

ReactiveDiscoveryClient接口中的方法与 DiscoveryClient几乎相同,只是吧List类转哈云成了 reactor类的 Flux

在SpringWebFlux场景下, ReactiveDiscoveryClient会生效, 