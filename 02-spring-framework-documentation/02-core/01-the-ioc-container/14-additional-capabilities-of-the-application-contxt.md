# `ApplicationContext`的附加功能

在上面的文章我们知道

-  `org.springframework.beans.factory` 包提供了管理和操作 bean 的基本功能
- `org.springframework.context`提供了[`ApplicationContext`](https://docs.spring.io/spring-framework/docs/5.2.0.RELEASE/javadoc-api/org/springframework/context/ApplicationContext.html)接口,它继承了`BeanFactory`接口,

为了拓展,很多人会使用`ApplicationContext`声明,甚至不手动去创建它,而是依赖`ContextLoader`之类的支持类来自动实例化`ApplicationContext`，作为Java EE web应用程序正常启动过程的一部分。

为了加强`BeanFactory`的功能,并且以更加` framework-oriented `的方式来加强,`context`包提供了以下功能特色:

- 通过`MessageSource`接口来以i18n(国际化)的方式管理消息
- 通过`ResourceLoader`接口来获取资源,类似于 URLs 和文件
- 通过`ApplicationListener`接口和`ApplicationEventPublisher`接口来进行事件发布
- 通过`HierarchicalBeanFactory`接口来加载多重继承结构的 context,让每个都专注于某个特定的层,例如 web 层,可以通过`HierarchicalBeanFactory`接口

## 使用`MessageSource`进行国际化

实际上`ApplicationContext`接口就继承了`MessageSource`,从而提供了国际化支持,`HierarchicalMessageSource`可以在层次上解析消息,包含以下方法:

- `String getMessage(String code, Object[] args, String default, Locale loc)`: 这个基础方法用来从一个`MessageSource`中获取消息,当没有找到指定 locale的消息时,会使用默认的消息,使用标准库提供的“MessageFormat”功能，传入的任何参数都将成为替换值。
- `String getMessage(String code, Object[] args, Locale loc)`:  本质上和上面的方法一致,略微的不同是,没有指定默认的值,如果没有找到值,就会抛出一个 `NoSuchMessageException` 异样
- `String getMessage(MessageSourceResolvable resolvable, Locale locale)`: 这个方法使用的所有属性都被包装到一个类 `MessageSourceResolvable`中

如果一个`ApplicationContext`被初始化,那么它会自动去搜索一个`MessageSource` bean  的定义,这个 bean 的 name 必须为`messageSource`,

- 如果找到相关 bean ,那么`ApplicationContext`相关实现类就使用这个 bean 的方法作为代理
- 如果没有找到相关 bean ,那么容器会初始化`DelegatingMessageSource`作为默认的消息源

### `MessageSource` 的两个实现类

- `ResourceBundleMessageSource`
- `StaticMessageSource`

他们两个都实现了`HierarchicalMessageSource`以便进行嵌套消息传递

`StaticMessageSource`很少使用但是提供了编程方式去往源内添加消息

### `ResourceBundleMessageSource`

下面是一个`ResourceBundleMessageSource`的例子

```xml
<beans>
    <bean id="messageSource"
            class="org.springframework.context.support.ResourceBundleMessageSource">
        <property name="basenames">
            <list>
                <value>format</value>
                <value>exceptions</value>
                <value>windows</value>
            </list>
        </property>
    </bean>
</beans>
```

上面的例子假设你的 classpath 下有三个资源包`format`,`exceptions`和`windows`,任何请求想要通过解析消息,都是通过`ResourceBundle`类JDK标准的方式解析,假设这个三个文件为:

```properties
  # in format.properties
    message=Alligators rock!
```

```properties
   # in exceptions.properties
    argument.required=The {0} argument is required.
```

使用`MessageSource`进行获取

```java
public static void main(String[] args) {
    MessageSource resources = new ClassPathXmlApplicationContext("beans.xml");
    String message = resources.getMessage("message", null, "Default", Locale.ENGLISH);
    System.out.println(message);
}
```

```
Alligators rock!
```

### `ReloadableResourceBundleMessageSource`

Spring 提供了一个`ReloadableResourceBundleMessageSource`,这个变体支持

- 和`ResourceBundleMessageSource`一样的基于 JDK 的文件绑定
- 支持从 Spring任何资源路径(不仅仅是 classpath)下读取文件
- 支持热重载(同时有效地缓存它们)

## 标准和自定义事件

Spring `ApplicationContext`中的事件处理主要涉及到两个接口,使用的是标准的观察者模式

- `ApplicationListener`,实现此接口的 bean 在事件触发后会接到通知
- `ApplicationEvent`,事件

内置的事件

| Event                        | 概述                                                         | Explanation                                                  |
| :--------------------------- | ------------------------------------------------------------ | :----------------------------------------------------------- |
| `ContextRefreshedEvent`      | 上下文初始化或刷新                                           | Published when the `ApplicationContext` is initialized or refreshed (for example, by using the `refresh()` method on the `ConfigurableApplicationContext` interface). Here, “initialized” means that all beans are loaded, post-processor beans are detected and activated, singletons are pre-instantiated, and the `ApplicationContext` object is ready for use. As long as the context has not been closed, a refresh can be triggered multiple times, provided that the chosen `ApplicationContext` actually supports such “hot” refreshes. For example, `XmlWebApplicationContext` supports hot refreshes, but `GenericApplicationContext` does not. |
| `ContextStartedEvent`        | `ApplicationContext`调用 `start()`方法                       | Published when the `ApplicationContext` is started by using the `start()` method on the `ConfigurableApplicationContext` interface. Here, “started” means that all `Lifecycle` beans receive an explicit start signal. Typically, this signal is used to restart beans after an explicit stop, but it may also be used to start components that have not been configured for autostart (for example, components that have not already started on initialization). |
| `ContextStoppedEvent`        | `ApplicationContext`调用 stop 方法时触发,以为这所有 bean 都接收到 stop信号 | Published when the `ApplicationContext` is stopped by using the `stop()` method on the `ConfigurableApplicationContext` interface. Here, “stopped” means that all `Lifecycle` beans receive an explicit stop signal. A stopped context may be restarted through a `start()` call. |
| `ContextClosedEvent`         | `ApplicationContext`调用`close`方法,意味着所有单例的 bean 都被销毁 | Published when the `ApplicationContext` is being closed by using the `close()` method on the `ConfigurableApplicationContext` interface or via a JVM shutdown hook. Here, "closed" means that all singleton beans will be destroyed. Once the context is closed, it reaches its end of life and cannot be refreshed or restarted. |
| `RequestHandledEvent`        | Web 事件,告诉一个 bean.http 请求已经被处理                   | A web-specific event telling all beans that an HTTP request has been serviced. This event is published after the request is complete. This event is only applicable to web applications that use Spring’s `DispatcherServlet`. |
| `ServletRequestHandledEvent` | `RequestHandledEvent`子类,添加了基于 Servlet 相关的上下文信息 | A subclass of `RequestHandledEvent` that adds Servlet-specific context information. |

### 自定义事件

继承 Spring 的`ApplicationEvent`

```java
public class BlackListEvent extends ApplicationEvent {

    private final String address;
    private final String content;

    public BlackListEvent(Object source, String address, String content) {
        super(source);
        this.address = address;
        this.content = content;
    }

    // accessor and other methods...
}
```

调用`ApplicationEventPublisher`的`publishEvent()`方法,可以使用`ApplicationEventPublisherAware`获取`ApplicationEventPublisher`

```java
public class EmailService implements ApplicationEventPublisherAware {

    private List<String> blackList;
    private ApplicationEventPublisher publisher;

    public void setBlackList(List<String> blackList) {
        this.blackList = blackList;
    }

    public void setApplicationEventPublisher(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void sendEmail(String address, String content) {
        if (blackList.contains(address)) {
            publisher.publishEvent(new BlackListEvent(this, address, content));
            return;
        }
        // send email...
    }
}
```

自定义一个`ApplicationEvent`的实现类指定事件来接受

```java
public class BlackListNotifier implements ApplicationListener<BlackListEvent> {

    private String notificationAddress;

    public void setNotificationAddress(String notificationAddress) {
        this.notificationAddress = notificationAddress;
    }

    public void onApplicationEvent(BlackListEvent event) {
        // notify appropriate parties via notificationAddress...
    }
}
```

### 基于注解的事件监听器

不需要实现接口,直接使用`@EventListener`注解

```java
public class BlackListNotifier {

    private String notificationAddress;

    public void setNotificationAddress(String notificationAddress) {
        this.notificationAddress = notificationAddress;
    }

    @EventListener
    public void processBlackListEvent(BlackListEvent event) {
        // notify appropriate parties via notificationAddress...
    }
}
```

指定事件

```java
@EventListener({ContextStartedEvent.class, ContextRefreshedEvent.class})
public void handleContextStart() {
    // ...
}
```

使用 SpEL 表达式指定事件

```java
@EventListener(condition = "#blEvent.content == 'my-event'")
public void processBlackListEvent(BlackListEvent blEvent) {
    // notify appropriate parties via notificationAddress...
}
```

### 异步监听器

使用`@Async` 完成异步处理

```java
@EventListener
@Async
public void processBlackListEvent(BlackListEvent event) {
    // BlackListEvent is processed in a separate thread
}
```

在使用同步事件时有局限性:

- 如果异步监听器抛出了`Exception`,它并不会传播给调用者, 详情请查看`AsyncUncaughtExceptionHandler`

- 异步事件监听器方法不能通过返回值来发布后续事件,只能在手动注入[ApplicationEventPublisher](https://docs.spring.io/spring-framework/docs/5.2.0.RELEASE/javadoc-api/org/springframework/aop/interceptor/AsyncUncaughtExceptionHandler.html)进行手动发布

### 通用事件

你可以使用通用的事件,可以考虑使用`EntityCreatedEvent<T>`,其中泛型 `T`是创建的实际实体类型,例如

```java
@EventListener
public void onPersonCreated(EntityCreatedEvent<Person> event) {
    // ...
}
```

由于类型擦除( type erasure),这种方式仅仅适用于使用 通用的参数的事件,类似于

```java
class PersonCreatedEvent extends EntityCreatedEvent<Person> { …​ }
```

你可以使用`ResolvableTypeProvider`运行时获取类型

```java
public class EntityCreatedEvent<T> extends ApplicationEvent implements ResolvableTypeProvider {

    public EntityCreatedEvent(T entity) {
        super(entity);
    }

    @Override
    public ResolvableType getResolvableType() {
        return ResolvableType.forClassWithGenerics(getClass(), ResolvableType.forInstance(getSource()));
    }
}
```

## 