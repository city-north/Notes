# SpringCloud RefreshScope

---
[TOC]

## 简介

SpringCloud @RefreshScope 是如何控制Bean的动态刷新的

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
  public int getOrder() {
    return this.order;
  }

  public void setOrder(int order) {
    this.order = order;
  }

  /**
	 * Flag to determine whether all beans in refresh scope should be instantiated eagerly
	 * on startup. Default true.
	 * @param eager The flag to set.
	 */
  public void setEager(boolean eager) {
    this.eager = eager;
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
      if (this.getName().equals(definition.getScope())
          && !definition.isLazyInit()) {
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

