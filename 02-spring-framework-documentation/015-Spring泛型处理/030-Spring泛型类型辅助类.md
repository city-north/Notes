# 030-Spring泛型类型辅助类

[TOC]

## 核心API

org.springframework.core.GenericTypeResolver

- 版本支持 2.5.2
- 处理类型相关(Type)相关方法
  - resolveReturnType
  - resolveType

- 处理泛型参数类型(ResolvableType) 相关方法
  - resolveReturnTypeArgument
  - resolveTypeArgument
  - resolveTypeArguments
- 处理泛型类型变量(TypeVariable) 相关方法
  - getTypeVariableMap

