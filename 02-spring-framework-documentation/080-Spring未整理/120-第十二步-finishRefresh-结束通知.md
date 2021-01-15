# 120-第十二步-finishRefresh-结束通知

![image-20201007151953236](../../assets/image-20201007151953236.png)

## 目录

[TOC]

## 简介

在Spring中还提供了Lifecycle接口，Lifecycle中包含start/stop方法，实现此接口后Spring会保证在启动的时候调用其start方法开始生命周期，并在Spring关闭的时候调用stop方法来结束生命周期，通常用来配置后台程序，在启动后一直运行（如对MQ进行轮询等）。而ApplicationContext的初始化最后正是保证了这一功能的实现。

```java
	protected void finishRefresh() {
		// Clear context-level resource caches (such as ASM metadata from scanning).
		clearResourceCaches();

		// Initialize lifecycle processor for this context.
		initLifecycleProcessor();

		// Propagate refresh to lifecycle processor first.
		getLifecycleProcessor().onRefresh();

		// Publish the final event.
		publishEvent(new ContextRefreshedEvent(this));

		// Participate in LiveBeansView MBean, if active.
		LiveBeansView.registerApplicationContext(this);
	}
```
## initLifecycleProcessor

当ApplicationContext启动或停止时，它会通过LifecycleProcessor来与所有声明的bean的周期做状态更新，而在LifecycleProcessor的使用前首先需要初始化。

```java
protected void initLifecycleProcessor() {
         ConfigurableListableBeanFactory beanFactory = getBeanFactory();
         if (beanFactory.containsLocalBean(LIFECYCLE_PROCESSOR_BEAN_NAME)) {
             this.lifecycleProcessor =
                     beanFactory.getBean(LIFECYCLE_PROCESSOR_BEAN_NAME, LifecycleProcessor.class);
             if (logger.isDebugEnabled()) {
                 logger.debug("Using LifecycleProcessor [" + this.lifecycleProcessor + "]");
             }
         }else {
             DefaultLifecycleProcessor defaultProcessor = new DefaultLifecycleProcessor();
             defaultProcessor.setBeanFactory(beanFactory);
             this.lifecycleProcessor = defaultProcessor;
             beanFactory.registerSingleton(LIFECYCLE_PROCESSOR_BEAN_NAME, this.lifecycleProcessor);
             if (logger.isDebugEnabled()) {
                 logger.debug("Unable to locate LifecycleProcessor with name '" +
                         LIFECYCLE_PROCESSOR_BEAN_NAME +
                         "': using default [" + this.lifecycleProcessor + "]");
             }
         }
}

```

## onRefresh

启动所有实现了Lifecycle接口的bean。

```java
public void onRefresh() {
         startBeans(true);
         this.running = true;
}

private void startBeans(boolean autoStartupOnly) {
         Map<String, Lifecycle> lifecycleBeans = getLifecycleBeans();
         Map<Integer, LifecycleGroup> phases = new HashMap<Integer, LifecycleGroup>();
         for (Map.Entry<String, ? extends Lifecycle> entry : lifecycleBeans.entrySet()) {
             Lifecycle bean = entry.getValue();
             if (!autoStartupOnly || (bean instanceof SmartLifecycle && ((SmartLifecycle) bean).isAutoStartup())) {
                 int phase = getPhase(bean);
                 LifecycleGroup group = phases.get(phase);
                 if (group == null) {
                     group = new LifecycleGroup(phase, this.timeoutPerShutdownPhase, lifecycleBeans, autoStartupOnly);
                     phases.put(phase, group);
                 }
                 group.add(entry.getKey(), bean);
             }
         }
         if (phases.size() > 0) {
             List<Integer> keys = new ArrayList<Integer>(phases.keySet());
             Collections.sort(keys);
             for (Integer key : keys) {
                 phases.get(key).start();
             }
         }
}

```

## publishEvent

当完成ApplicationContext初始化的时候，要通过Spring中的事件发布机制来发出ContextRefreshedEvent事件，以保证对应的监听器可以做进一步的逻辑处理。

```java
public void publishEvent(ApplicationEvent event) {
         Assert.notNull(event, "Event must not be null");
         if (logger.isTraceEnabled()) {
             logger.trace("Publishing event in " + getDisplayName() + ": " + event);
         }
         getApplicationEventMulticaster().multicastEvent(event);
         if (this.parent != null) {
             this.parent.publishEvent(event);
         }
}
```



