# Servlet规范

Servlet 是 J2EE标准的一部分, 是 Java Web 开发的标准 。标准比协议多了强制性的意义,不过他们的作用是基本一样的:

**标准都是用来指定统一的规矩**

因为Java是一门具体的语言,所以为了统一的实现它可以指定自己的标准

## 标准时不干活的

Servlet制定了Java中处理Web请求的标准,我们只需要按照标准规定的去做就可以了,规范自己是不干活的,标准都是不干活的,要想使用Servlet 必须要有Servlet 容器才行

- Tomcat
- Jetty
- Undertow

## Servlet结构

![image-20201008225626207](../../assets/image-20201008225626207.png)



```java
public interface Servlet {
  public void init(ServletConfig config) throws ServletException;
  public ServletConfig getServletConfig();
  public void service(ServletRequest req, ServletResponse res)
  throws ServletException, IOException;
  public String getServletInfo();
  public void destroy();
}
```
| 方法             | 描述                                                         |
| ---------------- | ------------------------------------------------------------ |
| init             | 在容器启动的时候被容器调用(当load-on-startup 设置为负数或者不设置是会在Serlvet第一次用到时才调用),只调用一次 |
| getServletConfig | 用于获取ServletConfig                                        |
| service          | 处理一个请求                                                 |
| getServletInfo   | 获取Servlet相关信息                                          |
| destroy          | 用于Servlet销毁(一般是关闭服务器)时释放一些资源, 只会调用一次 |

