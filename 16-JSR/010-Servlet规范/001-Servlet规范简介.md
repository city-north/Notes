# Servlet规范

Servlet 是 J2EE标准的一部分, 是 Java Web 开发的标准 。标准比协议多了强制性的意义,不过他们的作用是基本一样的:

**标准都是用来指定统一的规矩**

因为Java是一门具体的语言,所以为了统一的实现它可以指定自己的标准

## 标准时不干活的

Servlet制定了Java中处理Web请求的标准,我们只需要按照标准规定的去做就可以了,规范自己是不干活的,标准都是不干活的,要想使用Servlet 必须要有Servlet 容器才行

- Tomcat
- Jetty
- Undertow

## Web应用

Web应用和ServletContext接口对象是一对一的关系，ServletContext对象提供了一个Servlet和它的应用程序视图。Web应用可能包括Servlet、JSP、工具类、静态文件、客户端Java Applet等。Web应用结构包括WEB-INF/web.xml文件、WEB-INF/lib/目录下存放的所有jar包、WEB-INF/classes/目录中存放的所有类、META-INF目录存放的项目的一些信息，以及其他根据具体目录存放的资源。一般WEB-INF目录下的文件都不能由容器直接提供给客户端访问，但WEB-INF目录中的内容可以通过Servlet代码调用ServletContext的getResource和getResourceAsStream方法来访问，并可使用RequestDispatcher调用公开这些内容。

Web容器用于加载WAR文件中Servlet的类加载器必须提供getResource方法，以加载WAR文件的JAR包中包含的任何资源。容器不允许Web应用程序覆盖或访问容器的实现类。一个类加载器的实现必须保证部署到容器的每个Web应用，在调用Thread.currentThread. getContextClassLoader()时返回一个规定的ClassLoader实例。部署的每个Web应用程序的ClassLoader实例必须是一个单独的实例。

服务器应该能在不重启Web容器的情况下更新一个Web应用程序，而更新Web应用程序时Web容器应该提供可靠的方法保存这些Web应用的会话。
如果调用response的sendError方法或如果Servlet产生一个异常或把错误传播给容器，容器要按照Web应用部署描述文件中定义的错误页面列表，根据状态码或异常试图返回一个匹配的错误页面。如果Web应用部署描述文件的error-page元素没有包含exception-type或error-code子元素，则错误页面使用默认的错误页面。
Web应用的部署描述符中可以配置欢迎文件列表。当一个Web的请求URI没有映射到一个Web资源时，可以从欢迎文件列表中按顺序匹配适合的资源返回给客户端，如欢迎页为index.html，则http://localhost:8080/webapp请求实际变为http://localhost:8080/webapp/index.html。如果找不到对应的欢迎页，则返回404响应。
当一个Web应用程序部署到容器中时，在Web应用程序开始处理客户端请求之前，必须按照下述步骤顺序执行。

- ① 实例化部署描述文件中＜listener＞元素标识的每个事件监听器的一个实例。
- ② 对于已实例化且实现了ServletContextListener接口的监听器实例，调用contextInitialized()方法。
- ③ 实例化部署描述文件中＜filter＞元素标识的每个过滤器的一个实例，并调用每个过滤器实例的init()方法。
- ④ 根据load-on-startup元素值定义的顺序，包含＜load-on-startup＞元素的＜servlet＞元素为每个Servlet实例化一个实例，并调用每个Servlet实例的init()方法。

对于不包含任何Servlet、Filter或Listener的Web应用，或使用注解声明的Web应用，可以不需要web.xml部署描述符。