# 065-Spring事件监听广播器-ApplicationEventMuticaster

[TOC]

## 一言蔽之

ApplicationEventMuticaster 是发布器, 是Spring底层发布事件的具体实现 , ApplicationEventPublisher 最终委派给ApplicationEventMuticaster实现(默认是SimpleApplicationEventMulticaster)

## 事件发布的方法

- 通过ApplicationEventPublisher发布
  - 依赖注入
- 通过ApplicationEventMulticaster发布
  - 依赖注入
  - 依赖查找

## ApplicationEventMuticaster

```java
public interface ApplicationEventMulticaster {
	
  //增删改查
   void addApplicationListener(ApplicationListener<?> listener);
   void addApplicationListenerBean(String listenerBeanName);
   void removeApplicationListener(ApplicationListener<?> listener);
   void removeApplicationListenerBean(String listenerBeanName);
   void removeAllListeners();
	//发布
   void multicastEvent(ApplicationEvent event);
   void multicastEvent(ApplicationEvent event, @Nullable ResolvableType eventType); 
}
```

## 依赖注入

- 通过 ApplicationEventPublisherAware回调接口
- 通过 @Autowired ApplicationEventPublisher

```java
public class InjectingApplicationEventPublisherDemo implements ApplicationEventPublisherAware,
        ApplicationContextAware, BeanPostProcessor {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        //# 3
        applicationEventPublisher.publishEvent(new MySpringEvent("The event from @Autowired ApplicationEventPublisher"));
        // #4
        applicationContext.publishEvent(new MySpringEvent("The event from @Autowired ApplicationContext"));
    }

    public static void main(String[] args) {

        // 创建注解驱动 Spring 应用上下文
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        // 注册 Configuration Class
        context.register(InjectingApplicationEventPublisherDemo.class);

        // 增加 Spring 事件监听器
        context.addApplicationListener(new MySpringEventListener());

        // 启动 Spring 应用上下文
        context.refresh();

        // 关闭 Spring 应用上下文
        context.close();
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) { // #1
        applicationEventPublisher.publishEvent(new MySpringEvent("The event from ApplicationEventPublisherAware"));
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException { // #2
        applicationEventPublisher.publishEvent(new MySpringEvent("The event from ApplicationContextAware"));
    }
}

```

## 依赖查找

org.springframework.context.support.AbstractApplicationContext#refresh

 [070-初始化内建Bean：Spring事件广播器-initApplicationEventMulticaster().md](../019-Spring应用上下文生命周期/070-初始化内建Bean：Spring事件广播器-initApplicationEventMulticaster().md) 

 [090-Spring事件监听器注册阶段-registerListeners().md](../019-Spring应用上下文生命周期/090-Spring事件监听器注册阶段-registerListeners().md) 

```java
@Override
public void refresh() throws BeansException, IllegalStateException {
   synchronized (this.startupShutdownMonitor) {
      // Prepare this context for refreshing.
      prepareRefresh();

      // Tell the subclass to refresh the internal bean factory.
      ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

      // Prepare the bean factory for use in this context.
      prepareBeanFactory(beanFactory);

      try {
         // Allows post-processing of the bean factory in context subclasses.
         postProcessBeanFactory(beanFactory);

         // Invoke factory processors registered as beans in the context.
         invokeBeanFactoryPostProcessors(beanFactory);

         // Register bean processors that intercept bean creation.
         registerBeanPostProcessors(beanFactory);

         // Initialize message source for this context.
         initMessageSource();

         // Initialize event multicaster for this context.
         // 注册 应用事件广播器
         initApplicationEventMulticaster();

         // Initialize other special beans in specific context subclasses.
         onRefresh();

         // Check for listener beans and register them.
        //注册监听器
         registerListeners();

         // Instantiate all remaining (non-lazy-init) singletons.
         finishBeanFactoryInitialization(beanFactory);

         // Last step: publish corresponding event.
         finishRefresh();
      }
```



## 关于异步

 [120-同步和异步的事件广播.md](120-同步和异步的事件广播.md) 

## 关于错误处理

 [130-Spring事件异常处理.md](130-Spring事件异常处理.md) 