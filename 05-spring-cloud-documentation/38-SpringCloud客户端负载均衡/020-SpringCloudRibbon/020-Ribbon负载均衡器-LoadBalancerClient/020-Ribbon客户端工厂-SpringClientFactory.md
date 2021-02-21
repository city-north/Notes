# 020-Ribbon客户端工厂-SpringClientFactory

[TOC]

## 一言蔽之

SpringClientFactory 是一个与LoadBalancerClientFactory 作用类似的工厂类, 器内部维护这一个Map, 这个Map用于存放各个服务的

- Key-> 各个服务的服务名

- Value -> 各个服务独立的ApplicationContext ,维护对应服务的一些配置和Bean

 [013-SpringCloud运行上下文-NamedContextFactory.md](../../../36-SpringCloud声明式客户端/010-SpringCloudOpenFeign/020-动态注册BeanDefinition/013-SpringCloud运行上下文-NamedContextFactory.md) 



## SpringClientFactory源码



```java
public class SpringClientFactory extends NamedContextFactory<RibbonClientSpecification> {

	static final String NAMESPACE = "ribbon";

	public SpringClientFactory() {
		super(RibbonClientConfiguration.class, NAMESPACE, "ribbon.client.name");
	}

	/**
	 * Get the rest client associated with the name.
	 * @throws RuntimeException if any error occurs
	 */
	public <C extends IClient<?, ?>> C getClient(String name, Class<C> clientClass) {
		return getInstance(name, clientClass);
	}

	/**
	 * Get the load balancer associated with the name.
	 * @throws RuntimeException if any error occurs
	 */
	public ILoadBalancer getLoadBalancer(String name) {
		return getInstance(name, ILoadBalancer.class);
	}

	/**
	 * Get the client config associated with the name.
	 * @throws RuntimeException if any error occurs
	 */
	public IClientConfig getClientConfig(String name) {
		return getInstance(name, IClientConfig.class);
	}

	/**
	 * Get the load balancer context associated with the name.
	 * @throws RuntimeException if any error occurs
	 */
	public RibbonLoadBalancerContext getLoadBalancerContext(String serviceId) {
		return getInstance(serviceId, RibbonLoadBalancerContext.class);
	}

	static <C> C instantiateWithConfig(Class<C> clazz, IClientConfig config) {
		return instantiateWithConfig(null, clazz, config);
	}

	static <C> C instantiateWithConfig(AnnotationConfigApplicationContext context,
										Class<C> clazz, IClientConfig config) {
		C result = null;
		
		try {
			Constructor<C> constructor = clazz.getConstructor(IClientConfig.class);
			result = constructor.newInstance(config);
		} catch (Throwable e) {
			// Ignored
		}
		
		if (result == null) {
			result = BeanUtils.instantiate(clazz);
			
			if (result instanceof IClientConfigAware) {
				((IClientConfigAware) result).initWithNiwsConfig(config);
			}
			
			if (context != null) {
				context.getAutowireCapableBeanFactory().autowireBean(result);
			}
		}
		
		return result;
	}

	@Override
	public <C> C getInstance(String name, Class<C> type) {
		C instance = super.getInstance(name, type);
		if (instance != null) {
			return instance;
		}
		IClientConfig config = getInstance(name, IClientConfig.class);
		return instantiateWithConfig(getContext(name), type, config);
	}

	@Override
	protected AnnotationConfigApplicationContext getContext(String name) {
		return super.getContext(name);
	}

}
```

