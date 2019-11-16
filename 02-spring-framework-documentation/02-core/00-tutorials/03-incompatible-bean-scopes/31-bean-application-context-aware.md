# 让 Bean 能够感知到 ApplicationContext

> 版权声明
>
> 此篇文章参考于[logicbig](https://www.logicbig.com/),引用其文字/代码/图片仅用于学习笔记,不用于商业用途

为了解决 [[**更小作用域 Bean 注入问题**]](30-injecting-prototype-bean.md) 

我们可以注入 Spring 的 `ApplicationContext`到任何 bean中,通过这种方式,我们可以让这个 bean 能够获取到 `ApplicationContext`的相关信息:

- 通过使用`ApplicationContext.getBean()方法`获取其他 bean
- 通过使用`ApplicationContext.getResource()`来获取资源

使用这个方法,我们可以解决之前章节的 [30-injecting-prototype-bean.md](30-injecting-prototype-bean.md)中的问题,在一个singleton bean 中注入prototype 类型的 bean



## 获取`ApplicationContext`的方式

- 通过`@Autowire`注解注入`ApplicationContext`到任何 bean
- 任何 bean 实现`ApplicationContextAware`接口并实现其中的方法`setApplicationContext`

## 注入 ApplicationContext 的缺点

- 与 SpringAPI 耦合
- 并没有违反 Spring 的 IoC原则,因为我们没有让 Spring 注入依赖,而是我们要求 Spring 提供给我们依赖
- 不推荐此方法解决 Singleton bean 注入 prototype bean 的问题





