# RepositoryService 流程存储服务

Java API : [https://www.activiti.org/javadocs/](https://www.activiti.org/javadocs/)

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

## 功能简介

- 管理流程定义文件 xml以及静态的资源
- 对特定流程的暂停和激活
- 流程启动权限管理 : 例如某些用户和用户组才能启动

##API 简介

-  部署文件构造器 `DeploymentBuilder`
-  部署文件查询`DeploymentQuery`
-  流程定义文件查询对象`ProcessDefinitionQuery`
-  流程部署文件对象 `Deployment`
-  流程定义文件对象 `ProcessDefinition`
-  流程定义的 Java 格式 `BpmnModel`

## 测试用户与用户组启动

```java
public class RepositoryServiceTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(RepositoryServiceTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();


    @Test
    @Deployment(resources = "second_approve.bpmn20.xml")
    public void testCandidateStrat() {
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().singleResult();
        LOGGER.info("EC 测试 processDefinitionName: {} ,id ={}", processDefinition.getName(), processDefinition.getId());
        repositoryService.addCandidateStarterUser(processDefinition.getId(), "EricChen");
        repositoryService.addCandidateStarterGroup(processDefinition.getId(), "EC_GROUP");


        //查询用户组和用户对象
        List<IdentityLink> list = repositoryService.getIdentityLinksForProcessDefinition(processDefinition.getId());
        for (IdentityLink link : list) {
            LOGGER.info("identityLink ->{}",link);
        }

    }

}
```


| 返回值类型                     | Method and Description                                       | 描述                          |
| :----------------------------- | :----------------------------------------------------------- | ----------------------------- |
| `void`                         | `activateProcessDefinitionById(String processDefinitionId)`  | 激活指定流程定义              |
| `void`                         | `activateProcessDefinitionById(String processDefinitionId, boolean activateProcessInstances, Date activationDate)` | 激活指定流程定义              |
| `void`                         | `activateProcessDefinitionByKey(String processDefinitionKey)` | 根据流程定义文件中的 Key 激活 |
| `void`                         | `activateProcessDefinitionByKey(String processDefinitionKey, boolean activateProcessInstances, Date activationDate)` | 重载,指定激活时间             |
| `void`                         | `activateProcessDefinitionByKey(String processDefinitionKey, boolean activateProcessInstances, Date activationDate, String tenantId)` | 重载,指定激活时间/租户        |
| `void`                         | `activateProcessDefinitionByKey(String processDefinitionKey, String tenantId)` | 重载,指定租户                 |
| `void`                         | `addCandidateStarterGroup(String processDefinitionId, String groupId)` | 添加候选人组                  |
| `void`                         | `addCandidateStarterUser(String processDefinitionId, String userId)` | 添加候选人                    |
| `void`                         | `addModelEditorSource(String modelId, byte[] bytes)`         | 添加流程定义文件              |
| `void`                         | `addModelEditorSourceExtra(String modelId, byte[] bytes)`    | 添加流程定义未安检            |
| `void`                         | `changeDeploymentTenantId(String deploymentId, String newTenantId)` | 修改租户标识                  |
| `DeploymentBuilder`            | `createDeployment()`                                         | 创建部署对象                  |
| `DeploymentQuery`              | `createDeploymentQuery()`                                    | 查询对象                      |
| `ModelQuery`                   | `createModelQuery()`                                         | 查询模型                      |
| `NativeDeploymentQuery`        | `createNativeDeploymentQuery()`                              | 返回有一个NativeQuery查询队形 |
| `NativeModelQuery`             | `createNativeModelQuery()`                                   | 返回一个流程定义NativeQue对象 |
| `NativeProcessDefinitionQuery` | `createNativeProcessDefinitionQuery()`                       | 返回流程定义查询对象          |
| `ProcessDefinitionQuery`       | `createProcessDefinitionQuery()`                             | 返回流程定义查询对象          |





