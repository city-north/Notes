# Environment Profile

> 版权声明
>
> 此篇文章参考于[logicbig](https://www.logicbig.com/),引用其文字/代码/图片仅用于学习笔记,不用于商业用途



>  A profile is a named, logical group of bean definitions to be registered with the container only if the given profile is active.
>
> Profile 是一个命名过的,逻辑上的一组 bean 定义,指定激活 active 一个 profile 就只会注册该组中的 bean

## Profile的应用场景

- 一个应用需要在不同的应用环境下运行时,需要 profile,例如
  - Development 测试环境
  - Test 测试环境
  - Production  生产环境
- 我们需要根据不同的环境选择我们需要的 bean
- 根据不通过的客户定制不同的应用部分,例如根据 不同的 UI

## 什么是 Environment

`Environment`是当前应用运行环境的抽象

主要有两个方面:

- profiles 
- properties

## 值得注意的是

- Java 配置/组件标注`@Profile`后可以设置 profile
- 一个类中的方法可以单独标注`@Profile`

- 我们可以用一个`@Profile`注解多个 profile name

- 我们可以使用两种方式激活一个 profile

  - 启动应用时指定 `--spring.profiles.active`

  ```java
  java -jar spring-boot-02-config-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev
  ```

  - 虚拟机参数指定

  ```java
  -Dspring.profiles.active=dev
  ```

  - `SpringContext.getEnvironment().setActiveProfiles("profile1",profile2)`

- 如果是基于 Servlet 的应用我们可以指定`web.xml`中的 context-param

```xml
<context-param>
    <param-name>spring.profiles.active</param-name>
    <param-value>profile1</param-value>
</context-param>
```

- 如果一个没有标注有`@Profile`的 bean 会被加载到所有的 profile 中

- 我们可以设置一个`default`的 profile,它具有特殊的含义:

  - 如果没有任何 profile, 这个 default就会被加载
  - 如果你激活任何其他 profile, 这个 default 就不会加载
  - 我们可以使用`context.getEnvironment.setDefaultPrifules()`设置

  

