# 070-Spring-Converter接口的局限性

[TOC]

## Converter接口的局限性

#### 局限性1：缺少SourceType和Target Type前置判断

应对措施：增加 org.springframework.core.convert.converter.ConditionalConverter 作为条件判断

#### 局限性2：仅能转换单一的SourceType和TargetType

应对措施：使用org.springframework.core.convert.converter.GenericConverter 来代替

 [080-Spring-GenericConverter接口.md](080-Spring-GenericConverter接口.md) 

