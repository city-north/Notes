# TCC 模式

TCC (try-confirm-cancel)与Saga事务处理方式相比多了一个Try方法。事务调用的发起方来根据事务的执行情况协调相关各方进行提交事务或者回滚事务。

### 成功场景

成功场景下， 每个事务都会有开始和对应的结束事件

![Successful Scenario](assets/successful_scenario_TCC.png)

### 异常场景

异常场景下，事务发起方会向alpha上报异常事件，然后alpha会向该全局事务的其它已完成的子事务发送补偿指令，确保最终所有的子事务要么都成功，要么都回滚。

![exception_scenario_TCC](assets/exception_scenario_TCC.png)

