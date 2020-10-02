# 历史数据管理服务 HistoryService

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

- 管理流程实例结束后的历史数据
- 构建历史数据的查询对象
- 根据流程实例 Id 删除流程历史数据

##历史数据实体

| 历史数据实体             | 描述                                                |
| ------------------------ | --------------------------------------------------- |
| HistoricProcessInstance  | 历史流程数据实例实体                                |
| HistoricVariableInstance | 流程或任务变量值得实体                              |
| HistoricActivityInstance | 单个活动节点执行的信息                              |
| HistoricTaskInstance     | 用户任务实例的信息                                  |
| HistoricDetail           | 历史流程活动详细信息,Form 属性的信息,变量变化等信息 |

##查询方法

```java
public interface HistoryService {

  /**
   * Creates a new programmatic query to search for {@link HistoricProcessInstance}s.
   */
  HistoricProcessInstanceQuery createHistoricProcessInstanceQuery();

  /**
   * Creates a new programmatic query to search for {@link HistoricActivityInstance}s.
   */
  HistoricActivityInstanceQuery createHistoricActivityInstanceQuery();

  /**
   * Creates a new programmatic query to search for {@link HistoricTaskInstance}s.
   */
  HistoricTaskInstanceQuery createHistoricTaskInstanceQuery();

  /** Creates a new programmatic query to search for {@link HistoricDetail}s. */
  HistoricDetailQuery createHistoricDetailQuery();

  /**
   * Returns a new {@link org.activiti.engine.query.NativeQuery} for process definitions.
   */
  NativeHistoricDetailQuery createNativeHistoricDetailQuery();

  /**
   * Creates a new programmatic query to search for {@link HistoricVariableInstance}s.
   */
  HistoricVariableInstanceQuery createHistoricVariableInstanceQuery();

  /**
   * Returns a new {@link org.activiti.engine.query.NativeQuery} for process definitions.
   */
  NativeHistoricVariableInstanceQuery createNativeHistoricVariableInstanceQuery();

  /**
   * Deletes historic task instance. This might be useful for tasks that are {@link TaskService#newTask() dynamically created} and then {@link TaskService#complete(String) completed}. If the historic
   * task instance doesn't exist, no exception is thrown and the method returns normal.
   */
  void deleteHistoricTaskInstance(String taskId);

  /**
   * Deletes historic process instance. All historic activities, historic task and historic details (variable updates, form properties) are deleted as well.
   */
  void deleteHistoricProcessInstance(String processInstanceId);

  /**
   * creates a native query to search for {@link HistoricProcessInstance}s via SQL
   */
  NativeHistoricProcessInstanceQuery createNativeHistoricProcessInstanceQuery();

  /**
   * creates a native query to search for {@link HistoricTaskInstance}s via SQL
   */
  NativeHistoricTaskInstanceQuery createNativeHistoricTaskInstanceQuery();

  /**
   * creates a native query to search for {@link HistoricActivityInstance}s via SQL
   */
  NativeHistoricActivityInstanceQuery createNativeHistoricActivityInstanceQuery();

  /**
   * Retrieves the {@link HistoricIdentityLink}s associated with the given task. Such an {@link IdentityLink} informs how a certain identity (eg. group or user) is associated with a certain task (eg.
   * as candidate, assignee, etc.), even if the task is completed as opposed to {@link IdentityLink}s which only exist for active tasks.
   */
  List<HistoricIdentityLink> getHistoricIdentityLinksForTask(String taskId);

  /**
   * Retrieves the {@link HistoricIdentityLink}s associated with the given process instance. Such an {@link IdentityLink} informs how a certain identity (eg. group or user) is associated with a
   * certain process instance, even if the instance is completed as opposed to {@link IdentityLink}s which only exist for active instances.
   */
  List<HistoricIdentityLink> getHistoricIdentityLinksForProcessInstance(String processInstanceId);

  /**
   * Allows to retrieve the {@link ProcessInstanceHistoryLog} for one process instance.
   */
  ProcessInstanceHistoryLogQuery createProcessInstanceHistoryLogQuery(String processInstanceId);

}

```