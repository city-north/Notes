# 090-基于XML文件装载SpringIoC容器配置元信息

[TOC]

## SpringBean配置元信息

| XML元素           | 使用场景                                 |
| ----------------- | ---------------------------------------- |
| `<beans:beans/>`  | 单XML资源下的多个SpringBeans配置         |
| `<beans:bean/>`   | 单个SpringBean定义(BeanDefinition)配置   |
| `<beans:alias/>`  | 为SpringBean定义(BeanDefinition)映射别名 |
| `<beans:import/>` | 加载外部SpringXML配置资源                |