# 130-SpringBean初始化后阶段.md

## 目录

------

[TOC]

## 一言蔽之

初始化Bean之后调用的BeanPostProcessor#postProcessAfterInitialization,主要是在初始化完成周,我们可以对bean进行一些列的自定义操作

## DEMO

```java
DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
reader.loadBeanDefinitions(new ClassPathResource("lifecycle/beforeInitilization/spring-bean-lifecycle-before-initialization.xml"));
beanFactory.addBeanPostProcessor(new BeanPostProcessor() {
  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    System.out.println("---调用初始化后置处理器---");
    //返回null则直接返回,不走后续processor
    return null;
  }
});
final BeanInitializationDemo bean = beanFactory.getBean(BeanInitializationDemo.class);
System.out.println(bean);
```

## BeanPostProcessor注册阶段

 [110-SpringBean初始化前阶段.md](110-SpringBean初始化前阶段.md) 

## BeanPostProcessor初始化后置阶段

代码入口就是在属性赋值之后,进行初始化操作之前进行的,入口为initializeBean

![image-20201126000429923](../../assets/image-20201126000429923.png)

#### 堆栈信息

```java
postProcessAfterInitialization:27, SpringBeanAfterInitializationDemo$1 (cn.eccto.study.springframework.lifecycle)
//应用初始化后置阶段Processor
applyBeanPostProcessorsAfterInitialization:434, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
//初始化bean
initializeBean:1749, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
doCreateBean:576, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
createBean:498, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
lambda$doGetBean$0:320, AbstractBeanFactory (org.springframework.beans.factory.support)
getObject:-1, 221111433 (org.springframework.beans.factory.support.AbstractBeanFactory$$Lambda$6)
getSingleton:222, DefaultSingletonBeanRegistry (org.springframework.beans.factory.support)
doGetBean:318, AbstractBeanFactory (org.springframework.beans.factory.support)
getBean:224, AbstractBeanFactory (org.springframework.beans.factory.support)
resolveNamedBean:1114, DefaultListableBeanFactory (org.springframework.beans.factory.support)
resolveBean:407, DefaultListableBeanFactory (org.springframework.beans.factory.support)
getBean:341, DefaultListableBeanFactory (org.springframework.beans.factory.support)
getBean:335, DefaultListableBeanFactory (org.springframework.beans.factory.support)
main:32, SpringBeanAfterInitializationDemo (cn.eccto.study.springframework.lifecycle)
```

AbstractAutowireCapableBeanFactory#initializeBean

```java
//org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#initializeBean
//初始容器创建的Bean实例对象，为其添加BeanPostProcessor后置处理器
protected Object initializeBean(final String beanName, final Object bean, @Nullable RootBeanDefinition mbd) {
  //JDK的安全机制验证权限
  if (System.getSecurityManager() != null) {
    //实现PrivilegedAction接口的匿名内部类
    AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
      //执行Aware操作回调
      invokeAwareMethods(beanName, bean);
      return null;
    }, getAccessControlContext());
  }
  else {
    //为Bean实例对象包装相关属性，如名称，类加载器，所属容器等信息
    invokeAwareMethods(beanName, bean);
  }

  Object wrappedBean = bean;
  //对BeanPostProcessor后置处理器的postProcessBeforeInitialization
  //回调方法的调用，为Bean实例初始化前做一些处理
  if (mbd == null || !mbd.isSynthetic()) {
    wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
  }

  //调用Bean实例对象初始化的方法，这个初始化方法是在Spring Bean定义配置
  //文件中通过init-method属性指定的
  try {
    invokeInitMethods(beanName, wrappedBean, mbd);
  }
  catch (Throwable ex) {
    throw new BeanCreationException(
      (mbd != null ? mbd.getResourceDescription() : null),
      beanName, "Invocation of init method failed", ex);
  }
  //对BeanPostProcessor后置处理器的postProcessAfterInitialization
  //回调方法的调用，为Bean实例初始化之后做一些处理
  if (mbd == null || !mbd.isSynthetic()) {
        //----------------------本章关注点----初始化前阶段-----------------------------------------//
    wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
        //----------------------本章关注点----初始化前阶段-----------------------------------------//
  }

  return wrappedBean;
}
```

#### 调用BeanPostProcessor后置处理器实例对象初始化之后的处理方法

```java
@Override
//调用BeanPostProcessor后置处理器实例对象初始化之后的处理方法
public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
		throws BeansException {

	Object result = existingBean;
	//遍历容器为所创建的Bean添加的所有BeanPostProcessor后置处理器
	for (BeanPostProcessor beanProcessor : getBeanPostProcessors()) {
		//调用Bean实例所有的后置处理中的初始化后处理方法，为Bean实例对象在
		//初始化之后做一些自定义的处理操作
		Object current = beanProcessor.postProcessAfterInitialization(result, beanName);
		if (current == null) {
			return result;
		}
		result = current;
	}
	return result;
```