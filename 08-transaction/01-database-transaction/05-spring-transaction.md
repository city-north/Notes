# Spring 中的事务管理

- 提供统一的 API 接口支持不同的资源
- 提供声明式的事务管理
- 方便的与 Spring 框架集成
- 多个资源的事务管理、同步

## Spring 事务抽象

- PlatformTransactionManager 提供事务管理器的接口,有多个实现,包括提交回滚等操作
- TransactionDefinition 事务定义,可以设置事务的属性
- TransactionStatus 事务的状态 ,可以设置事务的运行的状态

### 事务管理器

这是Spring事务基础结构中的中心接口。应用程序可以直接使用它，但它主要不是作为API:通常，应用程序将通过AOP使用TransactionTemplate或声明性事务界定。
对于实现类，建议从提供的`AbstractPlatformTransactionManager`类派生，该类预先实现了定义的传播行为并负责事务同步处理。子类必须为底层事务的特定状态实现模板方法，例如:begin、suspend、resume、commit。

这个策略接口的默认实现是`JtaTransactionManager`和`DataSourceTransactionManager`，它们可以作为其他事务策略的实现指南。

```java
public interface PlatformTransactionManager {
	//获取当前激活的事务
	TransactionStatus getTransaction(@Nullable TransactionDefinition definition) throws TransactionException;
  // 提交
	void commit(TransactionStatus status) throws TransactionException;
	//回滚
	void rollback(TransactionStatus status) throws TransactionException;
}
```

## 事务定义

事务隔离级别机制

```java
public interface TransactionDefinition {
	//事务的传播机制
   int PROPAGATION_REQUIRED = 0;
   int PROPAGATION_SUPPORTS = 1;
   int PROPAGATION_MANDATORY = 2;
   int PROPAGATION_REQUIRES_NEW = 3;
   int PROPAGATION_NOT_SUPPORTED = 4;
   int PROPAGATION_NEVER = 5;
   int PROPAGATION_NESTED = 6;//嵌套事务
  //事务的隔离界别
   int ISOLATION_DEFAULT = -1;//与数据库一致
   int ISOLATION_READ_UNCOMMITTED = Connection.TRANSACTION_READ_UNCOMMITTED; //读未提交
   int ISOLATION_READ_COMMITTED = Connection.TRANSACTION_READ_COMMITTED;//度已提交
   int ISOLATION_REPEATABLE_READ = Connection.TRANSACTION_REPEATABLE_READ;//可重复读
   int ISOLATION_SERIALIZABLE = Connection.TRANSACTION_SERIALIZABLE;//串列话
   int TIMEOUT_DEFAULT = -1;
   int getPropagationBehavior();  //获取事务传播实行
   int getIsolationLevel();//获取事务的隔离界别
   int getTimeout();//超时时间
   boolean isReadOnly();//是否只读
   @Nullable
   String getName();

}
```

## 事务的传播机制

