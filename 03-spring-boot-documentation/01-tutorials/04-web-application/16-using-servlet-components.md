# Using Servlet Components

SpringBoot 的注解  [@ServletComponentScan](http://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/web/servlet/ServletComponentScan.html) 可以开启 Servlet 3.0 相关的注解:

- @WebServlet
- @WebFilter
- @WebListener

## @ServletComponentScan

如下面代码所示`ServletComponentScan` import 了`ServletComponentScanRegistrar`

`ServletComponentScanRegistrar` 又继承了`ImportBeanDefinitionRegistrar`

- 这个实现类主要扫描和注册 servlet 组件
- 默认情况下扫描标注这个类的包下的 class

值得注意的是

- `ServletComponentScan`仅仅在使用嵌入式容器时使用
- 如果是外部容器,由于 servlet 容器内置的扫描机制来管理 servlet 组件的管理.所以标注这个类也无效

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(ServletComponentScanRegistrar.class)
public @interface ServletComponentScan {

	/**
	 * Alias for the {@link #basePackages()} attribute. Allows for more concise annotation
	 * declarations e.g.: {@code @ServletComponentScan("org.my.pkg")} instead of
	 * {@code @ServletComponentScan(basePackages="org.my.pkg")}.
	 * @return the base packages to scan
	 */
	@AliasFor("basePackages")
	String[] value() default {};

	/**
	 * Base packages to scan for annotated servlet components. {@link #value()} is an
	 * alias for (and mutually exclusive with) this attribute.
	 * <p>
	 * Use {@link #basePackageClasses()} for a type-safe alternative to String-based
	 * package names.
	 * @return the base packages to scan
	 */
	@AliasFor("value")
	String[] basePackages() default {};

	/**
	 * Type-safe alternative to {@link #basePackages()} for specifying the packages to
	 * scan for annotated servlet components. The package of each class specified will be
	 * scanned.
	 * @return classes from the base packages to scan
	 */
	Class<?>[] basePackageClasses() default {};

}

```

## Registering Servlet Components as Spring Beans

Spring 提供了三个类让我们手动注册 Servlet 组件

- [ServletRegistrationBean](http://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/web/servlet/ServletRegistrationBean.html)
- [FilterRegistrationBean](http://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/web/servlet/FilterRegistrationBean.html)
- [ServletListenerRegistrationBean](http://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/web/servlet/ServletListenerRegistrationBean.html)

#### 代码实例

```java
/**
 * Spring 提供了三个类让我们手动注册 Servlet 组件
 *
 * @author EricChen 2019/12/09 21:36
 */
@SpringBootApplication
public class RegistServletExample {

    public static void main(String[] args) {
        SpringApplication.run(RegistServletExample.class, args);
    }


    @Bean
    public ServletRegistrationBean servletServletRegistrationBean() {
        ServletRegistrationBean srb = new ServletRegistrationBean();
        srb.setServlet(new MyServlet());
        srb.setUrlMappings(Arrays.asList("/path2/*"));
        return srb;
    }


    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean frb = new FilterRegistrationBean();
        frb.setFilter(new MyFilter());
        frb.setUrlPatterns(Arrays.asList("/*"));
        return frb;
    }

    @Bean
    ServletListenerRegistrationBean<ServletContextListener> myServletListener() {
        ServletListenerRegistrationBean<ServletContextListener> srb =
                new ServletListenerRegistrationBean<>();
        srb.setListener(new MyServletListener());
        return srb;
    }
}

```

#### controller

```java
/**
 * description
 *
 * @author EricChen 2019/12/09 21:45
 */
@Controller
public class MyController {

    @RequestMapping("/*")
    @ResponseBody
    public String handler() {
        return "response form spring controller method";
    }
}

```

#### ServletListener

```java
/**
 * description
 *
 * @author EricChen 2019/12/09 21:41
 */
public class MyServletListener implements ServletContextListener {
    private static final Logger logger = LoggerFactory.getLogger(MyServletListener.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        logger.debug("from ServletContextListener: {} context initialized", sce.getServletContext().getServerInfo());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
```

#### HttpServlet

```java

/**
 * 自定义 Servlet
 *
 * @author EricChen 2019/12/09 21:36
 */
public class MyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        writer.println("response from servlet ");
    }
}

```

