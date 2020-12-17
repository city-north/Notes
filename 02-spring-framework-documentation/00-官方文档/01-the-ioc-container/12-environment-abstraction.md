# Environment 抽象

`Environment`接口是容器两个关键应用环境的抽象:

- profiles
- properties

`Environment`接口与 profiles 的关系是定义哪些 profile正在激活使用中,哪些是默认激活的

`Environment`接口与 properties 的关系是提供了所有的资源变量的简便配置或者读取方式:

- properties 配置文件
- JVM 系统属性
- System environment variables ,系统环境变量
- JNDI (Java Naming and Directory Interface,Java命名和目录接口)
- Servlet context parameters

- 专门的`Properties`对象,`Map`对象等等

### Profiles

动态根据 Profile 切换不同的 bean

实例

```java
@Configuration
public class AppConfig {

    @Bean("helloWorld")
    @Profile("default")
    public String helloWorldDef(){
        return "helloWorld-default";
    }


    @Bean("helloWorld")
    @Profile("development")
    public String helloWorldDev(){
        return "helloWorld-development";
    }


    @Bean("helloWorld")
    @Profile("production")
    public String helloWorldPro(){
        return "helloWorld-production";
    }
}
```

```java
class Test {
    private static final Logger logger = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) {

        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.getEnvironment().setActiveProfiles("production");
        ctx.register(AppConfig.class);
        ctx.refresh();
        String helloWorld = ctx.getBean("helloWorld", String.class);
        logger.debug("获取到的值为{}", helloWorld);
    }
}
```

输出结果

```
 DEBUG cn.eccto.study.spring_framework.environment.Test - 获取到的值为helloWorld-production
```

除了上面的激活方式.还可以使用

```java
ctx.getEnvironment().setActiveProfiles("profile1", "profile2");
```

或者运行应用时候指定

```java
-Dspring.profiles.active="profile1,profile2"
```

### PropertySource抽象

`Environment`的一个默认实现类是`StandardEnvironment`,它主要包含了两个部分的配置

- `System.getProperties()`
- `System.getenv()`

当使用

```java
ApplicationContext ctx = new GenericApplicationContext();
Environment env = ctx.getEnvironment();
boolean containsMyProperty = env.containsProperty("my-property");
System.out.println("Does my environment contain the 'my-property' property? " + containsMyProperty);
```

查询顺序如下:

1. ServletConfig parameters (if applicable — for example, in case of a `DispatcherServlet` context)
2. ServletContext parameters (web.xml context-param entries)
3. JNDI environment variables (`java:comp/env/` entries)
4. JVM system properties (`-D` command-line arguments)
5. JVM system environment (operating system environment variables)

我们可以使用`@PropertySource`注解指定配置文件

```java
@Configuration
@PropertySource("classpath:/com/myco/app.properties")
public class AppConfig {

    @Autowired
    Environment env;

    @Bean
    public TestBean testBean() {
        TestBean testBean = new TestBean();
        testBean.setName(env.getProperty("testbean.name"));
        return testBean;
    }
}
```

### 语句中的占位符解析

默认情况下会无论`customer`在哪定义,只要他们存在在`Environment`里,就可以这样使用

```xml
<beans>
    <import resource="com/bank/service/${customer}-config.xml"/>
</beans>
```

