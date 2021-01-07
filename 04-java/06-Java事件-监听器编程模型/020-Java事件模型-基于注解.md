# 020-Java事件模型-基于注解

[TOC]

## 面向注解的事件-监听器设计模式

| Java技术规范 | 事件注解                       | 监听器注解                                     |
| ------------ | ------------------------------ | ---------------------------------------------- |
| Servlet3.0+  |                                | @java.servlet.annotation.WebListener(注解扫描) |
| JPA1.0       | @javax.persistence.PostPersist |                                                |
| Java Common  | @PostConstruct                 |                                                |
| EJB 3.0      | @javax.ejb.PrePassivate        |                                                |
| JSF 2.0+     | @javax.faces.event.Listener    |                                                |

