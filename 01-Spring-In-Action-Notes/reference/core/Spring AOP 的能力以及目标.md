# Spring AOP 的能力以及目标

Spring AOP 有一下能力:

- 由纯 Java 实现,不需要其他额外流程编译
- 不需要控制类加载器层次结构，因此适合在servlet容器或应用程序服务器中使用。

Spring AOP 的目标

- 目前只支持方法切入点,不支持属性切入点(可以通过 AspectJ 实现)
- Spring AOP 和其他大部分 AOP 框架不同,他的目标不是提供完整的 AOP 实现,其目的是在AOP实现和Spring IoC之间提供一个紧密的集成

## 值得注意的是

- Spring Framework 的一个信条就是无侵入式,在业务开发时,不应该强制地将框架内的接口或者类引入到你的业务逻辑或实体模型中