# 030-RuntimeService-流程运行控制服务

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

## 启动流程的常用方式

- 启动流程以及对流程数据的控制
- 流程实例(ProcessInstance) 与执行流(Execution)的查询
- 通过流程实例可以获取流程的上下文数据
- 通过Execution 执行流,我们可以查询到当前执行到哪一步,可以获取到当前执行的节点 ID,
- 触发流程操作/接收消息和信号

## RuntimeService 启动流程以及变量管理

- 启动流程的常用方式(Id,key,message)
- 每一次 流程的部署,ID 会发生变化,但是 Key 不会发生变化
- 流程可选参数(businessKey[业务唯一标志],variables,tenantId)
- businessKey[业务唯一标志],比如订单号,物流单号等
- variables ,map类型 key->String | value->object
- tenantId 多租户标志,区分流程运行的范围
- 变量(variables)的设置和获取
	- 流程实例级别
	- 执行流级别



## 流程实例与执行流
值得注意的是:
- 流程实例(**ProcessInstance**)表示一次工作流业务的数据实体
- 执行流对象(Execution)表示流程实例中具体的执行路径
	
	- 如果简单的流程只有一条线的话,那么流程实例 ID 和执行流对象 ID 是一致的
- 流程实例接口继承了执行流
	- 在执行流接口的基础上拓展了比如 bussinessKey 等一系列操作
	
	
```java
   /**
     * 测试使用 RuntimeService启动
     */
    @Test
    @Deployment(resources = "second_approve.bpmn20.xml")
    public void testStartProcess() {
        HashMap<String, Object> var = Maps.newHashMap();
        var.put("ecTestKey","ecTestValue");
        RuntimeService runtimeService  = getRuntimeService();
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("second_approve", var);

        //获取执行流
        List<Execution> executions = runtimeService.createExecutionQuery()
                .processInstanceId(processInstance.getProcessInstanceId())
                .listPage(0, 100);
        for (Execution e : executions) {
            LOGGER.info("执行流 -> {}",e);
        }

    }
```

## 流程触发
- 使用 trigger 触发 ReceiveTask 节点
- 触发信号捕获事件 signalEventReceived
	- 信号可以全局发送信号,类似于广播
- 触发消息捕获事件 messageEventReceived
	- 消息只能针对某个流程实例发消息

### 使用 `trigger`触发`ReceiveTask`节点
流程定义文件
```xml
  <process id="ectest" name="ectest" isExecutable="true">
    <startEvent id="start" name="开始"/>
    <sequenceFlow sourceRef="start" targetRef="task1" />
<!--    <userTask id="task1" name="task1name"/>-->
    <receiveTask id="task1"/>
    <sequenceFlow sourceRef="task1" targetRef="end" />
    <endEvent id="end"/>
  </process>
```
可以看到定义了一个 receiveTask 节点,使用 runtimeService.trigger 后,查询不到了该 task,证明已经被触发
```java
    /**
     * 测试使用 trigger
     */
    @Test
    @Deployment(resources = "second_approve.bpmn20.xml")
    public void testTrigger() {
        ProcessInstance instance = getRuntimeService().startProcessInstanceByKey("ectest");
        //获取指定流并触发
        Execution execution = getRuntimeService().createExecutionQuery()
                .activityId("task1")
                .singleResult();
        LOGGER.info("execution -> {}",execution);
        getRuntimeService().trigger(execution.getId());
        //再次查询看看还有没有
        execution = getRuntimeService().createExecutionQuery()
                .activityId("task1")
                .singleResult();

        LOGGER.info("execution -> {}",execution);
    }
```

### 触发信号捕获事件 signalEventReceived
流程定义文件 
```xml
<singnal id= "signalStart" name = "my-signal"/>
  <process id="ectest" name="ectest" isExecutable="true">
    <startEvent id="start" name="开始"/>
    <sequenceFlow sourceRef="start" targetRef="singal-received" />
    <intermediateCatchEvent id="singal-received">
      <signalEventDefinition signalRef="signalStart"/>
    </intermediateCatchEvent>
    <sequenceFlow sourceRef="singal-received" targetRef="end" />
    <endEvent id="end"/>
  </process>
```
使用:
```java
	//触发信号捕获事件
	runtimeService.signalEventReceived("my-signal");
```

### 触发消息捕获事件 messageEventReceived
 必须制定流程 ID 才能触发
 ```xml
 <message id= "messageStart" name = "my-message"/>
  <process id="ectest" name="ectest" isExecutable="true">
    <startEvent id="start" name="开始"/>
    <sequenceFlow sourceRef="start" targetRef="message-received" />
    <intermediateCatchEvent id="message-received">
      <messageEventDefinition signalRef="messageStart"/>
    </intermediateCatchEvent>
    <sequenceFlow sourceRef="message-received" targetRef="end" />
    <endEvent id="end"/>
  </process>
 ```

 ```java
 //触发消息事件
	runtimeService.messageEventReceived("my-message",executionId);
	//触发
 ```

 如果基于消息事件去启动一个流程:
主要是在 startEvent 事件内加入一个消息事件定义,并在前声明一个消息

 ```xml
 <message id= "messageStart" name = "my-message"/>
  <process id="ectest" name="ectest" isExecutable="true">
	<startEvent id = "start">
      <messageEventDefinition signalRef="messageStart"/>
	</startEvent>
    <sequenceFlow sourceRef="start" targetRef="end" />
    <endEvent id="end"/>
  </process>
 ```


  ```java
 //触发消息事件启动流程
	runtimeService.startProcessInstanceByMessage("my-message");
	//触发
  ```

## 常用方法测试

```java


    /**
     * 测试使用 ProcessInstanceBuilder
     */
    @Test
    @Deployment(resources = "second_approve.bpmn20.xml")
    public void testProcessInstanceBuilder() {
        ProcessInstanceBuilder processInstanceBuilder = getRuntimeService()
                .createProcessInstanceBuilder();
        HashMap<String, Object> var = Maps.newHashMap();
        var.put("ecTestKey","ecTestValue");
        ProcessInstance instance = processInstanceBuilder
                .businessKey("process-whatever")
                .processDefinitionKey("second_approve")
                .variables(var)
                .start();
        LOGGER.info("processInstance = {}",instance);
    }

    /**
     * 测试使用 变量获取
     */
    @Test
    @Deployment(resources = "second_approve.bpmn20.xml")
    public void testVarables() {
        ProcessInstanceBuilder processInstanceBuilder = getRuntimeService()
                .createProcessInstanceBuilder();
        HashMap<String, Object> var = Maps.newHashMap();
        var.put("ecTestKey","ecTestValue");
        ProcessInstance instance = processInstanceBuilder
                .businessKey("process-whatever")
                .processDefinitionKey("second_approve")
                .variables(var)
                .start();
        LOGGER.info("processInstance = {}",instance);
        //查询流程执行对象
        Map<String, Object> variables = getRuntimeService().getVariables(instance.getId());
        LOGGER.info("varibale :{}" ,variables);
    }
```