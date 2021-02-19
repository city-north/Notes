# 140-SpringBean初始化完成阶段.md

## 目录

------

[TOC]

## 一言蔽之

初始化完成阶段主要是调用: SmartInitializingSingleton#afterSingletonsInstantiated`的回调,这个回调确保了回调时,是一个完整初始化的Bean

## DEMO

```java
public class UserHolder implements SmartInitializingSingleton {
    private final User user;
    /**
     * 执行回调
     */
    @Override
    public void afterSingletonsInstantiated() {
        // 调用这个方法时,确保bean已经是一个完全初始化的bean
        this.description = "The user holder V8";
        System.out.println("afterSingletonsInstantiated() = " + description);
    }
}
```

## SmartInitializingSingleton注册阶段

没有注册阶段,硬编码方式固话了调用逻辑

## SmartInitializingSingleton执行阶段

### 代码入口

Spring4.1+ : SmartInitializingSingleton#afterSingletonsInstantiated

### 手动API代码调用时机

在我们自己编程测试时,我们需要手动去触发这个时机

```java
public static void main(String[] args) {
  DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
  // 添加 BeanPostProcessor 实现 MyInstantiationAwareBeanPostProcessor
  beanFactory.addBeanPostProcessor(new MyInstantiationAwareBeanPostProcessor());
  // 添加 CommonAnnotationBeanPostProcessor 解决 @PostConstruct
  beanFactory.addBeanPostProcessor(new CommonAnnotationBeanPostProcessor());
  XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
  String[] locations = {"META-INF/dependency-lookup-context.xml"};
  int beanNumbers = beanDefinitionReader.loadBeanDefinitions(locations);
  // 显示地执行 preInstantiateSingletons()
  // SmartInitializingSingleton 通常在 Spring ApplicationContext 场景使用
  // preInstantiateSingletons 将已注册的 BeanDefinition 初始化成 Spring Bean
  
  //----------------------本章关注点---------------------------------------------//
  beanFactory.preInstantiateSingletons();
  //----------------------本章关注点---------------------------------------------//
}
```

### AbstractApplicationContext中调用的时机

ApplicationContext中,调用的时机是org.springframework.context.support.AbstractApplicationContext#refresh

 [110-第十一步-初始化所有剩余的非lazy单例Bean.md](../080-Spring拓展点/110-第十一步-初始化所有剩余的非lazy单例Bean.md) 

```java
//org.springframework.context.support.AbstractApplicationContext#refresh
@Override
public void refresh() throws BeansException, IllegalStateException {
  synchronized (this.startupShutdownMonitor) {
    try {
  	//忽略
      
      // Instantiate all remaining (non-lazy-init) singletons.
      //11、初始化所有剩余的单例Bean
              //----------------------本章关注点---------------------------------------------//
			      finishBeanFactoryInitialization(beanFactory);
          //----------------------本章关注点---------------------------------------------//
    }
	//忽略
}
```

在refresh的第十一步时,进行调用

```java
	//对配置了lazy-init属性的Bean进行预实例化处理
	protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
		// Initialize conversion service for this context.
		//这是Spring3以后新加的代码，为容器指定一个转换服务(ConversionService)
		//在对某些Bean属性进行转换时使用
		if (beanFactory.containsBean(CONVERSION_SERVICE_BEAN_NAME) &&
				beanFactory.isTypeMatch(CONVERSION_SERVICE_BEAN_NAME, ConversionService.class)) {
			beanFactory.setConversionService(
					beanFactory.getBean(CONVERSION_SERVICE_BEAN_NAME, ConversionService.class));
		}

		// Register a default embedded value resolver if no bean post-processor
		// (such as a PropertyPlaceholderConfigurer bean) registered any before:
		// at this point, primarily for resolution in annotation attribute values.
		if (!beanFactory.hasEmbeddedValueResolver()) {
			beanFactory.addEmbeddedValueResolver(strVal -> getEnvironment().resolvePlaceholders(strVal));
		}

		// Initialize LoadTimeWeaverAware beans early to allow for registering their transformers early.
		String[] weaverAwareNames = beanFactory.getBeanNamesForType(LoadTimeWeaverAware.class, false, false);
		for (String weaverAwareName : weaverAwareNames) {
			getBean(weaverAwareName);
		}

		// Stop using the temporary ClassLoader for type matching.
		//为了类型匹配，停止使用临时的类加载器
		beanFactory.setTempClassLoader(null);

		// Allow for caching all bean definition metadata, not expecting further changes.
		//缓存容器中所有注册的BeanDefinition元数据，以防被修改
		beanFactory.freezeConfiguration();

		// Instantiate all remaining (non-lazy-init) singletons.
		//对配置了lazy-init属性的单态模式Bean进行预实例化处理
    //----------------------本章关注点---------------------------------------------//
    beanFactory.preInstantiateSingletons();
    //----------------------本章关注点---------------------------------------------//

	}
```

#### 对配置lazy-init属性单态Bean的预实例化

```java
//org.springframework.beans.factory.support.DefaultListableBeanFactory#preInstantiateSingletons
//对配置lazy-init属性单态Bean的预实例化
@Override
public void preInstantiateSingletons() throws BeansException {
  if (this.logger.isDebugEnabled()) {
    this.logger.debug("Pre-instantiating singletons in " + this);
  }

  // Iterate over a copy to allow for init methods which in turn register new bean definitions.
  // While this may not be part of the regular factory bootstrap, it does otherwise work fine.
  List<String> beanNames = new ArrayList<>(this.beanDefinitionNames);

  // Trigger initialization of all non-lazy singleton beans...
  for (String beanName : beanNames) {
    //获取指定名称的Bean定义
    RootBeanDefinition bd = getMergedLocalBeanDefinition(beanName);
    //Bean不是抽象的，是单态模式的，且lazy-init属性配置为false
    if (!bd.isAbstract() && bd.isSingleton() && !bd.isLazyInit()) {
      //如果指定名称的bean是创建容器的Bean
      if (isFactoryBean(beanName)) {
        //FACTORY_BEAN_PREFIX=”&”，当Bean名称前面加”&”符号
        //时，获取的是产生容器对象本身，而不是容器产生的Bean.
        //调用getBean方法，触发容器对Bean实例化和依赖注入过程
        final FactoryBean<?> factory = (FactoryBean<?>) getBean(FACTORY_BEAN_PREFIX + beanName);
        //标识是否需要预实例化
        boolean isEagerInit;
        if (System.getSecurityManager() != null && factory instanceof SmartFactoryBean) {
          //一个匿名内部类
          isEagerInit = AccessController.doPrivileged((PrivilegedAction<Boolean>) () ->
                                                      ((SmartFactoryBean<?>) factory).isEagerInit(),
                                                      getAccessControlContext());
        }
        else {
          isEagerInit = (factory instanceof SmartFactoryBean &&
                         ((SmartFactoryBean<?>) factory).isEagerInit());
        }
        if (isEagerInit) {
          //调用getBean方法，触发容器对Bean实例化和依赖注入过程
          getBean(beanName);
        }
      }
      else {
        getBean(beanName);
      }
    }
  }

  // Trigger post-initialization callback for all applicable beans...
  for (String beanName : beanNames) {
    Object singletonInstance = getSingleton(beanName);
    if (singletonInstance instanceof SmartInitializingSingleton) {
      final SmartInitializingSingleton smartSingleton = (SmartInitializingSingleton) singletonInstance;
      if (System.getSecurityManager() != null) {
        AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
          smartSingleton.afterSingletonsInstantiated();
          return null;
        }, getAccessControlContext());
      }
      else {
        //----------------------本章关注点----初始化前阶段-----------------------------------------//
        smartSingleton.afterSingletonsInstantiated();
        //----------------------本章关注点----初始化前阶段-----------------------------------------//
      }
    }
  }
}
```

## 为什么有了BeanPostProcessor,还要有SmartInitializingSingleton

- SmartInitializingSingleton确保完全初始化 后完成回调

当我们任何一个对象去getBean的时候,它所依赖的对象也会初始化.有可能在初始化的时候,BeanPostProcesser还没有加进来,

这么就会出现一个没有完全初始化完整的Bean

当你需要一个完完全全初始化完毕的回调时,使用SmartInitializingSingleton

