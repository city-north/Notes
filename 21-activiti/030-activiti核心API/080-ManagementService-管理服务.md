# ManagementService

Java Doc :[https://www.activiti.org/javadocs/](https://www.activiti.org/javadocs/)

## 服务列表

| 序号 | Service名称       | 主要作用                                           |
| ---- | ----------------- | -------------------------------------------------- |
| 1    | RepositoryService | 流程部署、流程定义                                 |
| 2    | TaskService       | 对流程实例进行管理和控制                           |
| 3    | IdentityService   | 流程角色数据管理，如用户组、用户以及它们之间的关系 |
| 4    | formService       | 表单服务                                           |
| 5    | RuntimeService    | 在流程运行时对流程实例进行管理与控制               |
| 6    | ManagementService | 对流程引擎进行管理与维护                           |
| 7    | HistoryService    | 流程历史数据、查询、删除                           |
| 8    | DynamicBpmSerivce | 动态流程定义模型修改                               |

## 功能

- 管理 JOb 任务
- 数据库相关通用操作
  - 查询表结构元数据(TableMetaData)
  - 通用表查询(TablePageQuery)
  - 执行自定义 SQL 查询 (executeCustomSql)
- 执行流程引擎命令(Command).方便自定义 Command

##Job查询对象

| 工作查询对象       | 描述               |
| ------------------ | ------------------ |
| JobQuery           | 查询一般工作       |
| TimerJobQuery      | 查询定时工作       |
| SuspendedJobQuery  | 查询中断工作       |
| DeadLetterJobQuery | 查询无法执行的工作 |