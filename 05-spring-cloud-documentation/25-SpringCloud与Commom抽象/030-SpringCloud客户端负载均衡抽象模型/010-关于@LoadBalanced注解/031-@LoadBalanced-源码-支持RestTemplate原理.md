# 031-@LoadBalanced-源码-支持RestTemplate原理

[TOC]

## RestTemplateCustomizer-RestTemplate定制化器

spring-cloud-commons模块中的 META-INF/spring.factory 文件里存在的 LoadBalancerAutoConfiguration 用来自动选择与装载负载均衡相关的功能

#### RestTemplateCustomizer-定制RestTemplate

```java
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RestTemplate.class)
@ConditionalOnBean(LoadBalancerClient.class)
@EnableConfigurationProperties(LoadBalancerRetryProperties.class)
public class LoadBalancerAutoConfiguration {

	@LoadBalanced
	@Autowired(required = false)
	private List<RestTemplate> restTemplates = Collections.emptyList();  //①

	@Bean
	public SmartInitializingSingleton loadBalancedRestTemplateInitializerDeprecated( final ObjectProvider<List<RestTemplateCustomizer>> restTemplateCustomizers) {
		return () -> restTemplateCustomizers.ifAvailable(customizers -> {
			for (RestTemplate restTemplate : LoadBalancerAutoConfiguration.this.restTemplates) {
				for (RestTemplateCustomizer customizer : customizers) {
					customizer.customize(restTemplate);
				}
			}
		});
	}
	//...省略

}

```

- ① 获取所有 ApplicationContext中的 被标注 @LoadBalanced 注解的 RestTemplate
- 获取所有的 RestTemplateCustomizer
- 遍历 RestTemplateCustomizer 列表, 定制

## RestTemplateCustomizer-创建并设置拦截器

org.springframework.cloud.client.loadbalancer.LoadBalancerAutoConfiguration.LoadBalancerInterceptorConfig

```java
	@Configuration(proxyBeanMethods = false)
	@ConditionalOnMissingClass("org.springframework.retry.support.RetryTemplate") //①
	static class LoadBalancerInterceptorConfig {

		@Bean
		public LoadBalancerInterceptor ribbonInterceptor( //②
				LoadBalancerClient loadBalancerClient, LoadBalancerRequestFactory requestFactory) {
			return new LoadBalancerInterceptor(loadBalancerClient, requestFactory);
		}

		@Bean
		@ConditionalOnMissingBean
		public RestTemplateCustomizer restTemplateCustomizer(
				final LoadBalancerInterceptor loadBalancerInterceptor) { ③ 
			return restTemplate -> {List<ClientHttpRequestInterceptor> list = new ArrayList<>(
						restTemplate.getInterceptors());
				list.add(loadBalancerInterceptor);
				restTemplate.setInterceptors(list);
			};
		}

	}
```

- ① 条件注解, 当系统中不存在 RetryTemplate 时才会开启
- ② 声明默认的 LoadBalancerInterceptor . 这个拦截器 继承自 ClientHttpRequestInterceptor , 可以 添加到 RestTemplate 中拦截
- ③ 添加到 自定义 RestTemplateCustomizer, 返回的 restTemplate 会设置上这些拦截器

## RestTemplateCustomizer-retry框架存在时构建

唯一的区别是创建了RetryLoadBalancerInterceptor , 支持重试

```java
@Configuration(proxyBeanMethods = false)
@ConditionalOnClass(RetryTemplate.class)
public static class RetryInterceptorAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public RetryLoadBalancerInterceptor ribbonInterceptor(
    LoadBalancerClient loadBalancerClient,
    LoadBalancerRetryProperties properties,
    LoadBalancerRequestFactory requestFactory,
    LoadBalancedRetryFactory loadBalancedRetryFactory) {
    return new RetryLoadBalancerInterceptor(loadBalancerClient, properties,
                                            requestFactory, loadBalancedRetryFactory);
  }

  @Bean
  @ConditionalOnMissingBean
  public RestTemplateCustomizer restTemplateCustomizer(
    final RetryLoadBalancerInterceptor loadBalancerInterceptor) {
    return restTemplate -> {
      List<ClientHttpRequestInterceptor> list = new ArrayList<>(
        restTemplate.getInterceptors());
      list.add(loadBalancerInterceptor);
      restTemplate.setInterceptors(list);
    };
  }

}
```

