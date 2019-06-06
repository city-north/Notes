# DispatcherSevlet

SpringMVC 与其他 Web 框架一样,围绕着控制器模式进行设计的,其核心为 Servlet为`DispatcherServlet`,提供一个用于请求处理的共享算法,而实际工作是由可配置的委托组件执行的.该模型灵活,支持多种工作流



`DispatcherServlet`说到底是一个`Servlet`,它需要使用Java配置或`web.xml`根据Servlet规范声明和映射。

- 使用 Java代码方式配置
- 在`web.xml`中进行声明配置



## 使用 Java 代码初始化一个 DispactherServlet

下面这个例子是使用 Java 配置的方式进行注册和初始化`DispacterServlet`

```java
public class MyWebApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext servletCxt) {

        // Load Spring web application configuration
       // 加载 Spring web application configuration
        AnnotationConfigWebApplicationContext ac = new AnnotationConfigWebApplicationContext();
        ac.register(AppConfig.class);
        ac.refresh();

        // Create and register the DispatcherServlet
        // 创建并注册一个 DispatcherServlet
        DispatcherServlet servlet = new DispatcherServlet(ac);
        ServletRegistration.Dynamic registration = servletCxt.addServlet("app", servlet);
        registration.setLoadOnStartup(1);
        registration.addMapping("/app/*");
    }
}
```



## 使用 web.xml 声明一个 DispactherServlet

```xml
<web-app>

    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>

    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>/WEB-INF/app-context.xml</param-value>
    </context-param>

    <servlet>
        <servlet-name>app</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value></param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <servlet-mapping>
        <servlet-name>app</servlet-name>
        <url-pattern>/app/*</url-pattern>
    </servlet-mapping>

</web-app>
```

## 一些特殊的 Bean类型

`DispatcherServlet`委派了一些特殊的 bean 来处理请求和渲染相关的请求,这里的"特殊 Bean"意思是实现框架契约的spring托管对象实例,这些契约往往是内置的,但是你可以自定义其中的属性、拓展或者替代他们

| Bean type                                                    | Explanation                                                  |
| :----------------------------------------------------------- | :----------------------------------------------------------- |
| `HandlerMapping`                                             | Map a request to a handler along with a list of [interceptors](https://docs.spring.io/spring/docs/5.1.7.RELEASE/spring-framework-reference/web.html#mvc-handlermapping-interceptor) for pre- and post-processing. The mapping is based on some criteria, the details of which vary by `HandlerMapping` implementation.The two main `HandlerMapping` implementations are `RequestMappingHandlerMapping` (which supports `@RequestMapping` annotated methods) and `SimpleUrlHandlerMapping` (which maintains explicit registrations of URI path patterns to handlers).映射一个请求到一个 handler ,这个映射基于一个`interceptor`(前置或者后置处理)的列表,这个映射基于一些标准,其细节因`HandlerMapping`实现而异,它有几个主要的实现类:`RequestMappingHandlerMapping`(支持`@RequestMapping`注解方法)和`SimpleUrlHandlerMapping` (维护了一个显式的URL注册路径公式和 handler 的映射). |
| `HandlerAdapter`                                             | Help the `DispatcherServlet` to invoke a handler mapped to a request, regardless of how the handler is actually invoked. For example, invoking an annotated controller requires resolving annotations. The main purpose of a `HandlerAdapter` is to shield the `DispatcherServlet` from such details.帮助`DispatcherServlet`根据一个请求调用一个处理器,它不关心这个 handler 如何进行调用的.例如，调用带注释的控制器需要解析注释.`HandlerAdapter`的主要目的是屏蔽`DispatcherServlet`和它的调用细节 |
| [`HandlerExceptionResolver`](https://docs.spring.io/spring/docs/5.1.7.RELEASE/spring-framework-reference/web.html#mvc-exceptionhandlers) | Strategy to resolve exceptions, possibly mapping them to handlers, to HTML error views, or other targets. See [Exceptions](https://docs.spring.io/spring/docs/5.1.7.RELEASE/spring-framework-reference/web.html#mvc-exceptionhandlers).解析 Exceptions 的策略,有可能将他们和`Handler`进行映射,例如`Html错误页面`或者其他的目标 |
| [`ViewResolver`](https://docs.spring.io/spring/docs/5.1.7.RELEASE/spring-framework-reference/web.html#mvc-viewresolver) | Resolve logical `String`-based view names returned from a handler to an actual `View` with which to render to the response. See [View Resolution](https://docs.spring.io/spring/docs/5.1.7.RELEASE/spring-framework-reference/web.html#mvc-viewresolver) and [View Technologies](https://docs.spring.io/spring/docs/5.1.7.RELEASE/spring-framework-reference/web.html#mvc-view).解析逻辑上String 格式的"view",调用 handler 并返回一个指定的`View`并渲染到 response |
| [`LocaleResolver`](https://docs.spring.io/spring/docs/5.1.7.RELEASE/spring-framework-reference/web.html#mvc-localeresolver), [LocaleContextResolver](https://docs.spring.io/spring/docs/5.1.7.RELEASE/spring-framework-reference/web.html#mvc-timezone) | Resolve the `Locale` a client is using and possibly their time zone, in order to be able to offer internationalized views. See [Locale](https://docs.spring.io/spring/docs/5.1.7.RELEASE/spring-framework-reference/web.html#mvc-localeresolver).解析客户端使用的 `Locale`,并生成国际化页面 |
| [`ThemeResolver`](https://docs.spring.io/spring/docs/5.1.7.RELEASE/spring-framework-reference/web.html#mvc-themeresolver) | Resolve themes your web application can use — for example, to offer personalized layouts. See [Themes](https://docs.spring.io/spring/docs/5.1.7.RELEASE/spring-framework-reference/web.html#mvc-themeresolver).`解析web 应用的主题,例如,设置一个人化的布局 |
| [`MultipartResolver`](https://docs.spring.io/spring/docs/5.1.7.RELEASE/spring-framework-reference/web.html#mvc-multipart) | Abstraction for parsing a multi-part request (for example, browser form file upload) with the help of some multipart parsing library. See [Multipart Resolver](https://docs.spring.io/spring/docs/5.1.7.RELEASE/spring-framework-reference/web.html#mvc-multipart).抽象解析一个多模块的请求,例如(for example 文件上传需要借助于多模块的解析 library) |
| [`FlashMapManager`](https://docs.spring.io/spring/docs/5.1.7.RELEASE/spring-framework-reference/web.html#mvc-flash-attributes) | Store and retrieve the “input” and the “output” `FlashMap` that can be used to pass attributes from one request to another, usually across a redirect. See [Flash Attributes](https://docs.spring.io/spring/docs/5.1.7.RELEASE/spring-framework-reference/web.html#mvc-flash-attributes).存储检索"输出"和"输入"的`FlashMap`用来从一个请求传递属性到另外一个请求,通常通过重定向 |