# BeanNameViewResolver 自动注册

在 SpringBoot 中,BeanNameViewResovler bean 默认注册的,意味着我们可以默认使用视图的bean名作为视图名。在一个普通的Spring MVC应用程序中，我们必须自己显式地注册这个bean

```java
/**
 * 自定义 {@link View} 实现类与解析
 *
 * @author EricChen 2019/12/09 21:59
 */
@SpringBootApplication
public class MyCustomViewExample {
    public static void main(String[] args) {
        SpringApplication.run(MyCustomViewExample.class, args);
    }

}

```

#### View

```java
/**
 * 自定义 View
 *
 * @author EricChen 2019/12/09 21:56
 */
@Component("myCustomView")
public class MyCustomView implements View {

    private static final Logger logger = LoggerFactory.getLogger(MyCustomView.class);

    @Override
    public String getContentType() {
        return "text/html";
    }

    @Override
    public void render(Map<String, ?> map, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception {
        map.forEach((k, v) -> logger.debug("render map key={},value={}", k, v));
        PrintWriter writer = httpServletResponse.getWriter();
        writer.write("msg rendered in MyCustomView: " + map.get("msg"));
    }
}
```



```java
/**
 * 控制层代码
 *
 * @author EricChen 2019/12/09 21:59
 */
@Controller
public class MyController {
    @GetMapping("/")
    public String handle(Model model) {
        model.addAttribute("msg", "test msg from controller");
        return "myCustomView";
    }
}

```

