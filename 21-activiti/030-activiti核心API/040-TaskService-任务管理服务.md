# TaskService 任务管理服务
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

## TaskService
- 对用户任务(UserTask)管理和流程的控制
- 设置用户任务(UserTask)的权限消息(拥有者,候选人,办理人)
-  针对用户任务添加任务附件、任务评论和事件记录

## TaskService 对 Task 管理与流程控制
-  Task对象的创建与删除
	-  一般 Task流程一般由流程定义里面的执行过程中,流转到 UserTask 的时候,会通过 TaskService 创建一个 Task对象
	-  一般不手工创建或者删除,当我们流程流转到下一个节点的时候,上一个节点就会被 TaskService 删除
- 查询 Task,并驱动 Task 节点完成执行
- Task 相关参数变量(Variable)设置 ,包括本地变量和全局变量

```xml
    <process id="ec_test" name="我的测试流程" isExecutable="true">

        <startEvent id="start_event" name="开始"/>
        <sequenceFlow sourceRef="start_event" targetRef="myTask"/>
        <userTask id="myTask" name="myTask" activiti:candidateUsers="user1,user2,user3">
            <documentation> 测试变量是否替换 ${message} </documentation>
        </userTask>
        <sequenceFlow sourceRef="myTask" targetRef="end"/>
        <endEvent id="end"/>
    </process>

```

```java
    /**
     * 测试变量替换
     */
    @Test
    @Deployment(resources = {"cn/eccto/activiti/test/api/task_service_variable_bpmn20.xml"})
    public void testVarableReplace() {
        HashMap<String, Object> var = Maps.newHashMap();
        var.put("message","这是一个测试信息");
        var.put("key1", "ecTestValue");
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance instance = runtimeService.startProcessInstanceByKey("ec_test",var);
        TaskService taskService = activitiRule.getTaskService();
        List<Task> list = taskService.createTaskQuery().list();
        for(Task task : list){
            LOGGER.info("task : {}" , ToStringBuilder.reflectionToString(task,ToStringStyle.JSON_STYLE));
            LOGGER.info("task description : {}",task.getDescription());
        }
        
```
测试输出:
![](https://www.showdoc.cc/server/api/common/visitfile/sign/01fecb1d4a572393c1a82258e302d3ed?showdoc=.jpg)
可以看到在流程定义文件中的通配符被替换掉了
```java

        //可以获取流程变量+本地 Task 变量
        taskService.getVariables(taskId);
        //只能获取到本地 Task 变量
        taskService.getVariablesLocal(taskId);
        //只能获取流程变量
        runtimeService.getVariables();
```


##TaskService 本地变量类型以及可见性

- 本次Task 完成后,后续节点依然可以获取到变量
- 如果设置变量可见性为 local ,那么后续节点将无法获取到该变量
```java
    /**
     * 测试变量可见性
     */
    @Test
    @Deployment(resources = {"cn/eccto/activiti/test/api/task_service_variable2_bpmn20.xml"})
    public void testVarable2() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance instance = runtimeService
                .startProcessInstanceByKey("ec_test");
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        //测试全局变量
        taskService.setVariable(task.getId(),"public","publicV");
        //测试本地变量
        taskService.setVariableLocal(task.getId(),"local","localV");

        Map<String, Object> variables = taskService.getVariables(task.getId());
        Map<String, Object> variablesLocal = taskService.getVariablesLocal(task.getId());

        Map<String, Object> variables1 = runtimeService.getVariables(task.getExecutionId());
        Map<String, Object> processVariables = instance.getProcessVariables();
        LOGGER.info("到达userTask1 后获取变量{}",variables);
        LOGGER.info("到达userTask2 后获取本地变量 {}",variablesLocal);
        LOGGER.info("到达userTask2 后获取Runtime 执行流对应的变量 {}",variables1);
        LOGGER.info("到达userTask2 后获取流程实例变量{}",processVariables);

        taskService.complete(task.getId());
        Task userTask2 = taskService.createTaskQuery().singleResult();
        Map<String, Object> variables2 = taskService.getVariables(userTask2.getId());
        Map<String, Object> variablesLocal2 = taskService.getVariablesLocal(userTask2.getId());

        Map<String, Object> variables22 = runtimeService.getVariables(userTask2.getExecutionId());
        Map<String, Object> processVariables2 = instance.getProcessVariables();
        LOGGER.info("到达userTask2 后获取变量{}",variables2);
        LOGGER.info("到达userTask2 后获取本地变量 {}",variablesLocal2);
        LOGGER.info("到达userTask2 后获取Runtime 执行流对应的变量 {}",variables22);
        LOGGER.info("到达userTask2 后获取流程实例变量{}",processVariables2);
    }


```
![](https://www.showdoc.cc/server/api/common/visitfile/sign/353eccd04865be10af8198d4142ecb81?showdoc=.jpg)



## TaskService 设置 Task 权限信息
包括:
- 候选用户(candidateUser) 和候选组(candidateGroup)
- 指定拥有人(Owner)和办理人(Assignee)
- 通过 claim 设置办理人


```java

taskService.setOwner(task.getId(),"user1");//设置拥有人
taskService.setAssignee(task.getId(),"jimmy");//设置办理人,不推荐,因为不会做当前办理人的校验而直接设置
taskService.claim(task.getId(),"jimmy");//calim 方法如果发现当前任务已经指定了办理人,而且不是当前办理人那么就会报错
```

## TaskService 设置 Task附加信息
- 任务附件(Attachment)创建与查询
-  任务评论(Comment)创建与查询

```java

//	创建一个附件  (类型,taskId,名称,附件)
taskService.createAttachment("url",task.getId()),"desc","/url/pic.png");
taskSerivec.addComment(String taskId, String processInstanceId, String message)//
默认的 type ="comment"
taskSerivec.addComment(String taskId, String processInstanceId, String type, String message)
//
```

##TaskEvent 会记录对 Task的所有操作
```java

    @Test
    @Deployment(resources = {"cn/eccto/activiti/test/api/task_service_comment_bpmn20.xml"})
    public void testComment() {
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        ProcessInstance instance = runtimeService
                .startProcessInstanceByKey("ec_test");
        TaskService taskService = activitiRule.getTaskService();
        Task task = taskService.createTaskQuery().singleResult();
        //存储 Comment
        taskService.addComment(task.getId(),task.getProcessInstanceId(),"comment-1");
        taskService.addComment(task.getId(),task.getProcessInstanceId(),"comment-2");

        //event 会记录包括 Comment 以内的其他所有操作

        taskService.createAttachment("url",
                task.getId(),
                task.getProcessInstanceId(),
                "my-attchment",
                "my-attachment-desc",
                "/url/test");

        taskService.setOwner(task.getId(),"EricChen");
        //获取评论
        List<Comment> taskComments = taskService.getTaskComments(task.getId());
        for(Comment comment : taskComments){
            LOGGER.info("task comment -> {}",ToStringBuilder.reflectionToString(comment,ToStringStyle.JSON_STYLE));

        }
        List<Event> taskEvents = taskService.getTaskEvents(task.getId());
        for(Event event : taskEvents){
            LOGGER.info("task event -> {}" ,ToStringBuilder.reflectionToString(event,ToStringStyle.JSON_STYLE));
        }
    }
```

输出:
![](https://www.showdoc.cc/server/api/common/visitfile/sign/53ac5d30c2b9fbf3d08c02371abe368d?showdoc=.jpg)