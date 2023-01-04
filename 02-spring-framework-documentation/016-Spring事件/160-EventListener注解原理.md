# 160-EventListener注解原理

[TOC]

## 原理简介

Spring 使用 EventListenerMethodProcessor 在 BeanFactory 初始化完成之后,获取到所有的BeanDefinition之后,获取到标记的@EventListener的方法封装成一个

ApplicationListenerMethodAdapter,然后到了afterSingletonsInstantiated 后创建监听器加入到ApplicationContextMuticaster里 

## 核心类

- org.springframework.context.event.EventListenerMethodProcessor

![image-20210112215003657](../../assets/image-20210112215003657.png)

## 源码分析

EventListenerMethodProcessor 实现了 BeanFactoryPostProcessor ,在 BeanFactory 初始化完成之后,获取到所有的EventListenerFactory类型的工厂

```java
	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
		this.beanFactory = beanFactory;
		Map<String, EventListenerFactory> beans = beanFactory.getBeansOfType(EventListenerFactory.class, false, false);
		List<EventListenerFactory> factories = new ArrayList<>(beans.values());
		AnnotationAwareOrderComparator.sort(factories);
		this.eventListenerFactories = factories;
	}
```

通过Factory获取到 ApplicationListenerMethodAdapter

```java
public class DefaultEventListenerFactory implements EventListenerFactory, Ordered {

	private int order = LOWEST_PRECEDENCE;


	public void setOrder(int order) {
		this.order = order;
	}

	@Override
	public int getOrder() {
		return this.order;
	}


	@Override
	public boolean supportsMethod(Method method) {
		return true;
	}

	@Override
	public ApplicationListener<?> createApplicationListener(String beanName, Class<?> type, Method method) {
		return new ApplicationListenerMethodAdapter(beanName, type, method);
	}

}
```

根据Bean名称,方法名称 , 获取到一个适配ApplicationListenerMethodAdapter , 存储到

```
    @Nullable
    private ConfigurableListableBeanFactory beanFactory;
```



#### afterSingletonsInstantiated

当bean

```java
	@Override
	public void afterSingletonsInstantiated() {
		List<EventListenerFactory> factories = getEventListenerFactories();
		ConfigurableApplicationContext context = getApplicationContext();
		String[] beanNames = context.getBeanNamesForType(Object.class);
		for (String beanName : beanNames) {
			if (!ScopedProxyUtils.isScopedTarget(beanName)) {
				Class<?> type = null;
				try {
					type = AutoProxyUtils.determineTargetClass(context.getBeanFactory(), beanName);
				}
				catch (Throwable ex) {
					// An unresolvable bean type, probably from a lazy bean - let's ignore it.
					if (logger.isDebugEnabled()) {
						logger.debug("Could not resolve target class for bean with name '" + beanName + "'", ex);
					}
				}
				if (type != null) {
					if (ScopedObject.class.isAssignableFrom(type)) {
						try {
							Class<?> targetClass = AutoProxyUtils.determineTargetClass(
									context.getBeanFactory(), ScopedProxyUtils.getTargetBeanName(beanName));
							if (targetClass != null) {
								type = targetClass;
							}
						}
						catch (Throwable ex) {
							// An invalid scoped proxy arrangement - let's ignore it.
							if (logger.isDebugEnabled()) {
								logger.debug("Could not resolve target bean for scoped proxy '" + beanName + "'", ex);
							}
						}
					}
					try {
						processBean(factories, beanName, type);
					}
					catch (Throwable ex) {
						throw new BeanInitializationException("Failed to process @EventListener " +
								"annotation on bean with name '" + beanName + "'", ex);
					}
				}
			}
		}
	}

```

```java

	protected void processBean(
			final List<EventListenerFactory> factories, final String beanName, final Class<?> targetType) {

		if (!this.nonAnnotatedClasses.contains(targetType)) {
			Map<Method, EventListener> annotatedMethods = null;
			try {
				annotatedMethods = MethodIntrospector.selectMethods(targetType,
						(MethodIntrospector.MetadataLookup<EventListener>) method ->
								AnnotatedElementUtils.findMergedAnnotation(method, EventListener.class));
			}
			catch (Throwable ex) {
				// An unresolvable type in a method signature, probably from a lazy bean - let's ignore it.
				if (logger.isDebugEnabled()) {
					logger.debug("Could not resolve methods for bean with name '" + beanName + "'", ex);
				}
			}
			if (CollectionUtils.isEmpty(annotatedMethods)) {
				this.nonAnnotatedClasses.add(targetType);
				if (logger.isTraceEnabled()) {
					logger.trace("No @EventListener annotations found on bean class: " + targetType.getName());
				}
			}
			else {
				// Non-empty set of methods
				ConfigurableApplicationContext context = getApplicationContext();
				for (Method method : annotatedMethods.keySet()) {
					for (EventListenerFactory factory : factories) {
						if (factory.supportsMethod(method)) {
							Method methodToUse = AopUtils.selectInvocableMethod(method, context.getType(beanName));
              //创建监听器
							ApplicationListener<?> applicationListener =
									factory.createApplicationListener(beanName, targetType, methodToUse);
							if (applicationListener instanceof ApplicationListenerMethodAdapter) {
								((ApplicationListenerMethodAdapter) applicationListener).init(context, this.evaluator);
							}
              //添加到监听器
							context.addApplicationListener(applicationListener);
							break;
						}
					}
				}
				if (logger.isDebugEnabled()) {
					logger.debug(annotatedMethods.size() + " @EventListener methods processed on bean '" +
							beanName + "': " + annotatedMethods);
				}
			}
		}
	}
```

