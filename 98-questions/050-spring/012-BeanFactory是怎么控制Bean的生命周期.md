# BeanFactory是怎么控制Bean的生命周期

BeanFactory的默认实现是DefaultListableBeanFactory , 其中Bean声明周期与方法映射如下:

|      | BeanFactory控制的生命周期 | 方法                       | 笔记 |
| ---- | ------------------------- | -------------------------- | ---- |
| 1    | BeanDefinition注册阶段    | registerBeanDefinition     |      |
| 2    | BeanDefinition合并阶段    | getMergedBeanDefinition    |      |
| 3    | Bean实例化前阶段          | resolveBeforeInstantiation |      |
| 4    | Bean实例化阶段-           | createBeanInstance         |      |
| 5    | Bean初始化后阶段          | populateBean               |      |
| 6    | Bean属性赋值前阶段        | populateBean               |      |
| 7    | Bean属性赋值阶段          | populateBean               |      |
| 8    | BeanAware接口回调阶段     | initializeBean             |      |
| 9    | Bean初始化前阶段          | initializeBean             |      |
| 10   | Bean初始化阶段            | initializeBean             |      |
| 11   | Bean初始化后阶段          | initializeBean             |      |
| 12   | Bean初始化完成阶段        | preInstantiateSingletons   |      |
| 13   | Bean销毁前阶段            | destoryBean                |      |
| 14   | Bean销毁阶段              | destoryBean                |      |