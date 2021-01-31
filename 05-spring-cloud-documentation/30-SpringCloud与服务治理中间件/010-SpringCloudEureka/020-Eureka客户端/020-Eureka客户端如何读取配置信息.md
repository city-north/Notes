# 010-Eureka客户端如何读取配置信息

[TOC]

## 一言蔽之

自动化配置类EurekaDiscoveryClientConfiguration 主要做的是将 配置信息 (EurekaClientConfig) 和 客户端 EurekaClient封装成对象适配器EurekaDiscoveryClient 中

## 先入为主的核心类

- 自动化配置 : org.springframework.cloud.netflix.eureka.EurekaDiscoveryClientConfiguration
- Eureaka 客户端发现类 org.springframework.cloud.netflix.eureka.EurekaDiscoveryClient

## 初始化EurekaDiscoveryClient

根据配置类初始化Eureka客户端

```java
@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties
@ConditionalOnClass(EurekaClientConfig.class)
@ConditionalOnProperty(value = "eureka.client.enabled", matchIfMissing = true)
@ConditionalOnDiscoveryEnabled
@ConditionalOnBlockingDiscoveryEnabled
public class EurekaDiscoveryClientConfiguration {


	@Bean
	@ConditionalOnMissingBean
	public EurekaDiscoveryClient discoveryClient(EurekaClient client,EurekaClientConfig clientConfig) {
		return new EurekaDiscoveryClient(client, clientConfig);
	}
```

