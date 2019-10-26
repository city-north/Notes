# 组件扫描与组件管理

- 组件扫描
- 组件管理



## 组件扫描

### 什么是组件扫描

自动从 classpath 下扫描备选组件定义的方式,在进行扫描时可以指定过滤的方式,基本可以替换 XML的配置方式

> 从Spring3.0起,Spring 的 JavaConfig 对象已经成为 Spring FrameWork core 的相关的特性,这允许你使用 Java 编码的方式替换掉 xml 的方式,其典型的注解包括:
>
> - `@Configuration`
> - `@Bean`
> - `@Import`
> - `@DependsOn`

### `@Comonent` 和其他模式注解

- `@Comonent`注解标记该类是一个 Spring管理的组件

- `@Repository`注解标记该类是 repostory (或者叫做 Data Access Object or DAO) 角色的组件,自动支持事务回滚

- `@Service`注解标记该类是 Service 层的组件
- `@Controller`标记该类是 是一个控制器组件

### 使用元注解和组合注解

许多由 Spring 管理的注解可以用作 meta-annotations ,例如前面提到的`@Service`注解:

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component 
public @interface Service {

    // ...
}
```

当然你也可以将做个meta-annotations,例如`@RestController`注解就是由`@Controller`+`@ResponseBody`两个注解组合而来

另外,组合注解可以选择重新定义元注解的属性,当你执行暴露元注解的部分属性的时候非常有用,比如:

```java
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Scope(WebApplicationContext.SCOPE_SESSION)
public @interface SessionScope {

    /**
     * Alias for {@link Scope#proxyMode}.
     * <p>Defaults to {@link ScopedProxyMode#TARGET_CLASS}.
     */
    @AliasFor(annotation = Scope.class)
    ScopedProxyMode proxyMode() default ScopedProxyMode.TARGET_CLASS;

}
```

上面的`@SessionScope`写死了`@Scope`注解的`name`,但是还是暴露出了`proxyMode`属性,通过这种方式,你就可以不定义 `proxyMode`属性

```java
@Service
@SessionScope
public class SessionScopedService {
    // ...
}
```

当然,你也可以选择重写:

```java
@Service
@SessionScope(proxyMode = ScopedProxyMode.INTERFACES)
public class SessionScopedUserService implements UserService {
    // ...
}
```

### 自动检测类以及注册 Bean 的定义

使用`@ComponentScan`注解来进行扫描,`basePackages`属性是指定包扫描的根路径:

```java
@Configuration
@ComponentScan(basePackages = "org.example")
public class AppConfig  {
    // ...
}
```

当然,你也可以使用 xml 进行定义

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xmlns:context="http://www.springframework.org/schema/context"
    xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd
        http://www.springframework.org/schema/context
        https://www.springframework.org/schema/context/spring-context.xsd">

    <context:component-scan base-package="org.example"/>

</beans>
```

当使用自动扫描组件的时候,`AutowiredAnnotationBeanPostProcessor`和`CommonAnnotationBeanPostProcessor`两个都被隐式地调用了,这意味着,这两个组件会自动检测并且链接到一起- 所有这些都不需要XML中提供的任何bean配置元数据。

> 如果你想关掉`AutowiredAnnotationBeanPostProcessor`和`CommonAnnotationBeanPostProcessor`
>
> 可以使用`annotation-config`属性设置为`false`

### 使用 Filter 自定义扫描

默认情况下,基于注解的扫描会扫描:

-  `@Component`
-  `@Repository`
-  `@Service`
-  `@Controller`
-  `@Configuration`

- 以及自定义的`@Component`

你可以使用`@ComponentScan`注解的`includeFilters`和`excludeFilters`属性

或者配置文件中的 `<context:include-filter />`和`<context:exclude-filter />`

配置自定义的 Filter,需要设置两个属性`type`和`expression`属性,下面表格描述了过滤相关的属性:

| Filter Type          | Example Expression           | Description                                                  |
| :------------------- | :--------------------------- | :----------------------------------------------------------- |
| annotation (default) | `org.example.SomeAnnotation` | An annotation to be *present* or *meta-present* at the type level in target components. |
| assignable           | `org.example.SomeClass`      | A class (or interface) that the target components are assignable to (extend or implement). |
| aspectj              | `org.example..*Service+`     | An AspectJ type expression to be matched by the target components. |
| regex                | `org\.example\.Default.*`    | A regex expression to be matched by the target components' class names. |
| custom               | `org.example.MyTypeFilter`   | A custom implementation of the `org.springframework.core.type.TypeFilter` interface. |

### 配置实例:

```java
@Configuration
@ComponentScan(basePackages = "org.example",
        includeFilters = @Filter(type = FilterType.REGEX, pattern = ".*Stub.*Repository"),
        excludeFilters = @Filter(Repository.class))
public class AppConfig {
    ...
}
```

XML:

```xml
<beans>
    <context:component-scan base-package="org.example">
        <context:include-filter type="regex"
                expression=".*Stub.*Repository"/>
        <context:exclude-filter type="annotation"
                expression="org.springframework.stereotype.Repository"/>
    </context:component-scan>
</beans>
```

## 组件管理

Spring 的组件也可以将 bean 配置进容器,只要将类配置为`@Configuration`,然后用`@Bean`标注方法:

```java
@Component
public class FactoryMethodComponent {

    @Bean
    @Qualifier("public")
    public TestBean publicInstance() {
        return new TestBean("publicInstance");
    }

    public void doWork() {
        // Component method implementation omitted
    }
}
```

上面的例子可以看出,`FactoryMethodComponent`的组件`doWork`方法是相关的业务逻辑,

`publicInstance`方法,提供了`public` bean 的实例的注册,在注册时候可以使用以下注解:

- `@Bean`
- `@Qualifier`

- `@Scope`
- `@Lazy`

自动注入的属性可以可以使用的,在方法形参上声明指定的`bean`就能够完成注入

```java
@Component
public class FactoryMethodComponent {

    private static int i;

    @Bean
    @Qualifier("public")
    public TestBean publicInstance() {
        return new TestBean("publicInstance");
    }

    // use of a custom qualifier and autowiring of method parameters
    @Bean
    protected TestBean protectedInstance(
            @Qualifier("public") TestBean spouse,
            @Value("#{privateInstance.age}") String country) {
        TestBean tb = new TestBean("protectedInstance", 1);
        tb.setSpouse(spouse);
        tb.setCountry(country);
        return tb;
    }

    @Bean
    private TestBean privateInstance() {
        return new TestBean("privateInstance", i++);
    }

    @Bean
    @RequestScope
    public TestBean requestScopedInstance() {
        return new TestBean("requestScopedInstance", 3);
    }
}
```

上例中,自动注入了 String 类型的方法参数`country`的值为`privateInstance`这个 bean 的`age`属性,直接使用 Spring 的 EL 表达式`#{ <expression> }`进行获取

## 为自动扫描的组件设置Scope

使用`@Scope`注解为bean 组件设置作用域

```java
@Scope("prototype")
@Repository
public class MovieFinderImpl implements MovieFinder {
    // ...
}
```

在扫描时指定 Scope

```java
@Configuration
@ComponentScan(basePackages = "org.example", scopedProxy = ScopedProxyMode.INTERFACES)
public class AppConfig {
    // ...
}
```

## 使用注解`@Qualifier`

使用`@Qualifier`注解对 bean 进行指定

```java
@Component
@Qualifier("Action")
public class ActionMovieCatalog implements MovieCatalog {
    // ...
}
```

