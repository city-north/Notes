# 使用`@DependsOn`控制 bean 的加载顺序

Spring 容器加载 bean 的顺序不能被提前预测,因为 Spring 框架没有一个专门的逻辑来控制初始化的顺序.但是 Spring 确保了 如果 bean A 是 BeanB 的一个依赖(beanA 有一个实例变量`@Autowired B b`) 那么 B 就会先被初始化.

如果我们在没有依赖的情况下控制B先被初始化呢?

## 什么时候想要控制 Spring bean 的初始化顺序

There might be scenarios where A is depending on B indirectly. May be B is supposed to update some global cache outside of B, may be via a singleton pattern and cache is not registered with the Spring container and A is using that cache. Then of course A doesn't want to access the cache if it's not ready. Another scenario: may be A is some sort of events publisher (Or JMS publisher) and B (or may be more beans) are listening to those events. This is a typical scenario of observer pattern. Again, we don't want B to miss any events and would like to have B being initialize first.

In short, there could be many situations where we want B to initialize before A to avoid some kind of side effects. There we should use `@DependsOn` annotation on A indicating to the container that B should be initialize first. Here's an example to demonstrate how that works.\

在有些场景下,A 不直接依赖 B, 也许 B 是为了更新一些B 外部的全局的缓存,也许通过单例模式和缓存没有



![img](assets/depends-on.png)