# 注入方式

Spring有三种注入方式:

- **基于构造器的注入**:

可以在强制依赖时使用,我们应该将构造器参数指定给 final 修饰的成员变量

- **基于 Setter方法的注入**

用于可选的依赖注入

- **基于`Field`的注入**

Spring不鼓励使用这种方法，因为它可能会将强制字段从外部隐藏起来，否则将在构造函数中分配这些字段。这将剥夺正确初始化POJO的优势，特别是如果打算在Spring容器之外使用的话。尽管在本系列教程中，我们主要使用基于字段的注入来简化我们希望交付的概念，但我们建议开发人员始终避免在实际项目场景中使用基于字段的DI。



![img](https://www.logicbig.com/tutorials/spring-framework/spring-core/types-of-dependency-injection/images/di-types.png)

