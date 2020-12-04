# 050-SpringBeanClassLoading加载阶段

## 目录

---

[TOC]

## 简介

加载阶段实际上就是在获取到RootBeanDefinition之后(Merge之后),最后通过Class.forName的方式加载到类加载器

-  [040-SpringBeanDefinition合并阶段.md](040-SpringBeanDefinition合并阶段.md) 

## ConfiguableBeanFactory临时ClassLoader

```java
// org.springframework.beans.factory.support.AbstractBeanFactory#doResolveBeanClass 
@Nullable
private ClassLoader beanClassLoader = ClassUtils.getDefaultClassLoader();
```

解析时

```java
//org.springframework.beans.factory.support.AbstractBeanFactory#doResolveBeanClass
@Nullable
private Class<?> doResolveBeanClass(RootBeanDefinition mbd, Class<?>... typesToMatch) throws ClassNotFoundException {

  ClassLoader beanClassLoader = getBeanClassLoader();
  ClassLoader dynamicLoader = beanClassLoader;
  boolean freshResolve = false;

}
```

## SpringBean的Class加载过程

先入为主的入口 `org.springframework.beans.factory.support.AbstractBeanFactory#doGetBean`

```java
@SuppressWarnings("unchecked")
protected <T> T doGetBean(final String name, @Nullable final Class<T> requiredType,
                          @Nullable final Object[] args, boolean typeCheckOnly) throws BeansException {
  //省略从缓存中获取Bena的过程
  try {
    //上一节讲的,合并BeanDefinition
    final RootBeanDefinition mbd = getMergedLocalBeanDefinition(beanName);
    checkMergedBeanDefinition(mbd, beanName, args);

    // Guarantee initialization of beans that the current bean depends on.
    String[] dependsOn = mbd.getDependsOn();
    if (dependsOn != null) {
      //忽略,只要知道dependsOn会先创建
    }
    // Create bean instance.
    if (mbd.isSingleton()) {
      //获取单例类型的管理
      sharedInstance = getSingleton(beanName, () -> {
        try {
          //自定义ObjectFactory,如果找不到就创建一个
          //-------------------本章关注的重点代码---------------------------
          return createBean(beanName, mbd, args);
          //-------------------本章关注的重点代码---------------------------
        }
      });
      bean = getObjectForBeanInstance(sharedInstance, name, beanName, mbd);
    }
    //...忽略

  }
```

#### AbstractAutowireCapableBeanFactory#createBean(RootBeanDefinition, java.lang.Object[])

这里可以看到形参的mbd 是一个 RootBeanDefinition, 实际上是merge过的BeanDefinition

```java
@Override
protected Object createBean(String beanName, RootBeanDefinition mbd, @Nullable Object[] args) throws BeanCreationException {
  if (logger.isTraceEnabled()) {
    logger.trace("Creating instance of bean '" + beanName + "'");
  }
  RootBeanDefinition mbdToUse = mbd;
  // Make sure bean class is actually resolved at this point, and
  // clone the bean definition in case of a dynamically resolved Class
  // which cannot be stored in the shared merged bean definition.
  //判断需要创建的Bean是否可以实例化，即是否可以通过当前的类加载器加载
     //-------------------本章关注的重点代码---------------------------
  Class<?> resolvedClass = resolveBeanClass(mbd, beanName);
     //-------------------本章关注的重点代码---------------------------
  if (resolvedClass != null && !mbd.hasBeanClass() && mbd.getBeanClassName() != null) {
    mbdToUse = new RootBeanDefinition(mbd);
    mbdToUse.setBeanClass(resolvedClass);
  }
	//省略实例化阶段//后续讨论
}
```

####  AbstractBeanFactory#resolveBeanClass

```java
@Nullable
protected Class<?> resolveBeanClass(final RootBeanDefinition mbd, String beanName, final Class<?>... typesToMatch) throws CannotLoadBeanClassException {
  try {
    //当前是不是存在Bean的定义
    if (mbd.hasBeanClass()) {
      return mbd.getBeanClass();
    }
    //获取安全管理器,判断是否有加载类的权限
    if (System.getSecurityManager() != null) {
      return AccessController.doPrivileged((PrivilegedExceptionAction<Class<?>>) () ->
                                           //允许->直接加载
                                           doResolveBeanClass(mbd, typesToMatch), getAccessControlContext());
    }
    else {
      //如果没有开启安全管理器,直接进行解析BeanClass
      return doResolveBeanClass(mbd, typesToMatch);
    }
  }
  catch (PrivilegedActionException pae) {
    ClassNotFoundException ex = (ClassNotFoundException) pae.getException();
    throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), ex);
  }
  catch (ClassNotFoundException ex) {
    throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), ex);
  }
  catch (LinkageError ex) {
    throw new CannotLoadBeanClassException(mbd.getResourceDescription(), beanName, mbd.getBeanClassName(), ex);
  }
}
```

#### AbstractBeanFactory#doResolveBeanClass

```java
@Nullable
private Class<?> doResolveBeanClass(RootBeanDefinition mbd, Class<?>... typesToMatch)
  throws ClassNotFoundException {

  //获取当前Factory中的ClassLoader -> 经常是 AppClassLoader
  ClassLoader beanClassLoader = getBeanClassLoader();
  ClassLoader classLoaderToUse = beanClassLoader;
  if (!ObjectUtils.isEmpty(typesToMatch)) {
    // When just doing type checks (i.e. not creating an actual instance yet),
    // use the specified temporary class loader (e.g. in a weaving scenario).
    ClassLoader tempClassLoader = getTempClassLoader();
    if (tempClassLoader != null) {
      classLoaderToUse = tempClassLoader;
      if (tempClassLoader instanceof DecoratingClassLoader) {
        DecoratingClassLoader dcl = (DecoratingClassLoader) tempClassLoader;
        for (Class<?> typeToMatch : typesToMatch) {
          dcl.excludeClass(typeToMatch.getName());
        }
      }
    }
  }
  //获取BeanClass的name .1. String类型 2.Class类型
  String className = mbd.getBeanClassName();
  if (className != null) {
    Object evaluated = evaluateBeanDefinitionString(className, mbd);
    if (!className.equals(evaluated)) {
      // A dynamically resolved expression, supported as of 4.2...
      //Class类型
      if (evaluated instanceof Class) {
        return (Class<?>) evaluated;
      }
      //String类型
      else if (evaluated instanceof String) {
        return ClassUtils.forName((String) evaluated, classLoaderToUse);
      }
      else {
        throw new IllegalStateException("Invalid class name expression result: " + evaluated);
      }
    }
    // When resolving against a temporary class loader, exit early in order
    // to avoid storing the resolved Class in the bean definition.
    if (classLoaderToUse != beanClassLoader) {
      return ClassUtils.forName(className, classLoaderToUse);
    }
  }

  return mbd.resolveBeanClass(beanClassLoader);
}

```

## AbstractBeanDefinition#resolveBeanClass

```java
@Nullable
public Class<?> resolveBeanClass(@Nullable ClassLoader classLoader) throws ClassNotFoundException {
   String className = getBeanClassName();
   if (className == null) {
      return null;
   }
        //-------------------本章关注的重点代码---------------------------
   Class<?> resolvedClass = ClassUtils.forName(className, classLoader);
        //-------------------本章关注的重点代码---------------------------
   this.beanClass = resolvedClass;
   return resolvedClass;
}	
```