# 100-基于Java注解装载SpringIoC配置元信息.md

[TOC]

## SpringIoC容器装配注解

| Spring注解      | 场景说明                                | 起始版本 |
| --------------- | --------------------------------------- | -------- |
| @ImportResource | 替换XML元素import                       | 3.0      |
| @Import         | 导入Configuration Class                 | 3.0      |
| @ComponetScan   | 扫描指定package下标注Spring模式注解的类 | 3.1      |

## SpringIoC配置属性注解

| Spring注解       | 场景说明                   | 起始版本 |
| ---------------- | -------------------------- | -------- |
| @PropertySource  | 配置属性抽象PropertySource | 3.1      |
| @PropertySources | PropertySource集合注解     | 4.0      |

