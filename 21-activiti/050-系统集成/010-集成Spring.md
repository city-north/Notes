# Activiti 与 Spring 集成

- 集成 Spring 的配置
- 基于 Spring 对 Activiti 进行管理
- 基于 Spring 的流程单元测试

## 集成 Spring 配置

- activiti-spring
- Spring 的默认配置 activiti-context.xml
- Activiti 核心服务注入 Spring 容器

```xml
    <bean id="dataSource" class="com.alibaba.druid.pool.DruidDataSource">
        <property name="url" value="jdbc:h2:mem:activiti"/>
        <property name="driverClassName" value="org.h2.Driver"/>
        <property name="username" value="sa"/>
        <property name="password" value=""/>
    </bean>
    <!-- Spring 事务管理器 -->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <bean id="processEngineConfiguration"
          class="org.activiti.spring.SpringProcessEngineConfiguration">
        <property name="dataSource" ref="dataSource"/>
        <property name="transactionManager" ref="transactionManager"/>
        <property name="databaseSchemaUpdate" value="create-drop"/>
    </bean>

    <bean id="processEngine" class="org.activiti.spring.ProcessEngineFactoryBean">
        <property name="processEngineConfiguration" ref="processEngineConfiguration"/>
    </bean>

    <bean class="org.activiti.engine.test.ActivitiRule">
        <property name="processEngine" ref="processEngine"/>
    </bean>

```

##基于 Spring 对 Activiti 进行管理

- 集成 Spring 的事务管理器
- 在定义流程文件中通过表达式可以获取到所有的 Spring Bean
- 自动部署资源文件: 指定流程定义文件加载到数据库

## 基于 Spring 的流程单元测试

- 添加 pom 依赖 Spring-test
- 辅助测试 Rule : ActivitiRule
- 辅助测试 TestCase: SpringActivitiTestCase

```java
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:activiti-context.xml"})
public class ConfigSpringTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigSpringTest.class);

    @Autowired
    private ActivitiRule activitiRule;

    @Test
    public void test(){
        ProcessEngine processEngine = activitiRule.getProcessEngine();
        processEngine.getRepositoryService()
                .createDeployment()
                .addClasspathResource("second_approve.bpmn20.xml")
                .deploy();
        ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        RuntimeService runtimeService = activitiRule.getRuntimeService();
        RepositoryService repositoryService = activitiRule.getRepositoryService();
        ProcessInstance processInstance = runtimeService
                .startProcessInstanceByKey("second_approve");
        assert (processInstance !=null);
    }

}

```