# 030-Spring依赖注入DI过程

## 目录

- 依赖注入的核心类

## 依赖注入的核心类

| 序号 | 核心类                      | 核心方法  | 简介                                      |
| ---- | --------------------------- | --------- | ----------------------------------------- |
| 1    | BeanFactory                 | getBean   | 依赖注入的入口,懒加载                     |
| 2    | AbstractBeanFactory         | doGetBean | 具体依赖注入的入口                        |
| 3    | SimpleInstantiationStrategy |           | 实例化策略                                |
| 4    | BeanWrapper                 |           | 存储实例所有相关信息,scope,proxy,instance |
| 5    | BeanWrapperImpl             |           | BeanWrapper的默认实现                     |

## 依赖注入流程

![一步一步手绘Spring DI运行时序图](../../assets/一步一步手绘Spring DI运行时序图.png)

- [实例化-入口](001-实例化-入口.md) 
- [实例化-创建Bean](002-实例化-创建Bean.md) 
- [实例化-填充Bean](003-实例化-填充Bean.md) 
- [依赖注入-对属性进行注入](004-依赖注入-对属性进行注入.md) 

