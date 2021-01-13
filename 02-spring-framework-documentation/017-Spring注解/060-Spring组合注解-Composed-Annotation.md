# 060-Spring组合注解-Composed-Annotation

[toc]

## 什么是组合注解

当我们注解运用更多的时候呢，会出现“注解爆炸”情况，相当于我标记非常多的注解，例如微服务开发，有很多的注解SpringBoot注解，SpringCloud注解等等

组合注解是指某个元素"元标注"一个或者多个其他注解,其目的在于将这些关联的注解行为组合成单个自定义注解

例如`@TransactionalService`注解,就同时表达了`@Transaction`注解和`@Service`的语义,同时,由于元注解模式, 它也拥有`@Component`注解语义

## 基本定义

Spring组合注解（Composed Annotations）中的元注解允许使用Spring的模式注解（Stereotype Annotation)注解与其他Spring功能性注解的任意组合

