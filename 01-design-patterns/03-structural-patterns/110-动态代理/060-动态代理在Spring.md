# 060-动态代理在Spring

[TOC]

- 当 Bean 有实现接口时, JDK代理
- 当 Bean 没有接口时, cglib 代理

![image-20200815221854849](../../../assets/image-20200815221854849.png)

可以看到这个方法有两个分支

- 如果是单例走 getSingletonInstance
- 如果不是 走 newPrototypeInstance 每次请求都会创建一个新的 bean

![image-20200815222116807](../../../assets/image-20200815222116807.png)

