# 010-Java-AOP代理模式

[TOC]

## 参考

-  [动态代理](..\..\01-design-patterns\03-structural-patterns\110-动态代理\README.md) 
-  [07-proxy-pattern.md](../../01-design-patterns/03-structural-patterns/07-proxy-pattern.md) 

## 值得注意的是

- JDK代理每个代理允许多个接口进行代理， 所以一般要判断
- 动态代理是凭空创建字节码并且加载的， 在Spring中我们有可能随时会增加新的AOP目标方法拦截， 你不可能吧所有的类进行预先定义好

## 概念

- Advice -> 任何拦截的动作
- Pointcut-> 判断