# 100-Spring条件注解

[TOC]

## Spring条件注解

- 基于配置条件注解-org.springframework.context.annotation.Profile
  - 关联对象-org.springframework.core.env.Environment 中的Profiles
  - 实现变化：从Spring 4.0 开始，@Profile基于 @Conditional实现
- 基于编程条件注解-org.springframework.context.annotation.Conditional
  - 关联对象-org.springframework.context.annotation.Condition具体实现

## Conditional实现原理

- 上下文对象-org.springframework.context.annotation.ConditionContext
- 条件判断-org.springframework.context.annotation.ConditionEvaluator
- 配置阶段-org.springframework.context.annotation.ConfigurationCondition
- 判断入口- org.springframework.context.annotation.ConfigurationClassPostProcessor
  - org.springframework.context.annotation.ConfigurationClassParser

## 配置格式

- 基于配置(setDefaultProfile)
- 基于编程条件（Conditional)

