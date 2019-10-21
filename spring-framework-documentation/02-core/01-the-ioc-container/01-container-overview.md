# 容器

IoC 又叫依赖注入,对象声明他们的依赖,然后容器帮他们注入这些依赖,所以叫依赖注入

一般注入方式有:

- 构造器
- 工厂方法参数
- set 方法

- Spring 提供两个包完成 Spring 的 IoC 容器:

`org.springframework.beans`和`org.springframework.context`

- 两个类重要的接口:
  - `BeanFactory`,提供一个高级配置机制,该机制可以管理任何类型的对象
  - `ApplicationContext`是一个`BeanFactory`的子接口,添加了 AOP 支持,消息源处理,事件发布,以及应用级别的 context,例如`WebApplicationContext`

## 容器的配置方式

`org.springframework.context.ApplicationContext`代表了 Spring IoC  容器并且负责初始化、配置和组装 Bean,但是 它怎么知道如何配置?你需要通过元数据(metadata)告诉它

三种元数据配置方式:

- XML
- Java annotations
- Java code

### 图示



![container magic](assets/container-magic.png)

### 配置元数据(Configration Metadata)

- 基于 xml 的配置

- 基于注解的配置:Spring2.5 以上支持,通过注解配置
- 基于 Java 代码的配置: Spring3.0以上支持,支持`@Configuration`,`@Bean`,`@Import`,`@DependsOn`

## 初始化容器

`ApplicationContext`构造器是一个 String 资源,通过指定外部配置文件初始化,`classpath`或者是 `file`文件系统

可以通过读取配置文件的方式初始化`ApplicationContext`

```java
ApplicationContext context = new ClassPathXmlApplicationContext("services.xml", "daos.xml");
```



### 组合两个 xml 文件

```
<beans>
    <import resource="services.xml"/>
    <import resource="resources/messageSource.xml"/>
    <import resource="/resources/themeSource.xml"/>

    <bean id="bean1" class="..."/>
    <bean id="bean2" class="..."/>
</beans>
```

### 