# 001-Spring特性总览

[TOC]

# 核心特性（Core） 

- IoC 容器（IoC Container） 

- Spring 事件（Events） 

- 资源管理（Resources） 

- 国际化（i18n） 

- 校验（Validation） 

- 数据绑定（Data Binding） 

- 类型装换（Type Conversion） 

- Spring 表达式（Spring Express Language） 

- 面向切面编程（AOP）

## 数据存储（Data Access） 

• JDBC 

• 事务抽象（Transactions） 

• DAO 支持（DAO Support） 

• O/R映射（O/R Mapping） 

• XML 编列（XML Marshalling）

> 我们可以理解编列就是序列化 Object -> XML ， 反序列化就是反编列  (XML -> Object)

## Web技术

### Web 技术（Web） 

• Web Servlet 技术栈 

• Spring MVC 

• WebSocket 

• SockJS 

### Web Reactive 技术栈 

• Spring WebFlux 

• WebClient 

• WebSocket

## 技术整合（Integration） 

• 远程调用（Remoting） 

• Java 消息服务（JMS） 

• Java 连接架构（ JCA） 

• Java 管理扩展（JMX） 

• Java 邮件客户端（Email） 

• 本地任务（Tasks） 

• 本地调度（Scheduling） 

• 缓存抽象（Caching） 

• Spring 测试（Testing）

## 技术整合（Integration） 

• 远程调用（Remoting） ： Spring做了抽象， 无论是HTTP远程调用还是基于Hessian远程调用

• Java 消息服务（JMS） 

• Java 连接架构（ JCA） ： 统一Java资源链接， 例如JDBC

• Java 管理扩展（JMX）

• Java 邮件客户端（Email） 

• 本地任务（Tasks） 

• 本地调度（Scheduling） 

• 缓存抽象（Caching） 

• Spring 测试（Testing）

## 测试（Testing） 

• 模拟对象（Mock Objects） 

• TestContext 框架（TestContext Framework） 

• Spring MVC 测试（Spring MVC Test） 

• Web 测试客户端（WebTestClient）