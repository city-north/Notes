#  Destruction callback

析构回调

>  SpringApplication implicitly registers a shutdown hook with the JVM to ensure that ApplicationContext is closed gracefully on exit. That will also call all bean methods annotated with @PreDestroy. That means we don't have to explicitly use `Configurable Application Context# register Shutdown Hook()` in a boot application, like [we have to do in spring core application](https://www.logicbig.com/tutorials/spring-framework/spring-core/lifecycle-callbacks.html).

- Spring 应用隐式注册了 JVM shutdown 钩子确保 ApplicationContext 优雅关闭

- 回调会调用 bean 的 @PreDestroy方法

  

```java
@SpringBootConfiguration
public class ExampleMain {
    @Bean
    MyBean myBean() {
        return new MyBean();
    }

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ExampleMain.class, args);
        MyBean myBean = context.getBean(MyBean.class);
        myBean.doSomething();

        //no need to call context.registerShutdownHook();
    }

    private static class MyBean {

        @PostConstruct
        public void init() {
            System.out.println("init");
        }

        public void doSomething() {
            System.out.println("in doSomething()");
        }

        @PreDestroy
        public void destroy() {
            System.out.println("destroy");
        }
    }
}
```



```

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.5.3.RELEASE)

2017-05-17 11:56:20.564  INFO 16524 --- [mpleMain.main()] com.logicbig.example.ExampleMain         : Starting ExampleMain on JoeMsi with PID 16524 (D:\LogicBig\example-projects\spring-boot\boot-destruction-callback\target\classes started by Joe in D:\LogicBig\example-projects\spring-boot\boot-destruction-callback)
2017-05-17 11:56:20.565  INFO 16524 --- [mpleMain.main()] com.logicbig.example.ExampleMain         : No active profile set, falling back to default profiles: default
init
2017-05-17 11:56:20.723  INFO 16524 --- [mpleMain.main()] com.logicbig.example.ExampleMain         : Started ExampleMain in 0.348 seconds (JVM running for 2.94)
in doSomething()
destroy
```

