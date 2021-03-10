# 020-BeanNameGenerator-Bean名称生成器

[TOC]

## Hierarchy

核心接口-BeanNameGenerator

- [默认实现DefaultBeanNameGenerator](#默认实现DefaultBeanNameGenerator)
- [基于注解扫描的方式生成AnnotationBeanNameGenerator](#基于注解扫描的方式生成AnnotationBeanNameGenerator)

## 默认实现DefaultBeanNameGenerator

```java
public class DefaultBeanNameGenerator implements BeanNameGenerator {

	@Override
	public String generateBeanName(BeanDefinition definition, BeanDefinitionRegistry registry) {
		return BeanDefinitionReaderUtils.generateBeanName(definition, registry);
	}
}
```

## 基于注解扫描的方式生成AnnotationBeanNameGenerator

为一些没有设置bean名称的bean 设置默认的名称

- 类名称第一个字符小写
- 如果被占用,则使用JavaBean 中的
  - java.bean.Introspector.decapitalize

```java
	protected String buildDefaultBeanName(BeanDefinition definition) {
		String beanClassName = definition.getBeanClassName();
		Assert.state(beanClassName != null, "No bean class name set");
		String shortClassName = ClassUtils.getShortName(beanClassName);
    //使用自省的默认名称
		return Introspector.decapitalize(shortClassName);
	}
```

