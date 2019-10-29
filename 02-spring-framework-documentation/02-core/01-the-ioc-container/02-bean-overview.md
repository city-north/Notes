# Bean 概览

上节提到的元数据文件,配置一个 bean,告诉容器这个 bean 应该如何组装, Spring 将 bean 的配置抽象为`BeanDefinition`

一个 `BeanDefinition`包含以下数据:

- 类全名(package-qualified class name )
- Bean 行为配置节点,主要声明 bean 在容器中的行为(scope, lifecycle callbacks 等等)
- 关联 bean 所需要的其他 bean,这些关联称为依赖
- bean 的其他属性

列表:

| Property                 | Explained in…                                                |
| :----------------------- | :----------------------------------------------------------- |
| Class                    | [Instantiating Beans](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-class) |
| Name                     | [Naming Beans](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-beanname) |
| Scope                    | [Bean Scopes](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-scopes) |
| Constructor arguments    | [Dependency Injection](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-collaborators) |
| Properties               | [Dependency Injection](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-collaborators) |
| Autowiring mode          | [Autowiring Collaborators](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-autowire) |
| Lazy initialization mode | [Lazy-initialized Beans](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-lazy-init) |
| Initialization method    | [Initialization Callbacks](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-lifecycle-initializingbean) |
| Destruction method       | [Destruction Callbacks](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-lifecycle-disposablebean) |

## 自定义 bean

`ApplicationContext`允许用户在容器外注册一个对象,

- 通过`ApplicationContext`下的`getBeanFactory()`方法,返回对象是一个`DefaultListableBeanFactory`实现类

- `DefaultListableBeanFactory`实现类的`registerSingleton(..)`方法和`registerBeanDefinition(..)`进行注册



```java
class ApplicationContextTest {
    private static Logger logger = LoggerFactory.getLogger(ApplicationContextTest.class);

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("services.xml");
        ConfigurableListableBeanFactory beanFactory = context.getBeanFactory();
        beanFactory.registerSingleton("helloWorld", String.class);
        logger.debug("get helloWorld {}",context.getBean("helloWorld"));
        logger.debug("get StudentService {}",context.getBean("studentService"));

    }
}
```

## 命名 bean

XML 配置中:

- id 属性:一个 bean 的唯一表示
- name 属性: 可以包含特殊字符,可以使用`,`或者`;`分隔设置多个

命名惯例:

- 头字母小写,其他驼峰,例如`accountManager`,`accountService`等等
- 易于理解,方便在使用 AOP 时根据 name 获取多个 bean 来应用切面

### 别名

通过alias 标签设置别名

```xml
<alias name="fromName" alias="toName"/>
```

如果你有两个 dataSource,可以添加别名

```xml
<alias name="myApp-dataSource" alias="subsystemA-dataSource"/>
<alias name="myApp-dataSource" alias="subsystemB-dataSource"/>
```

## 初始化 bean

Spring 容器会根据配置文件初始化 Bean,如果你是 xml 配置文件,你可以使用`<bean>`标签下的`class`属性

- 指定 `class`类,容器可以通过反射指定构造器创建 bean
- 指定`class`类,包含`static`静态工厂方法去创建一个 bean

- 内部类,我们可以使用嵌套的属性,例如`com.example.SomeThing$OtherThing`,`SomeThing`中的内部类为`OtherThing`

### 通过构造方法创建 bean

以下方式都是使用构造方法创建 bean

```xml
<bean id="exampleBean" class="examples.ExampleBean"/>

<bean name="anotherExample" class="examples.ExampleBeanTwo"/>
```

### 通过静态工厂方法创建 bean

首先在配置文件上加载`factory-method`方法

```xml
<bean id="clientService"
    class="examples.ClientService"
    factory-method="createInstance"/>
```

创建`CloentService`中的`createInstance()`方法

```java
public class ClientService {
    private static ClientService clientService = new ClientService();
    private ClientService() {}

    public static ClientService createInstance() {
        return clientService;
    }
}
```



### 通过一个实例工厂方法创建 bean

在 xml 配置文件中,指定 `factory-bean`和`factory-method`来创建工厂

```xml
<!-- the factory bean, which contains a method called createInstance() -->
<bean id="serviceLocator" class="examples.DefaultServiceLocator">
    <!-- inject any dependencies required by this locator bean -->
</bean>

<!-- the bean to be created via the factory bean -->
<bean id="clientService"
    factory-bean="serviceLocator"
    factory-method="createClientServiceInstance"/>
```

工厂类中添加方法`createClientServiceInstance`

```java

public class DefaultServiceLocator {

    private static ClientService clientService = new ClientServiceImpl();

    public ClientService createClientServiceInstance() {
        return clientService;
    }
}
```

一个工厂类可以拥有多个工厂方法:

```xml
<bean id="serviceLocator" class="examples.DefaultServiceLocator">
    <!-- inject any dependencies required by this locator bean -->
</bean>

<bean id="clientService"
    factory-bean="serviceLocator"
    factory-method="createClientServiceInstance"/>

<bean id="accountService"
    factory-bean="serviceLocator"
    factory-method="createAccountServiceInstance"/>
```

