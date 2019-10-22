# Customing Bean 定制 Bean

Spring FrameWork 提供一些接口可以让你定制 Bean,他们包括:

- [Lifecycle Callbacks](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-lifecycle) 声明周期回调
- [`ApplicationContextAware` and `BeanNameAware`](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-aware) 两个 Aware 接口
- [Other `Aware` Interfaces](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#aware-list)其他Aware 接口

## Lifecycle Callbacks生命周期回调

通过两种方式接入 Spring 的生命周期

- 实现`InitializingBean` 初始化完成该 bean 后调用`afterPropertiesSet()`
- 实现`DisposableBean` 销毁后调用`destroy()`

### 初始化回调(Initialization Callbacks)

实现`org.springframework.beans.factory.Initializing`接口

```java
void afterPropertiesSet() throws Exception;
```

实例:

```java
public class AnotherExampleBean implements InitializingBean {

    @Override
    public void afterPropertiesSet() {
        // do some initialization work
    }
}
```

上面这种方式和 spring 框架耦合在一起,可以使用下列方式解耦:

```xml
<bean id="exampleInitBean" class="examples.ExampleBean" init-method="init"/>
```

实例

```java
public class ExampleBean {

    public void init() {
        // do some initialization work
    }
}
```



或者使用`@PostConstruct`注解

```java
@Component
public class ExampleBean {
		@PostConstruct
    public void init() {
        // do some initialization work
    }
}
```



### 销毁回调(Destruction Callbacks)

接口`org.springframework.beans.factory.DisposableBean`会在 bean 被销毁时调用`destroy`方法



```java
public class AnotherExampleBean implements DisposableBean {

    @Override
    public void destroy() {
        // do some destruction work (like releasing pooled connections)
    }
}
```

当然这种方式也与 Spring 耦合,所以可以使用注解或者 xml方式:

```xml
<bean id="exampleInitBean" class="examples.ExampleBean" destroy-method="cleanup"/>
```

```java
public class ExampleBean {
	@PostConstruct
    public void cleanup() {
        // do some destruction work (like releasing pooled connections)
    }
}
```

或者使用注解`@PreDestroy`

```
@Component
public class ExampleBean {
		@PreDestroy
    public void cleanup() {
        // do some destruction work (like releasing pooled connections)
    }
}
```



### 组合生命周期机制

##### 初始化执行顺序：

1.  `@PostConstruct`注解声明的方法

1. 实现`InitializingBean`接口 ,重写的`afterPropertiesSet()`方法

1. 自定义的初始化方法，在配置文件里声明

##### 销毁方法的执行顺序

1. `@PreDestroy` 注解声明的方法

1. 实现`DisposableBean `接口 ,重写的`destroy()`方法

1. 自定义的`destroy`方法，在配置文件里声明

### 启动或者关闭回调(Startup and Shutdown Callbacks)

`Lifecycle`接口是一个非常必要的接口，一些需要有自己的生命周期的对象都会实现这个接口。

```
public interface Lifecycle {

    void start();

    void stop();

    boolean isRunning();
}

```
所有Spring 管理的对象都**可以**实现这个接口。当`ApplicationContext`收到`start` 或者`stop`信号，比如在运行时要关闭或者重启的时候，`ApplicationContext`会去调用内部上下文里面实现这个方法的对象。它通过委托给一个生命周期处理器来实现这一点：

```java
public interface LifecycleProcessor extends Lifecycle {

    void onRefresh();

    void onClose();
}
```
它继承了Lifecycle 并且添加了两个方法，刷新`onRefresh`和关闭`onClose`

#### 值得注意的是
- `org.springframework.context.Lifecycle` 仅仅是最简单的开启/关闭通知，不能够实现context刷新时自动启动。

- `org.springframework.context.SmartLifecycle`接口来取代细粒度的自动启动控制（包括启动时），

- `stop` 通知是不能保证在Bean销毁之前调用，在正常的关闭是，Lifecycle bean 会在销毁回调调用之前接收到stop的信号

- 在上下文生命周期内的热刷新或中止的刷新尝试时，只调用destroy方法。

#### Phased 接口
startup 和shatdown 的调用顺序有时候非常重要，如如果一个Bean A 依赖另一个Bean B,那么在启动时，Bean A 会在它的依赖 Bean B 启动后启动。Bean A会在它的依赖B 销毁前销毁。

有些时候，这个顺序并不能直接看出来，这时候就需要一个方法来解决：

你知道一个Bean的优先级大于另一个Bean，`SmartLifecycle`接口给了我们另外一个选择，通过实现`getPhase()`方法来完成

```java
public interface Phased {

    int getPhase();
}
```
```java

public interface SmartLifecycle extends Lifecycle, Phased {

    boolean isAutoStartup();

    void stop(Runnable callback);
}
```

在启动的时候，Spring会默认先启动低等级的Bean,当Stop的时候，则相反。因此，一个实现`SmartLifecycle `接口的对象返回一个`Integer.MIN_VALUE`时会第一个启动且最后一个关闭。

默认的Bean ` getPhase()`为0

#### 自定义一个Stop方法
```java
public interface SmartLifecycle extends Lifecycle, Phased {

    boolean isAutoStartup();

    void stop(Runnable callback);
}
```

我们可以看到，上面代码的stop方法可以调用一个回调，所有的实现类都必要实现这个回调的run方法，这个方法会在实现类的关闭过程中执行，
这样就支持了同步关闭，异步关闭在默认的`LifecycleProcessor`实现类关闭是是很必要的。
 `DefaultLifecycleProcessor,`会等待一个超时时间，在这个超时时间内，所有实现这个接口的对象都会调用这个回调，每个阶段的默认超时时间为30秒.

 你可以通过自定义一个`lifecycleProcessor`的实例来实现个性化需求，但是如果你只想修改超时时间，配置一下更快：


```xml
<bean id="lifecycleProcessor" class="org.springframework.context.support.DefaultLifecycleProcessor">
    <!-- timeout value in milliseconds -->
    <property name="timeoutPerShutdownPhase" value="10000"/>
</bean>
```

#### isAutoStartup
`LifecycleProcessor `会定义一个刷新或者关闭context的回调，这个回调将简单地驱动关机进程，就像显式地调用stop()一样

`SmartLifecycle `接口可以自定义一个refresh方法，如果所有的bean都初始化了，那么会掉这个回调，只有在设置`isAutoStartup` = true的时候，
如果`isAutoStartup`是true,对象会在这个时候自动启动而不是而不是等待context调用它的start()方法

### 非 Web 项目的优雅停机(Shutting Down the Spring IoC Container Gracefully in Non-Web Applications)

需要先注册一个关闭钩子：
`registerShutdownHook()`

```java

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public final class Boot {

    public static void main(final String[] args) throws Exception {
        ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml");

        // add a shutdown hook for the above context...
        ctx.registerShutdownHook();

        // app runs here...

        // main method exits, hook is called prior to the app shutting down...
    }
}
```

如果您在非web应用程序环境中使用Spring的IoC容器;例如，在桌面环境中;您可以向JVM注册一个关机钩子

这样做可以确保优雅地关闭并调用单例bean上的相关销毁方法，以便释放所有资源。当然，您仍然必须正确配置和实现这些销毁回调。

## `ApplicationContextAware` and `BeanNameAware`

#### 运行时Bean自己获取到ApplicationContext

使一个Bean可以在运行时获取到`ApplicationContext`:
实现一个接口：

```java

public interface ApplicationContextAware {

    void setApplicationContext(ApplicationContext applicationContext) throws BeansException;
}

```

例子：

在写默认的实现类时，可以先不注册，在使用的地方获取到`ApplicationContext`然后判断里面有没有自己想要的Bean，如果有，说明客户端完成了自定义，如果没有，使用默认的实现

#### 运行时Bean自己获取到自己的名字
这个回到是在bean在初始化回到之后，例如`InitializingBean afterPropertiesSet`方法后

```java
public interface BeanNameAware {

    void setBeanName(String name) throws BeansException;
}
```

## 其他 Aware 接口

| Name                             | Injected Dependency                                          | Explained in…                                                |
| :------------------------------- | :----------------------------------------------------------- | :----------------------------------------------------------- |
| `ApplicationContextAware`        | Declaring `ApplicationContext`.                              | [`ApplicationContextAware` and `BeanNameAware`](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-aware) |
| `ApplicationEventPublisherAware` | Event publisher of the enclosing `ApplicationContext`.       | [Additional Capabilities of the `ApplicationContext`](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#context-introduction) |
| `BeanClassLoaderAware`           | Class loader used to load the bean classes.                  | [Instantiating Beans](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-class) |
| `BeanFactoryAware`               | Declaring `BeanFactory`.                                     | [`ApplicationContextAware` and `BeanNameAware`](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-aware) |
| `BeanNameAware`                  | Name of the declaring bean.                                  | [`ApplicationContextAware` and `BeanNameAware`](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#beans-factory-aware) |
| `BootstrapContextAware`          | Resource adapter `BootstrapContext` the container runs in. Typically available only in JCA-aware `ApplicationContext` instances. | [JCA CCI](https://docs.spring.io/spring/docs/current/spring-framework-reference/integration.html#cci) |
| `LoadTimeWeaverAware`            | Defined weaver for processing class definition at load time. | [Load-time Weaving with AspectJ in the Spring Framework](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#aop-aj-ltw) |
| `MessageSourceAware`             | Configured strategy for resolving messages (with support for parametrization and internationalization). | [Additional Capabilities of the `ApplicationContext`](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#context-introduction) |
| `NotificationPublisherAware`     | Spring JMX notification publisher.                           | [Notifications](https://docs.spring.io/spring/docs/current/spring-framework-reference/integration.html#jmx-notifications) |
| `ResourceLoaderAware`            | Configured loader for low-level access to resources.         | [Resources](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#resources) |
| `ServletConfigAware`             | Current `ServletConfig` the container runs in. Valid only in a web-aware Spring `ApplicationContext`. | [Spring MVC](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc) |
| `ServletContextAware`            | Current `ServletContext` the container runs in. Valid only in a web-aware Spring `ApplicationContext`. | [Spring MVC](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc) |