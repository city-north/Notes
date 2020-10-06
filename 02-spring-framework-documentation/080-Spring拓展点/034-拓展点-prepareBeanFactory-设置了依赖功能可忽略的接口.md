# 设置忽略依赖

当Spring将ApplicationContextAwareProcessor注册后, 那么invokeAwareInterfaces方法中间调用的额Aware类已经不是普通的bean了,

比如

- ResourceLoaderAware 等
- ApplicationEventPublisherAware 等

那么需要在Spring做bean的依赖注入的时候忽略他们, 而 ignoreDependencyInterface 的作用正式如此

```java
//设置了几个忽略自动装配的接口
beanFactory.ignoreDependencyInterface(ResourceLoaderAware.class);
beanFactory.ignoreDependencyInterface(ApplicationEventPublisherAware.class);
beanFactory.ignoreDependencyInterface(MessageSourceAware.class);
beanFactory.ignoreDependencyInterface(ApplicationContextAware.class);
beanFactory.ignoreDependencyInterface(EnvironmentAware.class);
```



