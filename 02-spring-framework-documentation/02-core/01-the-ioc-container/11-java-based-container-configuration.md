# 基于 Java 代码的容器配置方式

使用`@Configuration`注解标识一个配置类,使用`@Bean`注解标识一个主键

```java
@Configuration
public class AppConfig {

    @Bean
    public MyService myService() {
        return new MyServiceImpl();
    }
}
```

与下面 bean 的 xml 注册方式一致

```xml
<beans>
    <bean id="myService" class="com.acme.services.MyServiceImpl"/>
</beans>
```

## 使用`AnnotationConfigApplicationContext`注解来初始化容器

`AnnotationConfigApplicationContext` 不仅仅支持`@Configuration`,也支持`@Component`类和`JSR-330`注解标注的 bean

在使用`@Configuration`注解标注的类的处理上,它将所有这个类中标注`@Bean`的方法当做一个 bean 的注册

在使用`@Component` 注解标注的类的处理上,它会直接将这个看做一个Bean 的定义进行注册,在这个类中的标注有`@Autowired` 和 `@Inject`注解的元数据,进行注入

### 简单构造

`AnnotationConfigApplicationContext`构造起来非常简单:

```java
public static void main(String[] args) {
    ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
    MyService myService = ctx.getBean(MyService.class);
    myService.doStuff();
}
```

### 编程方式注册 bean

使用`register`方法可以直接将 bean 进行注入

```java
public static void main(String[] args) {
    AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
    ctx.register(AppConfig.class, OtherConfig.class);
    ctx.register(AdditionalConfig.class);
    ctx.refresh();
    MyService myService = ctx.getBean(MyService.class);
    myService.doStuff();
}
```

## Spring 对 Web 的支持

注解`AnnotationConfigApplicationContext`的`WebApplicationContext`变体,你可以使用这个实现类来配置`ContextLoaderListener`或者SpringMVC的`DispatcherServlet`等



```xml
<web-app>
    <!-- Configure ContextLoaderListener to use AnnotationConfigWebApplicationContext
        instead of the default XmlWebApplicationContext -->
    <context-param>
        <param-name>contextClass</param-name>
        <param-value>
            org.springframework.web.context.support.AnnotationConfigWebApplicationContext
        </param-value>
    </context-param>

    <!-- Configuration locations must consist of one or more comma- or space-delimited
        fully-qualified @Configuration classes. Fully-qualified packages may also be
        specified for component-scanning -->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>com.acme.AppConfig</param-value>
    </context-param>

    <!-- Bootstrap the root application context as usual using ContextLoaderListener -->
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <!-- Declare a Spring MVC DispatcherServlet as usual -->
    <servlet>
        <servlet-name>dispatcher</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <!-- Configure DispatcherServlet to use AnnotationConfigWebApplicationContext
            instead of the default XmlWebApplicationContext -->
        <init-param>
            <param-name>contextClass</param-name>
            <param-value>
                org.springframework.web.context.support.AnnotationConfigWebApplicationContext
            </param-value>
        </init-param>
        <!-- Again, config locations must consist of one or more comma- or space-delimited
            and fully-qualified @Configuration classes -->
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>com.acme.web.MvcConfig</param-value>
        </init-param>
    </servlet>

    <!-- map all requests for /app/* to the dispatcher servlet -->
    <servlet-mapping>
        <servlet-name>dispatcher</servlet-name>
        <url-pattern>/app/*</url-pattern>
    </servlet-mapping>
</web-app>
```

## 将 Java 配置方式与 XML方式组合

Spring’s `@Configuration` 类可以100%替换掉基于 xml 的声明方式,但是有些时候,例如 Spring xml 的 namespaces ,使用 xml 方式声明依然是明智的,为了可以方便的使用 xml 方式进行声明 bean,你可以使用`@ImportResource`将 xml 进行导入

- 以xml 为中心的使用方式是`ClassPathXmlApplicationContext`
- 以 java 为中心的使用方式是`AnnotationConfigApplicationContext`



