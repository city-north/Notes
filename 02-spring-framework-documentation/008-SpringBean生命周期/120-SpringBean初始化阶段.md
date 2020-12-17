# 120-SpringBean初始化阶段

## 目录

------

[TOC]

## 一言蔽之

初始化阶段主要包括三种初始化方式

1. @PostConstruct(依赖注解驱动)-CommonAnnotationBeanPostProcessor方式
2. 实现InitializingBean接口的afterPropertiesSet()方法-固定流程
3. 自定义初始化方法-固定流程

## DEMO

```java
public class SpringBeanInitializationDemo {
    public static void main(String[] args) {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        final int i = reader.loadBeanDefinitions(new ClassPathResource("lifecycle/beforeInitilization/spring-bean-lifecycle-initalization.xml"));
        System.out.printf("加载bean个数为 %s", i);
        System.out.println();
        //提供@PostConstruct支持
        beanFactory.addBeanPostProcessor(new CommonAnnotationBeanPostProcessor());
        final BeanInitializationDemo bean = beanFactory.getBean(BeanInitializationDemo.class);
        System.out.println("bean:" + bean);
//       输出1 ---testPostConstruct---
//       输出2 ---afterPropertiesSet---
//       输出3 ---customInitMethod---
    }
}
```

### BeanInitializationDemo定义

```java
public class BeanInitializationDemo implements InitializingBean {
    private String name;

  	//使用PostConstruct支持进行初始化
    @PostConstruct
    public void testPostConstruct() {
        System.out.println("---testPostConstruct---");
    }
	
  	//使用InitializingBean回调初始化
    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("---afterPropertiesSet---");
    }
  
  	//使用xml中配置的init 方法进行初始化
    public void customInitMethod() {
        System.out.println("---customInitMethod---");
    }
}
```

### @PostConstruct(依赖注解驱动)

执行原理,通过CommonAnnotationBeanPostProcessor后置处理器完成 

 [160-Java通用注解注入原理-CommonAnnotationBeanPostProcessor.md](../005-SpringIoC依赖注入/160-Java通用注解注入原理-CommonAnnotationBeanPostProcessor.md) 

```java
//org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#initializeBean(String, Object, RootBeanDefinition)
//初始化方法
protected Object initializeBean(final String beanName, final Object bean, @Nullable RootBeanDefinition mbd) {
  if (System.getSecurityManager() != null) {
    AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
      //执行aware相关操作
      invokeAwareMethods(beanName, bean);
      return null;
    }, getAccessControlContext());
  }
  else {
    //执行aware相关操作
    invokeAwareMethods(beanName, bean);
  }

  Object wrappedBean = bean;
  if (mbd == null || !mbd.isSynthetic()) {
    //应用实例化前置处理器, @PostConstruct 就是使用实例化前置处理器
    wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
  }

  try {
    //执行初始化方法 //afterPropertiesSet方法是在这里处理的
    invokeInitMethods(beanName, wrappedBean, mbd);
  }
  catch (Throwable ex) {
    throw new BeanCreationException(
      (mbd != null ? mbd.getResourceDescription() : null),
      beanName, "Invocation of init method failed", ex);
  }
  if (mbd == null || !mbd.isSynthetic()) {
    wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);
  }

  return wrappedBean;
}
```

applyBeanPostProcessorsBeforeInitialization

```java
@Override
public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
  throws BeansException {

  Object result = existingBean;
  for (BeanPostProcessor processor : getBeanPostProcessors()) {
    //执行 CommonAnnotationBeanPostProcessor前置处理器
    Object current = processor.postProcessBeforeInitialization(result, beanName);
    if (current == null) {
      return result;
    }
    result = current;
  }
  return result;
}
```

```java
//org.springframework.beans.factory.annotation.InitDestroyAnnotationBeanPostProcessor#postProcessBeforeInitialization	
@Override
public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
 	//获取声明周期的原信息
  LifecycleMetadata metadata = findLifecycleMetadata(bean.getClass());
  try {
    //调用初始化方法
    metadata.invokeInitMethods(bean, beanName);
  }
	//...忽略
  return bean;
}
```

执行具体的初始化逻辑

![image-20201126214435489](../../assets/image-20201126214435489.png)



### 实现InitializingBean接口的afterPropertiesSet()方法

堆栈信息

```
afterPropertiesSet:47, BeanInitializationDemo (cn.eccto.study.springframework.lifecycle.demoBean)
invokeInitMethods:1804, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
initializeBean:1741, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
doCreateBean:576, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
createBean:498, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
lambda$doGetBean$0:320, AbstractBeanFactory (org.springframework.beans.factory.support)
getObject:-1, 1346343363 (org.springframework.beans.factory.support.AbstractBeanFactory$$Lambda$6)
getSingleton:222, DefaultSingletonBeanRegistry (org.springframework.beans.factory.support)
doGetBean:318, AbstractBeanFactory (org.springframework.beans.factory.support)
getBean:224, AbstractBeanFactory (org.springframework.beans.factory.support)
resolveNamedBean:1114, DefaultListableBeanFactory (org.springframework.beans.factory.support)
resolveBean:407, DefaultListableBeanFactory (org.springframework.beans.factory.support)
getBean:341, DefaultListableBeanFactory (org.springframework.beans.factory.support)
getBean:335, DefaultListableBeanFactory (org.springframework.beans.factory.support)
main:23, SpringBeanInitializationDemo (cn.eccto.study.springframework.lifecycle)
```

AbstractAutowireCapableBeanFactory#invokeInitMethods

```java
protected void invokeInitMethods(String beanName, final Object bean, @Nullable RootBeanDefinition mbd)throws Throwable {
  //判断是否有InitializingBean 接口
  boolean isInitializingBean = (bean instanceof InitializingBean);
  if (isInitializingBean && (mbd == null || !mbd.isExternallyManagedInitMethod("afterPropertiesSet"))) {
  		//忽略...
    //具体执行的逻辑
      ((InitializingBean) bean).afterPropertiesSet();
    //忽略..
  }

  if (mbd != null && bean.getClass() != NullBean.class) {
    String initMethodName = mbd.getInitMethodName();
    if (StringUtils.hasLength(initMethodName) &&
        !(isInitializingBean && "afterPropertiesSet".equals(initMethodName)) &&
        !mbd.isExternallyManagedInitMethod(initMethodName)) {
      //执行自定义的逻辑
      invokeCustomInitMethod(beanName, bean, mbd);
    }
  }
}
```

### 自定义初始化方法

```java
customInitMethod:25, BeanInitializationDemo (cn.eccto.study.springframework.lifecycle.demoBean)
invoke0:-1, NativeMethodAccessorImpl (sun.reflect)
invoke:62, NativeMethodAccessorImpl (sun.reflect)
invoke:43, DelegatingMethodAccessorImpl (sun.reflect)
invoke:498, Method (java.lang.reflect)
invokeCustomInitMethod:1870, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
invokeInitMethods:1813, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
initializeBean:1741, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
doCreateBean:576, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
createBean:498, AbstractAutowireCapableBeanFactory (org.springframework.beans.factory.support)
lambda$doGetBean$0:320, AbstractBeanFactory (org.springframework.beans.factory.support)
getObject:-1, 1346343363 (org.springframework.beans.factory.support.AbstractBeanFactory$$Lambda$6)
getSingleton:222, DefaultSingletonBeanRegistry (org.springframework.beans.factory.support)
doGetBean:318, AbstractBeanFactory (org.springframework.beans.factory.support)
getBean:224, AbstractBeanFactory (org.springframework.beans.factory.support)
resolveNamedBean:1114, DefaultListableBeanFactory (org.springframework.beans.factory.support)
resolveBean:407, DefaultListableBeanFactory (org.springframework.beans.factory.support)
getBean:341, DefaultListableBeanFactory (org.springframework.beans.factory.support)
getBean:335, DefaultListableBeanFactory (org.springframework.beans.factory.support)
main:23, SpringBeanInitializationDemo (cn.eccto.study.springframework.lifecycle)
```

第三步执行的执行自定义初始化方法

```java
//org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#invokeCustomInitMethod
protected void invokeCustomInitMethod(String beanName, final Object bean, RootBeanDefinition mbd)
  throws Throwable {
	//获取Bean定义的初始化方法
  String initMethodName = mbd.getInitMethodName();
  Assert.state(initMethodName != null, "No init method set");
  Method initMethod = (mbd.isNonPublicAccessAllowed() ?
                       BeanUtils.findMethod(bean.getClass(), initMethodName) :
                       ClassUtils.getMethodIfAvailable(bean.getClass(), initMethodName));

  if (initMethod == null) {
    if (mbd.isEnforceInitMethod()) {
      throw new BeanDefinitionValidationException("Could not find an init method named '" +
                                                  initMethodName + "' on bean with name '" + beanName + "'");
    }
    else {
      if (logger.isTraceEnabled()) {
        logger.trace("No default init method named '" + initMethodName +
                     "' found on bean with name '" + beanName + "'");
      }
      // Ignore non-existent default lifecycle methods.
      return;
    }
  }

  if (logger.isTraceEnabled()) {
    logger.trace("Invoking init method  '" + initMethodName + "' on bean with name '" + beanName + "'");
  }
  Method methodToInvoke = ClassUtils.getInterfaceMethodIfPossible(initMethod);

  if (System.getSecurityManager() != null) {
    AccessController.doPrivileged((PrivilegedAction<Object>) () -> {
      ReflectionUtils.makeAccessible(methodToInvoke);
      return null;
    });
    try {
      AccessController.doPrivileged((PrivilegedExceptionAction<Object>) () ->
                                    methodToInvoke.invoke(bean), getAccessControlContext());
    }
    catch (PrivilegedActionException pae) {
      InvocationTargetException ex = (InvocationTargetException) pae.getException();
      throw ex.getTargetException();
    }
  }
  else {
    try {
      ReflectionUtils.makeAccessible(methodToInvoke);
      //执行方法
      methodToInvoke.invoke(bean);
    }
    catch (InvocationTargetException ex) {
      throw ex.getTargetException();
    }
  }
}

```

