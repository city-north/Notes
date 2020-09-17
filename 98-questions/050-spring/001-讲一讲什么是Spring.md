# 讲一讲什么是Spring

Spring是一个轻量级的IoC和AOP容器框架。是为Java应用程序提供基础性服务的一套框架，目的是用于简化企业应用程序的开发，它使得开发者只需要关心业务需求。

常见的配置方式有三种:

- 基于XML的配置
- 基于注解的配置
- 基于Java的配置。

主要由以下几个模块组成:

- Spring Core:核心类库，提供IOC服务;
- Spring Context:提供框架式的Bean访问方式，以及企业级功能(JNDI、定时任务等); Spring AOP:AOP服务;
- Spring DAO:对JDBC的抽象，简化了数据访问异常的处理;
- Spring ORM:对现有的ORM框架的支持;
- Spring Web:提供了基本的面向Web的综合特性，例如多方文件上传;
- Spring MVC:提供面向Web应用的Model-View-Controller实现。