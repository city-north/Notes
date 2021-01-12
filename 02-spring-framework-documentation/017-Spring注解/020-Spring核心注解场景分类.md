

# 020-Spring核心注解场景分类

[TOC]

## Spring模式注解(Stereotype Annotations)

[050-Spring模式注解-Stereotype-Annotations.md](050-Spring模式注解-Stereotype-Annotations.md) 

| Spring注解     | 场景说明          | 其实版本 |
| -------------- | ----------------- | -------- |
| @Repository    | 数据仓储模式注解  | 2.0      |
| @Compoent      | 通用主键模式注解  | 2.5      |
| @Service       | 服务模式注解      | 2.5      |
| @Controller    | Web控制器模式注解 | 2.5      |
| @Configuration | 配置类模式注解    | 3.0      |

## Spring装配注解

| Spring注解      | 场景说明                                | 其实版本 |
| --------------- | --------------------------------------- | -------- |
| @ImportResource | 替换xml元素`<import>`                   | 2.5      |
| @Import         | 导入Configuration类                     | 2.5      |
| @CompinentScan  | 扫描指定package下标注Spring模式注解的类 | 3.1      |

## Spring依赖注入注解

| Spring注解 | 场景说明                           | 其实版本 |
| ---------- | ---------------------------------- | -------- |
| @Autowired | Bean依赖注入，支持多种依赖查找方式 | 2.5      |
| @Qualifier | 细粒度@Autowired依赖查找           | 2.5      |

