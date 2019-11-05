# Spring AOP 能力以及目标

## Spring AOP 的特性

SpringAOP 是纯 Java 实现的,不需要特殊的编译过程,Spring AOP 不需要控制 classloader 的层次结构所以它很适合在 servlet 容器中使用

### 切入点支持

- Spring 目前只支持方法切入点
- 字段切入点没有被实现,所以不能用,如果你需要使用字段访问更新切入点,考虑使用 AspectJ

### 目标

Spring AOP 的方法与其他大多数 AOP 框架不同,它的目标是:

- 其目的不是提供最完整的AOP实现(尽管Spring AOP非常强大)
- 其目的是提供AOP实现和Spring IoC之间的紧密集成，以帮助解决企业应用程序中的常见问题。

因此，例如，Spring框架的AOP功能通常与Spring IoC容器一起使用。方面是通过使用普通的bean定义语法来配置的(尽管这允许强大的“自动代理”功能)。

