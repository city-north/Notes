# 070-基于Java注解装载SpringBean配置元信息

[TOC]

## 核心API

 [020-SpringBean元信息解析阶段.md](../008-SpringBean生命周期/020-SpringBean元信息解析阶段.md) 

org.springframework.context.annotation.AnnotatedBeanDefinitionReader

- 资源
  - 类对象-java.lang.Class
- 底层
  - [条件评估-ConditionEvaluator](#条件评估-ConditionEvaluator)
  - [Bean范围解析-ScopeMetadataReslover](#Bean范围解析-ScopeMetadataReslover)
  - [BeanDefinition解析-内部API实现](#BeanDefinition解析-内部API实现)
  - [BeanDefinition处理-AnnotationConfigUtils.processCommonDefinitionAnnotations](#BeanDefinition处理-AnnotationConfigUtils.processCommonDefinitionAnnotations)
  - [BeanDefinition注册-BeanDefinitonRegistry](#BeanDefinition注册-BeanDefinitonRegistry ) 

## AnnotatedBeanDefinitionReader

并没有直接继承或者实现BeanDefinitionReader

 [080-标注注解-AnnotatedBeanDefinition.md](../003-SpringBean的定义-BeanDefiniation/080-标注注解-AnnotatedBeanDefinition.md) 

原因是它不是传统的BeanDefinition那样,是基于Resource资源

### 条件评估-ConditionEvaluator

条件评估的工具类

#### shouldSkip

```java
//是不是跳过Bean,也就是@Condition注解是否成立
public boolean shouldSkip(AnnotatedTypeMetadata metadata) ;
```

### Bean范围解析-ScopeMetadataReslover

解析元数据相关的数据

