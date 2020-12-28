# BeanFactory和ApplicatonContext到底哪个是真正的容器

[TOC]

## BeanFactory

- Beanfactory接口提供了一些可以管理某种类型**对象**的机制(注意,这里说的是对象,而没有说是bean, 依赖来源并不只是Bean)

 [010-BeanFactory.md](../010-Spring5系统架构/010-BeanFactory.md) 

## ApplicationContext

ApplicationContext是Spring提供的一个高级的IoC容器，它除了能够提供IoC容器的基本功能，还为用户提供了以下附加服务。

```java
public interface ApplicationContext extends 
EnvironmentCapable,  // 外部化配置
ListableBeanFactory,  
HierarchicalBeanFactory,// 层级结构
MessageSource,  //消息资源
ApplicationEventPublisher, // 事件发布
ResourcePatternResolver  //资源解析
```

- 面向切面编程(AOP)
- 配置元信息(Configuration Metadata)
- 资源管理(Resources)
- 事件(Events)
- 注解(Annotations)
- 支持信息源，可以实现国际化  [020-MessageSource-国际化消息机制](../090-Spring机制/020-MessageSource-国际化消息机制) 
- 访问资源（实现ResourcePatternResolver接口)  [010-Resource机制](../090-Spring机制/010-Resource机制) 
- 支持应用事件（实现ApplicationEventPublisher接口） [0ApplicationEvent-事件机制](../090-Spring机制/030-ApplicationEvent-事件机制) 
- 环境抽象(Envornment) 包括配置和外部化配置

## 总结

总而言之, BeanFactory 提供的是一个配置的框架,并且是一个基本的功能,而ApplicationContext 提供了更多企业级开发的特性

ApplicationContext通过聚合一个BeanFactory的方式.提供了容器的方式

## ConfigurableApplicationContext

它提供了BeanFactory聚合的方法

```java
//关键代码
public interface ConfigurableApplicationContext extends ApplicationContext, Lifecycle, Closeable {

	/**
		返回内部的Bean Factory的实例, 可以用来访问潜在的工厂
	 */
	ConfigurableListableBeanFactory getBeanFactory() throws IllegalStateException;

}
```

## 具体实现 AbstractRefreshableApplicationContext

具体实现是在 AbstractRefreshableApplicationContext , 它是 ApplicationContext的实现类

```java
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext {

   /** Bean factory for this context. */
   @Nullable
   private DefaultListableBeanFactory beanFactory;

  ...
    
    
@Override
	public final ConfigurableListableBeanFactory getBeanFactory() {
		synchronized (this.beanFactoryMonitor) {
			if (this.beanFactory == null) {
				throw new IllegalStateException("BeanFactory not initialized or already closed - " +
						"call 'refresh' before accessing beans via the ApplicationContext");
			}
			return this.beanFactory;
		}
	}
```

