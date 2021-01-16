# 020-Spring拓展点-BeanDefinitionRegistryPostProcessor

[TOC]

## 一言蔽之

参考Spring笔记

[040-BeanFactory后置处理阶段-postProcessBeanFactory().md](../../02-spring-framework-documentation/019-Spring应用上下文生命周期/040-BeanFactory后置处理阶段-postProcessBeanFactory().md) 

## DEMO

> org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor

这个接口在读取项目中的`beanDefinition`之后执行，提供一个补充的扩展点

使用场景：你可以在这里动态注册自己的`beanDefinition`，可以加载classpath之外的bean

扩展方式为:

```java
public class TestBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {
    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        System.out.println("[BeanDefinitionRegistryPostProcessor] postProcessBeanDefinitionRegistry");
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        System.out.println("[BeanDefinitionRegistryPostProcessor] postProcessBeanFactory");
    }
}
```