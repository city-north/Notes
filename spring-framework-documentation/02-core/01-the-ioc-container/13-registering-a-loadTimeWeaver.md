## 注册一个`LoadTimeWeaver`

`LoadTimeWeaver`是一个Spring 使用的向Jvm中动态加载 class 的方法

如果要启动这个机制,可以使用注解`@EnableLoadTimeWeaving`

```java
@Configuration
@EnableLoadTimeWeaving
public class AppConfig {
}
```

或者使用 xml 方式:

```xml
<beans>
    <context:load-time-weaver/>
</beans>
```

一旦为`ApplicationContext`配置以后,任何 其中的bean 都应该实现`ApplicationContext`,从而接收对 load-time weaver 实例的引用,和这个组合非常有用,当要组合  [Spring’s JPA support](https://docs.spring.io/spring/docs/current/spring-framework-reference/data-access.html#orm-jpa)  ,因为 load-time 的织入在 JPA转换时非常必要,参考[`LocalContainerEntityManagerFactoryBean`](https://docs.spring.io/spring-framework/docs/5.2.0.RELEASE/javadoc-api/org/springframework/orm/jpa/LocalContainerEntityManagerFactoryBean.html) 的 javadoc 可以获取更多信息,想了解更多关于 AspectJ load-time weaving ,查看 [Load-time Weaving with AspectJ in the Spring Framework](https://docs.spring.io/spring/docs/current/spring-framework-reference/core.html#aop-aj-ltw).