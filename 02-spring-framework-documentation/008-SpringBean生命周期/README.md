# 008-SpringBean生命周期

# BeanFactory是怎么控制Bean的生命周期

BeanFactory的默认实现是DefaultListableBeanFactory , 其中Bean声明周期与方法映射如下:

|      | BeanFactory控制的生命周期 | 方法                       | 笔记                                                         |
| ---- | ------------------------- | -------------------------- | ------------------------------------------------------------ |
| 1    | BeanDefinition注册阶段    | registerBeanDefinition     | [030-SpringBean注册阶段.md](030-SpringBean注册阶段.md)       |
| 2    | BeanDefinition合并阶段    | getMergedBeanDefinition    | [040-SpringBeanDefinition合并阶段.md](040-SpringBeanDefinition合并阶段.md) |
| 3    | Bean实例化前阶段          | resolveBeforeInstantiation | [060-SpringBean实例化前阶段.md](060-SpringBean实例化前阶段.md) |
| 4    | Bean实例化阶段            | createBeanInstance         | [070-SpringBean实例化阶段.md](070-SpringBean实例化阶段.md)   |
| 5    | Bean实例化后阶段          | populateBean               | [080-SpringBean实例化后阶段.md](080-SpringBean实例化后阶段.md) |
| 6    | Bean属性赋值前阶段        | populateBean               | [090-SpringBean属性赋值前阶段.md](090-SpringBean属性赋值前阶段.md) |
| 7    | Bean属性赋值阶段          | populateBean               | [090-SpringBean属性赋值前阶段.md](090-SpringBean属性赋值前阶段.md) |
| 8    | BeanAware接口回调阶段     | initializeBean             | [100-SpringBean初始化阶段-Aware接口回调.md](100-SpringBean初始化阶段-Aware接口回调.md) |
| 9    | Bean初始化前阶段          | initializeBean             | [110-SpringBean初始化前阶段.md](110-SpringBean初始化前阶段.md) |
| 10   | Bean初始化阶段            | initializeBean             | [120-SpringBean初始化阶段.md](120-SpringBean初始化阶段.md)   |
| 11   | Bean初始化后阶段          | initializeBean             | [130-SpringBean初始化后阶段.md](130-SpringBean初始化后阶段.md) |
| 12   | Bean初始化完成阶段        | preInstantiateSingletons   | [140-SpringBean初始化完成阶段.md](140-SpringBean初始化完成阶段.md) |
| 13   | Bean销毁前阶段            | destoryBean                | [150-SpringBean销毁前阶段.md](150-SpringBean销毁前阶段.md)   |
| 14   | Bean销毁阶段              | destoryBean                | [160-SpringBean销毁阶段.md](160-SpringBean销毁阶段.md)       |

