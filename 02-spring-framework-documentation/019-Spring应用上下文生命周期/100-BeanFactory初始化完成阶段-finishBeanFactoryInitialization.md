# 100-BeanFactory初始化完成阶段

[TOC]

## BeanFactory初始化完成阶段做了什么?

- AbstractApplicationContext#finishBeanFactoryInitialization(ConfigurableListableBeanFactory)
  - BeanFactory关联ConversionServerice Bean , 如果存在
  - 添加 StringValueResolver对象
  - 依赖查找 LoadTimeWeaverAwareBean
  - BeanFactory临时ClassLoader 置为null
  - BeanFactory冻结配置
  - BeanFactory初始化非延迟单例Beans