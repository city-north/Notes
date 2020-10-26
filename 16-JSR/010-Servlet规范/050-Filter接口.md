# Filter接口

Filter接口允许Web容器对请求和响应做统一处理。例如，统一改变HTTP请求内容和响应内容，它可以作用于某个Servlet或一组Servlet。
Web应用部署完成后，必须实例化过滤器并调用其init方法。当请求进来时，获取第一个过滤器并调用doFilter方法，接着传入ServletRequest对象、ServletResponse对象及过滤器链(FilterChain)，doFilter方法负责过滤器链中下一个实体的doFilter方法调用。当容器要移除某过滤器时必须先调用过滤器的destroy方法。
可以用“@WebFilter”注解或部署描述文件定义过滤器，XML配置形式使用＜filter＞元素定义，包括＜filter-name＞、＜filter-class＞和＜init-params＞子节点，并使用＜filter-mapping＞定义Web应用的Servlet和其他静态资源通过过滤器。