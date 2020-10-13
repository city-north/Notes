# 080-第八步-initApplicationEventMulticaster-初始化容器事件传播器

![image-20201007151953236](../../assets/image-20201007151953236.png)

## 目录

- [Spring中的事件机制](#Spring中的事件机制)
- 

## Spring中的事件机制

 [README.md](../090-Spring机制/030-ApplicationEvent-事件机制/README.md) 

 [010-事件机制的简单用法.md](../090-Spring机制/030-ApplicationEvent-事件机制/010-事件机制的简单用法.md) 

## 源码分析

nitApplicationEventMulticaster的方式比较简单，无非考虑两种情况。

- 如果用户自定义了事件广播器，那么使用用户自定义的事件广播器。
- 如果用户没有自定义事件广播器，那么使用默认的ApplicationEventMulticaster。

```java
protected void initApplicationEventMulticaster() {
         ConfigurableListableBeanFactory beanFactory = getBeanFactory();
         if (beanFactory.containsLocalBean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME)) {
             this.applicationEventMulticaster =
                     beanFactory.getBean(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, ApplicationEventMulticaster.class);
             if (logger.isDebugEnabled()) {
                 logger.debug("Using ApplicationEventMulticaster [" + this.application EventMulticaster + "]");
             }
         }else {
             this.applicationEventMulticaster = new SimpleApplicationEventMulticaster (beanFactory);
             beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, this.applicationEventMulticaster);
             if (logger.isDebugEnabled()) {
                 logger.debug("Unable to locate ApplicationEventMulticaster with name '" +
                         APPLICATION_EVENT_MULTICASTER_BEAN_NAME +
                         "': using default [" + this.applicationEventMulticaster + "]");
             }
         }
}
```

按照之前介绍的顺序及逻辑，我们推断，作为广播器，一定是用于存放监听器并在合适的时候调用监听器，那么我们不妨进入默认的广播器实现SimpleApplicationEventMulticaster来一探究竟。

其中的一段代码是我们感兴趣的。

```java
public void multicastEvent(final ApplicationEvent event) {
         for (final ApplicationListener listener : getApplicationListeners(event)) {
             Executor executor = getTaskExecutor();
             if (executor != null) {
               executor.execute(new Runnable() {
                     @SuppressWarnings("unchecked")
                     public void run() {
                         listener.onApplicationEvent(event);
                     }
                 });
             }
             else {
                 listener.onApplicationEvent(event);
             }
         }
}
```

可以推断，当产生Spring事件的时候会默认使用SimpleApplicationEventMulticaster的multicastEvent来广播事件，遍历所有监听器，并使用监听器中的onApplicationEvent方法来进行监听器的处理。而对于每个监听器来说其实都可以获取到产生的事件，但是是否进行处理则由事件监听器来决定。



