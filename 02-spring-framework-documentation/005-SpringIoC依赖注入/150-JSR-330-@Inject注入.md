# 150-JSR-330-@Inject注入

[TOC]

## 目录

- [先入为主的核心类](#先入为主的核心类)
- [@Inject支持-构造函数](#@Inject支持-构造函数)
- [判断是否存在这些注解](#判断是否存在这些注解)
- [自定义进行依赖注入的注解](#自定义进行依赖注入的注解)

## 先入为主的核心类

- @Inject注入过程
  - 如果JSR-330存在于ClassPath中,复用AutowiredAnnotationBeanPostProcessor实现

## @Inject支持-构造函数

AutowiredAnnotationBeanPostProcessor 的构造函数中加入了对注解的支持

```java
//org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor#AutowiredAnnotationBeanPostProcessor

	@SuppressWarnings("unchecked")
	public AutowiredAnnotationBeanPostProcessor() {
		this.autowiredAnnotationTypes.add(Autowired.class);
		this.autowiredAnnotationTypes.add(Value.class);
		try {
      //判断当前classpath下是否有相应的类
			this.autowiredAnnotationTypes.add((Class<? extends Annotation>)
					ClassUtils.forName("javax.inject.Inject", AutowiredAnnotationBeanPostProcessor.class.getClassLoader()));
			logger.trace("JSR-330 'javax.inject.Inject' annotation found and supported for autowiring");
		}
		catch (ClassNotFoundException ex) {
			// JSR-330 API not available - simply skip.
		}
	}


```

## 判断是否存在这些注解

实际上这里的cacheAutowired注入方法本质上和Autowired 注解是类似的

```java
@Nullable
	private MergedAnnotation<?> findAutowiredAnnotation(AccessibleObject ao) {
		MergedAnnotations annotations = MergedAnnotations.from(ao);
		for (Class<? extends Annotation> type : this.autowiredAnnotationTypes) {
			MergedAnnotation<?> annotation = annotations.get(type);
			if (annotation.isPresent()) {
				return annotation;
			}
		}
		return null;
	}
```

#### JSR-303-API

```xml
<dependency>
  <groupId>javax.inject</groupId>
  <artifactId>javax.inject</artifactId>
  <version>1</version>
</dependency>
```

## 注入依赖

实际上这里的cacheAutowired注入方法本质上和Autowired 注解是类似的

```java
//org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor#postProcessProperties

	@Override
	public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) {
		InjectionMetadata metadata = findAutowiringMetadata(beanName, bean.getClass(), pvs);
		try {
			metadata.inject(bean, beanName, pvs);
		}
		catch (BeanCreationException ex) {
			throw ex;
		}
		catch (Throwable ex) {
			throw new BeanCreationException(beanName, "Injection of autowired dependencies failed", ex);
		}
		return pvs;
	}
```

## 自定义进行依赖注入的注解

```java
@Bean
@Order(Ordered.LOWEST_PRECEDENCE - 3)
@Scope
public static AutowiredAnnotationBeanPostProcessor beanPostProcessor() {
  //相当于覆盖掉Spring自带的处理器
  AutowiredAnnotationBeanPostProcessor beanPostProcessor = new AutowiredAnnotationBeanPostProcessor();
  //设置我们要使用的注解
  beanPostProcessor.setAutowiredAnnotationType(InjectedUser.class);
  return beanPostProcessor;
}
```

#### @InjectedUser

```java
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface InjectedUser {
}

```

