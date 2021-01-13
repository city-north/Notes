# @Component派生性原理

[TOC]

## 原理简介





## 核心组件

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

