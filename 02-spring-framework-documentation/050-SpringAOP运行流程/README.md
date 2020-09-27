# SpringAOP运行流程

## 目录

- 简介
- [BeanPostProcessor源码](#BeanPostProcessor源码)

Spring的AOP是通过接入**BeanPostProcessor** 后置处理器开始的,它是Spring IOC容器经常使用到的一个特性, 这个Bean后置处理器是一个监听器, 可以监听容器触发的Bean生命周期事件

后置处理器向容器注册之后,容器中的Bean就具备了接收IOC容器事件的回调能力

## BeanPostProcessor源码

```java
public interface BeanPostProcessor {

	//为在Bean的初始化前提供回调入口
	@Nullable
	default Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}
	
	//为在Bean的初始化之后提供回调入口
	@Nullable
	default Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

}
```

## 对容器中的Bean添加后置处理器

AbstractAutowireCapabIeBeanFactory 中的 doCreateBean 封装了相关的方法

BeanPostProcessor后置处理器的调用发生在Spring IoC容器完成Bean实例对象的创建和属性的依赖注入之后，在对Spring依赖注入的源码分析中我们知道，当应用程序第一次调用getBean()方法（lazy-init预实例化除外）向Spring IoC容器索取指定Bean时，触发Spring IoC容器创建Bean实例对象并进行依赖注入。

其实真正实现创建 Bean 对象并进行依赖注入的方法是AbstractAutowireCapableBeanFactory类的doCreateBean()方法，主要源码如下：

