# 100-依赖查找和注入的Bean会被缓存吗.md

[TOC]

## 依赖查找和注入的Bean会被缓存吗

- 单例Bean（Singleton)会进行缓存
  - 缓存位置 -> 
  - org.springframework.beans.factory.support.DefaultSingletonBeanRegistry#singletonObjects

- 原型Bean(Prototype) 不会

  - 当依赖查询或者依赖注入时， 根据BeanDefinition每次创建

- 其他Scope

  - request : 每个ServletRequest 内部缓存， 生命周期维持在每次HTTP请求
  - session : 每个 HttpSession 内部缓存， 生命周期维持在每个用户HTTP会话
  - application : 当前Servlet 应用内部缓存

  