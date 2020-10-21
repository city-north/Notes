# Spring的版本和支持

| Spring 版本 | Java标准版 | Java企业版            | 为什么                       | Servlet版本 | 为什么                                                       |
| ----------- | ---------- | --------------------- | ---------------------------- | ----------- | ------------------------------------------------------------ |
| 1.x         | 1.3+       | J2EE 1.3 +            | 动态代理,实现AOP的很重要环节 | 2.3         | 支持Servlet 事件机制,和Spring的事件来呼应,都是Java标准事件的实现 |
| 2.x         | 1.4.2 +    | J2EE 1.3 +            | 支持NIO                      |             |                                                              |
| 3.x         | 5+         | J2EE 1.4 和 Java EE 5 | 引用到大量的注解和枚举       |             |                                                              |
| 4.x         | 6+         | Java EE 6 和 7        | SpringBoot1版本              |             |                                                              |
| 5.x         | 8+         | Java EE 7             | SpringBoot2                  |             |                                                              |

## 3版本非常重要

因为3版本确定了Spring的内核

- 注解驱动
- 事件驱动
- AOP的支持

为什么3版本引入Java5

| 语法特性              | Spring支持版本 | 代表实现                   |
| --------------------- | -------------- | -------------------------- |
| 注解 Annotation       | 1.2 +          | @Transactional             |
| 枚举 Enumeration      | 1.2 +          | Propagation                |
| for-each 语法         | 3.0 +          | AbstractApplicationContext |
| 自动装箱 (AutoBoxing) | 3.0+           |                            |
| 泛型(Generic)         | 3.0+           | ApplicationListener        |

