# 面向切面编程

**AOP (Aspect-Oriented Programming)** :是面向对象编程的一个强大补充，通过AspectJ，我们现在可以把以前分散在应用各处的行为放入可重用的模块中。我们显式地声明在何处如何应用该行为。这有效减少了代码冗余，并让我们关注自身的主要功能。

Spring提供了一个AOP框架，让我们把切面插入到方法执行的周围。

 The examples are Transaction management, logging etc.

## 值得注意的是

- AOP 框架是 spring 框架的核心组成部分,但是Spring 的 IoC 容器并不依赖 SpringAOP
- AOP补充了Spring IoC，提供了一个非常强大的中间件解决方案。

Spring 中的 AOP:

- 提供了声明式的企业服务,最重要的是 **声明式事务管理**
- 让用户实现自定义方面，用AOP补充他们对OOP的使用。

## 目录

- [01-concepts.md](01-concepts.md) 相关概念
- 