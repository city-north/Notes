# 020-Singleton-Bean作用域

---
[TOC]

## The Singleton Scope (Singleton 作用域)

Spring容器管理的一个单例 bean,所有根据 ID 获取它的请求,都会只返回这个实例

![singleton](../../assets/singleton.png)

Spring 的单例与四人帮的设计模式里的单例的区别

- Spring 仅仅是容器实例内单例,一个 ClassLoader 可以有多个 Spring 容器实例
- 四人帮的设计模式是一个 ClassLoader 只有一个实例

```xml
<bean id="accountService" class="com.something.DefaultAccountService"/>

<!-- the following is equivalent, though redundant (singleton scope is the default) -->
<bean id="accountService" class="com.something.DefaultAccountService" scope="singleton"/>
```

## 声明单例或者是原型对象

```java
@Bean
// 默认 scope 就是 "singleton"
public static User singletonUser() {
    return createUser();
}

@Bean
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public static User prototypeUser() {
    return createUser();
}

private static User createUser() {
	User user = new User();
	user.setId(System.nanoTime());
	return user;
}
```

## Singleton和Prototype的区别

- 生成对象的差异
  - Singleton Bean 无论依赖查找还是依赖注入，均为同一个对象
  - Prototype Bean 无论依赖查找还是依赖注入，均为新生成的对象

- 如果是集合类型依赖注入
  - 如果依赖注入集合类型的对象，Singleton Bean 和 Prototype Bean 均会存在一个
  - Prototype Bean 有别于其他地方的依赖注入 Prototype Bean

- 初始化或者销毁回调
  - 初始化方法回调 : Singleton、Prototype Bean均会执行
  - 销毁方法:仅 Singleton Bean 会执行销毁方法回调

