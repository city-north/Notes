# Understanding Spring Boot Web MVC Auto Configuration

```java
@Configuration
@ConditionalOnWebApplication
@ConditionalOnClass({ Servlet.class, DispatcherServlet.class, WebMvcConfigurerAdapter.class })
@ConditionalOnMissingBean(WebMvcConfigurationSupport.class)
@AutoConfigureOrder(Ordered.HIGHEST_PRECEDENCE + 10)
@AutoConfigureAfter({ DispatcherServletAutoConfiguration.class,	ValidationAutoConfiguration.class })
public class WebMvcAutoConfiguration {
 ...
}
```

1. `Servlet`, `DispatcherServlet` and `WebMvcConfigurer` classes must be present on the classpath. If the dependency of spring-boot-starter-web has been included in the project then all of these classes will be present.
2. The application context being used must be web application context. When we start spring boot application via `SpringApplication` class, `AnnotationConfigEmbeddedWebApplicationContext` is initialized as ApplicationContext only if `ConfigurableWebApplicationContext` is on the classpath (included when used spring-boot-starter-web), otherwise `AnnotationConfigApplicationContext` is initialized.
3. `WebMvcConfigurationSupport` must not already be registered as bean. In a typical web MVC application, this class is registered when we use `@EnableWebMvc`.

## Adding additional Spring Web MVC Components

如果你需要添加新增的 MVC配置 (interceptors, formatters, view controllers etc.),我们可以添加自己的`WebMvcConfigurerAdapter`类并标注@Configuration ([example here](https://www.logicbig.com/tutorials/spring-framework/spring-boot/custom-formatter.html)).

## Where to find Boot MVC Configurations?

>  It's good to know where Spring Boot does MVC related beans registration and configuration so that we can investigate and find answers if they are not available in the documentations. Following are two important classes:

> `WebMvcAutoConfigurationAdapter` is a nested configuration class in `WebMvcAutoConfiguration` which extends `WebMvcConfigurerAdapter` and has Boot specific necessary beans registration for Web MVC.

- `WebMvcAutoConfigurationAdapter`  是一个`WebMvcAutoConfiguration`的嵌套配置类
- `WebMvcAutoConfigurationAdapter` 继承了`WebMvcConfigurerAdapter`注册了 Web MVC 的必要 bean

>  The other nested configuration class we should know is `EnableWebMvcConfiguration`. This class extends `DelegatingWebMvcConfiguration` which is capable of discovering more WebMvcConfigurer (needed for application side configuration as stated in the last section).

- `EnableWebMvcConfiguration` 继承自`DelegatingWebMvcConfiguration`,拥有更多 WebMvcConfigurer