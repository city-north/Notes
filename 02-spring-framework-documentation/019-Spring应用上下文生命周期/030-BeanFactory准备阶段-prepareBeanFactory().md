# 030-BeanFactory准备阶段

[TOC]

## 030-BeanFactory准备阶段做了什么?

- 关联ClassLoader

Bean在注册的时候,没有Class对象,只是知道类的全路径,通过ClassLoader进行加载

- 设置Bean表达式处理器(SpringEL)
- 注册PropertyEditorRegistrar实现-ResourceEditorRegistrar
- 注册Aware回调接口BeanPostProcessor实现-ApplicationContextAwareProcessor
- 忽略Aware回调接口作为依赖注入接口(也就是说Aware接口不支持依赖注入)
- 注册ResolvableDependenct对象
  - BeanFactory
  - ResourceLoader
  - ApplicationEventPublisher
  - ApplicationContext
- 注册ApplicationContextDetector对象
- 注册LoadTimeWeaverAwareProcessor对象(AOP对象)
- 注册单例对象
  - Environment
  - Java System Properties
  - OS环境变量

## 分析源码

```java
//3、为BeanFactory配置容器特性，例如类加载器、事件处理器等
prepareBeanFactory(beanFactory);
```

spring在进入函数 prepareBeanFactory 之前, 就已经完成了对配置的解析, 而 ApplicationContext在功能上的拓展点也由此展开

```java
	/**
	 * 配置工厂的标准特性,例如上下文的加载器,以及后置处理器
	 */
	protected void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    //设置beanFactory的classloader 为当前的classLoader
		beanFactory.setBeanClassLoader(getClassLoader());
    //设置beanFactory的表达式语言处理器,Spring3增加了表达式语言的支持
    //默认可以使用#{bean.xxx}的形式来调用相关的属性
		beanFactory.setBeanExpressionResolver(new StandardBeanExpressionResolver(beanFactory.getBeanClassLoader()));
    //为beanfactory设置一个PropertyEditor, 这个主要是对bean的属性等设置管理的一个工具
		beanFactory.addPropertyEditorRegistrar(new ResourceEditorRegistrar(this, getEnvironment()));

		// 添加bean的 后置处理器
		beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
    // 设置几个忽略自动装配的接口
		beanFactory.ignoreDependencyInterface(EnvironmentAware.class);
		beanFactory.ignoreDependencyInterface(EmbeddedValueResolverAware.class);
		beanFactory.ignoreDependencyInterface(ResourceLoaderAware.class);
		beanFactory.ignoreDependencyInterface(ApplicationEventPublisherAware.class);
		beanFactory.ignoreDependencyInterface(MessageSourceAware.class);
		beanFactory.ignoreDependencyInterface(ApplicationContextAware.class);

    //自动绑定的时候的游离对象
    //游离对象,只能依赖注入,不能依赖注入
		beanFactory.registerResolvableDependency(BeanFactory.class, beanFactory);
		beanFactory.registerResolvableDependency(ResourceLoader.class, this);
		beanFactory.registerResolvableDependency(ApplicationEventPublisher.class, this);
		beanFactory.registerResolvableDependency(ApplicationContext.class, this);


    //注册Application的探测器
		beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(this));

		// 增加对AspectJ的支持
		if (beanFactory.containsBean(LOAD_TIME_WEAVER_BEAN_NAME)) {
			beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
			// loadTimeWeaver
			beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
		}

		//添加默认的系统bean
		if (!beanFactory.containsLocalBean(ENVIRONMENT_BEAN_NAME)) {
      //environment
			beanFactory.registerSingleton(ENVIRONMENT_BEAN_NAME, getEnvironment());
		}
		if (!beanFactory.containsLocalBean(SYSTEM_PROPERTIES_BEAN_NAME)) {
      //systemProperties
			beanFactory.registerSingleton(SYSTEM_PROPERTIES_BEAN_NAME, getEnvironment().getSystemProperties());
		}
		if (!beanFactory.containsLocalBean(SYSTEM_ENVIRONMENT_BEAN_NAME)) {
      //systemEnvironment
			beanFactory.registerSingleton(SYSTEM_ENVIRONMENT_BEAN_NAME, getEnvironment().getSystemEnvironment());
		}
	}

```

主要做了

- [增加对SpEL语言的支持](#增加对SpEL语言的支持)
- [增加对属性编辑器的支持](#增加对属性编辑器的支持)
- [增加对一些内置类，比如EnvironmentAware、MessageSourceAware的信息注入](#增加对一些内置类，比如EnvironmentAware、MessageSourceAware的信息注入)
- [设置了依赖功能可忽略的接口](#设置了依赖功能可忽略的接口)
- 注册一些固定依赖的属性。
- 增加AspectJ的支持
- 将相关环境变量及属性注册以单例模式注册。

### 增加对SpEL语言的支持

 [032-BeanFactory准备阶段-增加对SpEL语言的支持.md](032-BeanFactory准备阶段-增加对SpEL语言的支持.md) 

### 增加对属性编辑器的支持

 [031-BeanFactory准备阶段-增加对属性编辑器的支持.md](031-BeanFactory准备阶段-增加对属性编辑器的支持.md) 

### 增加对一些内置类，比如EnvironmentAware、MessageSourceAware的信息注入

 [033-拓展点-prepareBeanFactory-增加一些内置类.md](033-拓展点-prepareBeanFactory-增加一些内置类.md) 

### 设置了依赖功能可忽略的接口

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

## 图示

![image-20201007151953236](../../assets/image-20201007151953236.png)

