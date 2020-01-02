# TransactionDefinition 事务定义

## 事务隔离级别机制

```java
public interface TransactionDefinition {
	//事务的传播机制
   int PROPAGATION_REQUIRED = 0; //没有就新建
   int PROPAGATION_SUPPORTS = 1; //调用我的方法有事务,我就在事务里处理,如果没有,我不处理
   int PROPAGATION_MANDATORY = 2;  //
   int PROPAGATION_REQUIRES_NEW = 3; // 无论调用者是否有事务,我都挂起,新建一个事务
   int PROPAGATION_NOT_SUPPORTED = 4;// 即使调用者有事务,我都挂起,我在非事务里执行
   int PROPAGATION_NEVER = 5;//绝不在事务里执行
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

