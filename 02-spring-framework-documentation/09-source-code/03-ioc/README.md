# 依赖注入

一些重要的类

## IoC容器

- BeanFactory
- AbstractBeanFactory

## 策略

- SimpleInstantiationStrategy

默认初始化策略

## 依赖注入

- AbstractPropertyAccessor

抽象属性访问器进行依赖注入

## 真正做初始化的类

- BeanWrapper
- BeanDefination

## IOC 容器初始化小结

现在通过上面的代码，总结一下 IOC 容器初始化的基本步骤:

初始化的入口在容器实现中的 refresh()调用来完成。
2、对 Bean 定义载入 IOC 容器使用的方法是 loadBeanDefinition(),
其中的大致过程如下:通过 ResourceLoader 来完成资源文件位置的定位，DefaultResourceLoader 是默认的实现，同时上下文本身就给出了 ResourceLoader 的实现，可以从类路径，文件系统,URL 等 方式来定为资源位置。如果是 XmlBeanFactory 作为 IOC 容器，那么需要为它指定 Bean 定义的资源， 也就是说 Bean 定义文件时通过抽象成 Resource 来被 IOC 容器处理的，容器通过 BeanDefinitionReader 来完成定义信息的解析和 Bean 信息的注册,往往使用的是`XmlBeanDefinitionReader `来解析 Bean 的 XML 定义文件-实际的处理过程是委托给 `BeanDefinitionParserDelegate` 来完成的，从而得到 bean 的定义信息，这些信息在 Spring 中使用 BeanDefinition 对象来表示-这个名字可以让我们想到 `loadBeanDefinition()`,`registerBeanDefinition()` 这些相关方法。它们都是为处理 BeanDefinitin 服务的，容器解析得到 BeanDefinition 以后，需要把 它在 IOC 容器中注册，这由 IOC 实现 `BeanDefinitionRegistry` 接口来实现。注册过程就是在 IOC 容器 内部维护的一个 HashMap 来保存得到的 `BeanDefinition` 的过程。这个 HashMap 是 IOC 容器持有 Bean 信息的场所，以后对 Bean 的操作都是围绕这个 HashMap 来实现的。
然后我们就可以通过 BeanFactory 和 ApplicationContext 来享受到 Spring IOC 的服务了,在使用 IOC 容器的时候，我们注意到除了少量粘合代码，绝大多数以正确 IOC 风格编写的应用程序代码完全不用关 心如何到达工厂，因为容器将把这些对象与容器管理的其他对象钩在一起。基本的策略是把工厂放到已 知的地方，最好是放在对预期使用的上下文有意义的地方，以及代码将实际需要访问工厂的地方。Spring 本身提供了对声明式载入 web 应用程序用法的应用程序上下文,并将其存储在 ServletContext 中的框架 实现。

## ![一步一步手绘Spring IOC运行时序图](assets/一步一步手绘Spring IOC运行时序图.jpg)