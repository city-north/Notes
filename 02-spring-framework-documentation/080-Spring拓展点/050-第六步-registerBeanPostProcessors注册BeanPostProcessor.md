# 第六步-registerBeanPostProcessors注册BeanPostProcessor

[TOC]

## 图示

![image-20201007151953236](../../assets/image-20201007151953236.png)

## 注册

```java
@Override
	public void refresh() throws BeansException, IllegalStateException {
		synchronized (this.startupShutdownMonitor) {
			...
			try {
				// Register bean processors that intercept bean creation.
				registerBeanPostProcessors(beanFactory);

				...
	}
```



## BeanPostProcessor介绍

- 第五步registerBeanPostProcessors仅仅做注册

- BeanPostProcessor真正的调用其实是在bean的实例化阶段进行的

这是一个很重要的步骤，也是很多功能BeanFactory不支持的重要原因。Spring中大部分功能都是通过后处理器的方式进行扩展的，这是Spring框架的一个特性，**但是在BeanFactory中其实并没有实现后处理器的自动注册，所以在调用的时候如果没有进行手动注册其实是不能使用的**。但是在ApplicationContext中却添加了自动注册功能，如自定义这样一个后处理器：

```java
public class MyInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor{
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    System.out.println("====");
    return null;
 	}
 ... ...
}
```

在配置文件中添加配置：

```xml
<bean class="processors.MyInstantiationAwareBeanPostProcessor"/>
```

那么使用BeanFactory方式进行Spring的bean的加载时是不会有任何改变的，但是使用ApplicationContext方式获取bean的时候会在获取每个bean时打印出“====”，而这个特性就是在 registerBeanPostProcessors 方法中完成

## 参考资料

-  [080-SpringBean实例化后阶段.md](../008-SpringBean生命周期/080-SpringBean实例化后阶段.md) 