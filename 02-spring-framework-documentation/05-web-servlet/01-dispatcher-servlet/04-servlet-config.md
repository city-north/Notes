# Servlet Config

在 Servlet 3.0环境下,你可以通过编码的方式注册 DispatchServlet ,在 [01-dispatcher-servlet-context-hierarchy.md](01-dispatcher-servlet-context-hierarchy.md) 已经有相应的笔记,这里做一些补充

```java
import org.springframework.web.WebApplicationInitializer;

public class MyWebApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) {
        XmlWebApplicationContext appContext = new XmlWebApplicationContext();
        appContext.setConfigLocation("/WEB-INF/spring/dispatcher-config.xml");

        ServletRegistration.Dynamic registration = container.addServlet("dispatcher", new DispatcherServlet(appContext));
        registration.setLoadOnStartup(1);
        registration.addMapping("/");
    }
}
```

## WebApplicationInitializer

这个接口时 SpringMVC 提供的,它提供了一种 Servlet 3 容器初始化机制,容器会自动检测它的实现类并通过  [SPI](../../../04-java/01-basic/spi-service-provider-interface.md) 的方式进行回调,这样,就允许实现类通过编码形式来创建 Servlet,Filter 等等

下面是一个编码方式注册 DispatcherServlet 的方式

```java
import org.springframework.web.WebApplicationInitializer;

public class MyWebApplicationInitializer implements WebApplicationInitializer {

    @Override
    public void onStartup(ServletContext container) {
        XmlWebApplicationContext appContext = new XmlWebApplicationContext();
        appContext.setConfigLocation("/WEB-INF/spring/dispatcher-config.xml");

        ServletRegistration.Dynamic registration = container.addServlet("dispatcher", new DispatcherServlet(appContext));
        registration.setLoadOnStartup(1);
        registration.addMapping("/");
    }
}
```

这个接口有两个抽象的实现类

- `AbstractDispatcherServletInitializer`: 主要是封装了一些方便注册 DispatcherServlet 的方法,如果你是一个基于 XML的配置方式,直接继承`AbstractDispatcherServletInitializer`

- `AbstractAnnotationConfigDispatcherServletInitializer`,主要添加了对注解注册 DispatcherServlet 的支持

## AbstractAnnotationConfigDispatcherServletInitializer

如果是基于 Java 代码方式注册 Bean 的方式,用一下方式:

```java
public class MyWebAppInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return null;
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class<?>[] { MyWebConfig.class };
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}
```

如果你是一个基于 XML的配置方式,直接继承`AbstractDispatcherServletInitializer`

## **AbstractDispatcherServletInitializer**

```java
public class MyWebAppInitializer extends AbstractDispatcherServletInitializer {

    @Override
    protected WebApplicationContext createRootApplicationContext() {
        return null;
    }

    @Override
    protected WebApplicationContext createServletApplicationContext() {
        XmlWebApplicationContext cxt = new XmlWebApplicationContext();
        cxt.setConfigLocation("/WEB-INF/spring/dispatcher-config.xml");
        return cxt;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[] { "/" };
    }
}
```

当然,他们也提供了非常简单的注册 Filter 的封装

```java
public class MyWebAppInitializer extends AbstractDispatcherServletInitializer {

    // ...

    @Override
    protected Filter[] getServletFilters() {
        return new Filter[] {
            new HiddenHttpMethodFilter(), new CharacterEncodingFilter() };
    }
}
```

#### 值得注意的是:

- 这里添加的 filter 都会直接关联到 DispatcherServlet

- `isAsyncSupported`方法规定了 Serlvet 是否支持同步
- 通过覆盖`createDispatcherServlet`方法,可以定制`DispatcherServlet`