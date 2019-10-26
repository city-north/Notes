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

未完待续https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#context-functionality-messagesource

