# Spring 中的事务管理

- 提供统一的 API 接口支持不同的资源
- 提供声明式的事务管理
- 方便的与 Spring 框架集成
- 多个资源的事务管理、同步

## Spring 事务抽象

- `PlatformTransactionManager` 提供事务管理器的接口,有多个实现,包括提交回滚等操作
- `TransactionDefinition` 事务定义,可以设置事务的属性
- `TransactionStatus` 事务的状态 ,可以设置事务的运行的状态

## 目录结构

## Spring 事务机制



![image-20200101185302901](assets/image-20200101185302901.png)







- 