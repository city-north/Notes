# 030-Errors接口设计

[TOC]

## Errors接口职责

- 数据绑定和校验错误收集接口,与Java Bean 和其属性有强关联性

## 核心方法

- reject方法(重载) : 收集错误文案
- rejectValue方法(重载) : 收集对象字段中的额错误文案

## 配套组件

- JavaBean 错误描述: org.springframework.validation.ObjectError
- JavaBean属性错误描述:  org.springframework.validation.FieldError