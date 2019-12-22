# Spring Cloud Context 

Application Context Services 

- Spring Cloud 基于 Spring Boot 开发
- Spring Cloud 另外构建了一些所有组件都有可能使用到的特性

## [The Bootstrap Application Context](https://cloud.spring.io/spring-cloud-static/Hoxton.RELEASE/reference/htmlsingle/#the-bootstrap-application-context)

- bootstrap context 是main 应用上下文的**父**上下文
- 负责从外部资源加载配置文件
- 解密从本地外部化配置文件的属性
- 两个上下文共享同一个`Environment`
- 默认情况下,bootstrap 属性(这里说的不是`bootstrap.properties`中 的属性,而是在引导(boostrap)阶段加载的属性)加载最高优先级,本地的配置没有办法覆盖

bootstrap 上下文使用与main应用程序上下文不同的约定来定位外部配置,

- Bootstrap : `bootstrap.yml` 和 `bootstrap.properties`文件
- main 应用程序: `application.yml` 和`application.properties`

下面是 bootstrap.yml 示例:

```yaml
spring:
  application:
    name: foo
  cloud:
    config:
      uri: ${SPRING_CONFIG_URI:http://localhost:8888}
```

- 使用`spring.cloud.bootstrap.enabled=false`关闭 bootstrap 流程

## [ Application Context Hierarchies](https://cloud.spring.io/spring-cloud-static/Hoxton.RELEASE/reference/htmlsingle/#application-context-hierarchies):应用程序上下文层次结构

- 如果你使用的是 `SpringApplication`和`SpringApplicationBuilder`来构建应用上下文,那么Bootstrap 上下文是 main 上下文的父
- Spring子上下文继承父上下文的 `property source`(属性源) 和` profiles`

所以不使用 Spring Cloud Config 的上下文缺少以下属性源

- "bootstrap":如果在 Bootstrap 上下文对象中找到了`PropertySourceLocators`实例并且其中有非空的属性.或者`CompositePropertySource`以更高权限出现,那么这个应用的属性来自于Spring Cloud Config Server
- "applicationConfig":[`classpath:bootstrap.yml`] .从配置文件`bootstrap.yml`中读取,或者指定配置文件,会配置到 Bootstrap 上下文中,优先级低于`application.yml`

因为属性源的排序,"bootstrap"有优先性,但是注意这个 bootstrap 上下阿文不包含任何来自`bootstrap.yml`的文本,`bootstrap.yml`中的属性优先级非常低,用来设置一些默认值

可以使用以下方法设置一个上下文的父

```java
SpringApplicationBuilder.child()
SpringApplicationBuilder.parent()
SpringApplicationBuilder.sibling()
```

BootStrap 上下文默认是其他上下文的父,子上下文的属性会覆盖父上文内的属性

## [Changing the Location of Bootstrap Properties](https://cloud.spring.io/spring-cloud-static/current/reference/htmlsingle/#customizing-bootstrap-properties)

你应用中加入 bootstrap 上下文的属性通常是 remote 的类型的,例如从 Spring Cloud Config Server.默认情况下他们不能本地覆盖,如果你想让本地属性源覆盖远程的属性,那么远程服务器 IXUS 要矛权限,设置为

```
spring.cloud.config.allowOverride=true
```

你本地设置这些属性是没用的,必须要在配置服务器上设置

一旦设置,两个更细粒度的设置控制远程属性相对于系统属性和应用程序的本地配置的位置

- `spring.cloud.config.overrideNone=true`: 允许通过任何属性文件覆盖配配置
- `spring.cloud.config.overrideSystemProperties=false`:只允许系统属性,命令行参数,环境变量(非本地配置文件) 可以覆盖远程的设置 



## [Changing the Location of Bootstrap Properties](https://cloud.spring.io/spring-cloud-static/current/reference/htmlsingle/#customizing-bootstrap-properties):修改 Bootstap 地址的位置

你可以使用以下属性设置`bootstrap.yml`或者`bootstrap.properties`属性

```
spring.cloud.bootstrap.name:bootstrap
spring.cloud.bootstrap.location:
```



## [Customizing the Bootstrap Configuration](https://cloud.spring.io/spring-cloud-static/current/reference/htmlsingle/#customizing-the-bootstrap-configuration):自定义 Bootstrap 配置

配置文件`/META-INF/spring.factories`中的`org.springframework.cloud.bootstrap.BootstrapConfiguration`,

自己声明一个 Bean ,实现`ApplicationContextInitializer`,并注册到该配置文件中

执行顺序:

- 首先 bootstrap 根据配置文件`spring.factories`上下文创建
- 然后所有的`ApplicationContextInitializer`都会 在 SpringApplication 启动前被加入到 `SpringApplication`

```java
**
 * 自定义 Bootstrap 配置
 *
 * @author EricChen 2019/12/15 10:49
 */
public class MyBootstrapConfiguration implements ApplicationContextInitializer {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        // 从 ConfigurableApplicationContext 获取 ConfigurableEnvironment 实例
        ConfigurableEnvironment environment = applicationContext.getEnvironment();
        // 获取 PropertySources
        MutablePropertySources propertySources = environment.getPropertySources();
        // 定义一个新的 PropertySource，并且放置在首位
        propertySources.addFirst(createPropertySource());
    }

    private PropertySource<?> createPropertySource() {
        Map<String, Object> source = new HashMap<>();
        source.put("name", "EricChen");
        PropertySource propertySource = new MapPropertySource("my-property-source", source);
        return propertySource;
    }
}

```

![image-20191217232311478](assets/image-20191217232311478.png)

## [Customizing the Bootstrap Property Sources](https://cloud.spring.io/spring-cloud-static/current/reference/htmlsingle/#customizing-bootstrap-property-sources):自定义 Bootstrap 属性源

默认情况下,默认外部化配置的默认属性源是在 bootstrap 流程下添加到 `SpringCloud Config Server `的,当然你也可以自定义添加一个源:

- 声明一个`PropertySourceLocator`类型的 bean
- 在`spring.factories`配置文件中配置

```java
/**
 * 自定义 属性源
 *
 * @author EricChen 2019/12/15 11:05
 */
public class CustomPropertySourceLocator implements PropertySourceLocator {

    @Override
    public PropertySource<?> locate(Environment environment) {
        return new MapPropertySource("customProperty",
                Collections.<String, Object>singletonMap("property.from.sample.custom.source", "worked as intended"));
    }

}

```

- 形参environment 和将要创建 ApplicationContext 的形参是同一个

![image-20191217232311478](assets/image-20191217232311478.png)

## [Environment Changes](https://cloud.spring.io/spring-cloud-static/current/reference/htmlsingle/#environment-changes)

应用会监听事件`EnvironmentChangeEvent`,监听到后会使用标准方式进行更改(用户可以自定义一个的`ApplicationListeners`),如果事件是`EnvironmentChangeEvent`,这个事件里包含一个属性改变 key 的列表,用来

- 重新绑定上下文`@ConfigurationProperties`bean
- 设置日志等级,如果这个属性包含`logging.level.*`

注意，默认情况下，Config客户端不会轮询`Environment`中的更改,我们不推荐使用轮询的方式(通过`@Scheduled`注解)如果你在集群应用中,更好的方式是使用广播的方式将事件`EnvironmentChangeEvent`发送到各个应用客户端,(可以使用 Spring Cloud Bus)

`EnvironmentChangeEvent`事件覆盖了很多类的刷新,只要你修改了 Environment 并且发送了这个事件,你就可以使用`/configprops` (endpoint)到标注有`@ConfigurationProperties`的类上去,

例如`DataSource`这个类有一个属性`maxPoolSize`,在运行时被修改了(`DataSource`默认由 SpringBoot 创建的`@ConfigurationProperties`bean) 可以动态的修改这个属性,重新绑定`@ConfigurationProperties`不能覆盖另一个大的用例类，在这个用例类中，您需要对刷新进行更多的控制，并且需要对整个`ApplicationContext`进行原子性的更改。为了解决这些问题，我们有`@RefreshScope`

## [ Refresh Scope](https://cloud.spring.io/spring-cloud-static/current/reference/htmlsingle/#refresh-scope)

A bean that marked`@refreshedScope` gets special treatments,这些bean 如果是有状态的,那么就必须是已经初始化好的,例如如果一个`Datasource`在被修改URL 之前就已经有打开的链接,你可能想先关闭这些已经打开的 connections ,然后档下一次有从连接池获取链接的时候,就会出现一个新的URL

如果一个 bean 只初始化一次,那么使用`@RefreshScope`的同事扼要设置一个属性`spring.cloud.refresh.extra-refreshable`指定一个雷小明

如果你要暴露`/refresh`端点:

```yml
management:
  endpoints:
    web:
      exposure:
        include: refresh
```

## [Endpoints](https://cloud.spring.io/spring-cloud-static/current/reference/htmlsingle/#endpoints)

一些除了 SpringBoot 默认的端点外的其他端点:

- `POST` to `/actuator/env` to update the `Environment` and rebind `@ConfigurationProperties` and log levels.
- `/actuator/refresh` to re-load the boot strap context and refresh the `@RefreshScope` beans.
- `/actuator/restart` to close the `ApplicationContext` and restart it (disabled by default).
- `/actuator/pause` and `/actuator/resume` for calling the `Lifecycle` methods (`stop()` and `start()` on the `ApplicationContext`).