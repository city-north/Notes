# Dispatcher Servlet 是如何被初始化的

## DispatcherServlet Hierarchy

![image-20200105171723364](assets/image-20200105171723364.png)

1. Servlet 规范规定, Servlet 容器初始化时会根据 web.xml 中配置调用 Serlvet的 `init(ServletConfig config)`

2. HttpServletBean 的 init 方法会调用SpringMVC初始化相关机制

   ![image-20200105172223038](assets/image-20200105172223038.png)