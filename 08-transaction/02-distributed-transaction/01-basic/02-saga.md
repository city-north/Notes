# Saga

## servicecomb-pack 中的saga

Saga处理场景是要求相关的子事务提供事务处理函数同时也提供补偿函数。Saga协调器alpha会根据事务的执行情况向omega发送相关的指令，确定是否向前重试或者向后恢复。

### 成功场景

成功场景下，每个事务都会有开始和有对应的结束事件。

![Successful Scenario](assets/successful_scenario.png)

### 异常场景

异常场景下，omega会向alpha上报中断事件，然后alpha会向该全局事务的其它已完成的子事务发送补偿指令，确保最终所有的子事务要么都成功，要么都回滚。

![Exception Scenario](assets/exception_scenario.png)

### 超时场景 (需要调整）

超时场景下，已超时的事件会被alpha的定期扫描器检测出来，与此同时，该超时事务对应的全局事务也会被中断。

![Timeout Scenario](assets/timeout_scenario.png)