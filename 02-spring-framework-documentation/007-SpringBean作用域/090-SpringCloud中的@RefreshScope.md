# 090-SpringCloud中的@RefreshScope

[TOC]

## 简介

我们都知道 Spring Bean里有4种scope, 

| scope值        | 作用                                                     |
| -------------- | -------------------------------------------------------- |
| singleton      | 全局只有一个实例, 每次获取的相同类型的Bean都是同一个实例 |
| prototype      | 每次获取Bean都创建一个新的实例                           |
| request        | 同一个Request于会返回之前保留的Bean, 否则会重新创建Bean  |
| global Session | , 否则会重新创建Bean同一个 Session返回之前保留的Bean     |
| refresh        | SpringCloud新增                                          |

SpringCloud @RefreshScope

- 新增了一种scope: refresh

 是如何控制Bean的动态刷新的

```java
package org.springframework.cloud.context.config.annotation;


@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
@Scope("refresh")
@Documented
public @interface RefreshScope {

	/**
	 * @see Scope#proxyMode()
	 * @return proxy mode
	 */
	ScopedProxyMode proxyMode() default ScopedProxyMode.TARGET_CLASS;

}
```

## 注册时机

```java
//org.springframework.cloud.autoconfigure.RefreshAutoConfiguration	
@Bean
@ConditionalOnMissingBean(RefreshScope.class)
public static RefreshScope refreshScope() {
  return new RefreshScope();
}
```

## RefreshScope实现

继承了GenericScope,  name设置为refresh

```java
@ManagedResource
public class RefreshScope extends GenericScope implements ApplicationContextAware,
ApplicationListener<ContextRefreshedEvent>, Ordered {

  private ApplicationContext context;

  private BeanDefinitionRegistry registry;

  private boolean eager = true;

  private int order = Ordered.LOWEST_PRECEDENCE - 100;

  /**
	 * Creates a scope instance and gives it the default name: "refresh".
	 */
  public RefreshScope() {
    super.setName("refresh");
  }

  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
    throws BeansException {
    this.registry = registry;
    super.postProcessBeanDefinitionRegistry(registry);
  }

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    start(event);
  }

  //上下文启动完成之后,会发送的事件
  public void start(ContextRefreshedEvent event) {
    if (event.getApplicationContext() == this.context && this.eager
        && this.registry != null) {
      eagerlyInitialize();
    }
  }

  //饥饿初始化,判断过滤整个BeanDefinition里哪些是Refreh的
  private void eagerlyInitialize() {
    for (String name : this.context.getBeanDefinitionNames()) {
      BeanDefinition definition = this.registry.getBeanDefinition(name);
      if (this.getName().equals(definition.getScope())&& !definition.isLazyInit()) {
        Object bean = this.context.getBean(name);
        if (bean != null) {
          bean.getClass();
        }
      }
    }
  }

  // 发送刷新事件
  @ManagedOperation(description = "Dispose of the current instance of bean name "
                    + "provided and force a refresh on next method execution.")
  public boolean refresh(String name) {
    if (!name.startsWith(SCOPED_TARGET_PREFIX)) {
      // User wants to refresh the bean with this name but that isn't the one in the
      // cache...
      name = SCOPED_TARGET_PREFIX + name;
    }
    // Ensure lifecycle is finished if bean was disposable
    if (super.destroy(name)) {
      this.context.publishEvent(new RefreshScopeRefreshedEvent(name));
      return true;
    }
    return false;
  }

  @ManagedOperation(description = "Dispose of the current instance of all beans "
                    + "in this scope and force a refresh on next method execution.")
  public void refreshAll() {
    super.destroy();
    this.context.publishEvent(new RefreshScopeRefreshedEvent());
  }

  @Override
  public void setApplicationContext(ApplicationContext context) throws BeansException {
    this.context = context;
  }

}

```

## 刷新

RefreshEventListener监听器, 监听ApplicationReadyEvent事件, 调用 ContextRefresher 进行刷新

```java
public class RefreshEventListener implements SmartApplicationListener {

	private static Log log = LogFactory.getLog(RefreshEventListener.class);

	private ContextRefresher refresh;

	private AtomicBoolean ready = new AtomicBoolean(false);

	public RefreshEventListener(ContextRefresher refresh) {
		this.refresh = refresh;
	}

	@Override
	public boolean supportsEventType(Class<? extends ApplicationEvent> eventType) {
		return ApplicationReadyEvent.class.isAssignableFrom(eventType)
				|| RefreshEvent.class.isAssignableFrom(eventType);
	}

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ApplicationReadyEvent) {
			handle((ApplicationReadyEvent) event);
		}
		else if (event instanceof RefreshEvent) {
			handle((RefreshEvent) event);
		}
	}

	public void handle(ApplicationReadyEvent event) {
		this.ready.compareAndSet(false, true);
	}

	public void handle(RefreshEvent event) {
		if (this.ready.get()) { // don't handle events before app is ready
			log.debug("Event received " + event.getEventDesc());
			Set<String> keys = this.refresh.refresh();
			log.info("Refresh keys changed: " + keys);
		}
	}

}
```

调用refreshAll方法

```java
public class ContextRefresher {

	...
	
	public synchronized Set<String> refresh() {
		Set<String> keys = refreshEnvironment();
		this.scope.refreshAll();
		return keys;
	}
  
  //refreshEnvironment返回值是一个String集合key, keys内部存储着动态刷新过的配置项的key
public synchronized Set<String> refreshEnvironment() {
  //动态刷新配置之前, 获取旧的propertySources列表里的值存储起来
		Map<String, Object> before = extract(this.context.getEnvironment().getPropertySources());
  
		updateEnvironment();
		Set<String> keys = changes(before, extract(this.context.getEnvironment().getPropertySources())).keySet();
  //发布 EnvironmentChangeEvent 事件
		this.context.publishEvent(new EnvironmentChangeEvent(this.context, keys));
		return keys;
	}
```

refreshEnvironment返回值是一个String集合key, keys内部存储着动态刷新过的配置项的key

```java
	@Override
	protected void updateEnvironment() {
		if (logger.isTraceEnabled()) {
			logger.trace("Re-processing environment to add config data");
		}
    //拷贝
		StandardEnvironment environment = copyEnvironment(getContext().getEnvironment());
		String[] activeProfiles = getContext().getEnvironment().getActiveProfiles();
		DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
		ConfigDataEnvironmentPostProcessor.applyTo(environment, resourceLoader, new DefaultBootstrapContext(),
				activeProfiles);

		if (environment.getPropertySources().contains(REFRESH_ARGS_PROPERTY_SOURCE)) {
			environment.getPropertySources().remove(REFRESH_ARGS_PROPERTY_SOURCE);
		}
		MutablePropertySources target = getContext().getEnvironment().getPropertySources();
		String targetName = null;
		for (PropertySource<?> source : environment.getPropertySources()) {
			String name = source.getName();
			if (target.contains(name)) {
				targetName = name;
			}
			if (!this.standardSources.contains(name)) {
				if (target.contains(name)) {
					target.replace(name, source);
				}
				else {
					if (targetName != null) {
						target.addAfter(targetName, source);
						// update targetName to preserve ordering
						targetName = name;
					}
					else {
						// targetName was null so we are at the start of the list
						target.addFirst(source);
						targetName = name;
					}
				}
			}
		}
	}
```



## 总结

每次RefreshEvent发送完毕之后, 都会触发RefreshScope的refreshAll方法, 该方法内部会在Spring上下文里销毁所有的Scope为refresh的Bean(@RefrehScope注解修饰), 销毁之后, 下次获取这些Bean的时候就会重新构造一遍(意味着会重新解析表达式, 也代表着会去获取最新的配置) , 最后发送RefreshScopeRefreshedEvent, 证明已经刷新
