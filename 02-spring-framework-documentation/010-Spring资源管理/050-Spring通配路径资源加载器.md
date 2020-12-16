# 050-Spring通配路径资源加载器

[TOC]

## 核心类

#### 通配路径ResourceResolver

- org.springframework.core.io.support.ResourcePatternResolver
  - org.springframework.core.io.support.PathMatchingResourcePatternResolver

#### 路径匹配器

- org.springframework.util.PathMatcher
  - org.springframework.util.AntPathMatcher - Ant模式匹配实现

#### 什么是Ant模式

一种路径匹配模式

- *.jsp 代表所有jsp为后缀的文件
- `*.*`代表所有目录以及其子目录