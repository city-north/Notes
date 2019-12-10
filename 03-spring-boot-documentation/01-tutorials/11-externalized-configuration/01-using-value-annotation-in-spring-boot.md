# 使用@Value 注解获取配置数据

#### src/main/resources/application.properties

```
app.title=My Spring Application
```

```java
@SpringBootConfiguration
public class ExampleMain {
    @Bean
    MyBean myBean() {
        return new MyBean();
    }

    public static void main(String[] args) throws InterruptedException {
        ApplicationContext context = SpringApplication.run(ExampleMain.class, args);
        MyBean myBean = context.getBean(MyBean.class);
        myBean.startApplication();
    }

    private static class MyBean {

        @Value("${app.title}")
        private String appTitle;

        public void startApplication() {
            System.out.printf("-- running application: %s --%n", appTitle);

        }
    }
}
```

#### 输出

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.5.4.RELEASE)

2017-07-06 21:01:02.995  INFO 11144 --- [           main] com.logicbig.example.ExampleMain         : Starting ExampleMain on JoeMsi with PID 11144 (D:\example-projects\spring-boot\boot-value-annotation\target\classes started by Joe in D:\example-projects\spring-boot\boot-value-annotation)
2017-07-06 21:01:02.995  INFO 11144 --- [           main] com.logicbig.example.ExampleMain         : No active profile set, falling back to default profiles: default
2017-07-06 21:01:03.027  INFO 11144 --- [           main] s.c.a.AnnotationConfigApplicationContext : Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@24b1d79b: startup date [Thu Jul 06 21:01:03 CDT 2017]; root of context hierarchy
2017-07-06 21:01:03.165  INFO 11144 --- [           main] com.logicbig.example.ExampleMain         : Started ExampleMain in 0.37 seconds (JVM running for 0.596)
-- running application: My Spring Application --
2017-07-06 21:01:03.165  INFO 11144 --- [       Thread-1] s.c.a.AnnotationConfigApplicationContext : Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@24b1d79b: startup date [Thu Jul 06 21:01:03 CDT 2017]; root of context hierarchy
```