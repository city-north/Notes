# 050-Spring模式注解-Stereotype-Annotations

[TOC]

## 理解@Component派生性

元标注@Component的注解在XML元素=<context:context-scan> 或者注解 `@ComponentScan`扫描中派生了`Conponent`的特性，并且从Spring Framework 4.0 之后开始支持多层次的派生性

## @Component派生性原理

- 核心组件：org.springframework.context.annotation.ClassPathBeanDefinitionScanner
  - org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
- 资源处理：org.springframework.core.io.support.ResourcePatternResolver
- 资源-类元信息
  - org.springframework.core.type.classreading.MetadataReaderFactory
- 类元信息 : org.springframework.core.type.ClassMetadata
  - ASM实现：org.springframework.core.type.classreading.ClassMetadataReadingVisitor
  - 反射实现：org.springframework.core.type.StandardAnnotationMetadata
- 注解元信息- org.springframework.core.type.AnnotationMetadata
  - ASM实现：org.springframework.core.type.classreading.AnnotationMetadataReadingVisitor
  - 反射实现：org.springframework.core.type.StandardAnnotationMetadata

## 源码分析

```java

```

