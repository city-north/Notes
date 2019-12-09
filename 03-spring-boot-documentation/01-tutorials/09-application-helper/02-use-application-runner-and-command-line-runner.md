# 使用 ApplicationRunner 和 CommandLineRunner

什么时候使用:

- 我们需要获取 SpringApplication 启动的回调的时候

## 实现CommandLineRunner

```java
@SpringBootConfiguration
public class CmdExample {
    @Bean
    MyBean myBean() {
        return new MyBean();
    }

    @Bean
    CommandLineRunner cmdRunner() {
        return new CmdRunner();
    }

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(CmdExample.class, args);
        System.out.println("Context ready : " + context);
        MyBean myBean = context.getBean(MyBean.class);
        myBean.doSomething();
    }

    private static class CmdRunner implements CommandLineRunner {

        @Override
        public void run(String... strings) throws Exception {
            System.out.println("running CmdRunner#run: " + Arrays.toString(strings));
        }
    }

    private static class MyBean {

        public void doSomething() {
            System.out.println("In a bean doing something");
        }
    }
}
```

#### out

```java

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.5.3.RELEASE)

2017-05-17 11:56:46.496  INFO 8768 --- [dExample.main()] com.logicbig.example.CmdExample          : Starting CmdExample on JoeMsi with PID 8768 (D:\LogicBig\example-projects\spring-boot\boot-app-and-cmd-runner\target\classes started by Joe in D:\LogicBig\example-projects\spring-boot\boot-app-and-cmd-runner)
2017-05-17 11:56:46.498  INFO 8768 --- [dExample.main()] com.logicbig.example.CmdExample          : No active profile set, falling back to default profiles: default
running CmdRunner#run: []
2017-05-17 11:56:46.664  INFO 8768 --- [dExample.main()] com.logicbig.example.CmdExample          : Started CmdExample in 0.335 seconds (JVM running for 2.994)
In a bean doing something
```

## 实现 ApplicationRunner

```java
@SpringBootConfiguration
public class AppExample {
    @Bean
    MyBean myBean() {
        return new MyBean();
    }

    @Bean
    ApplicationRunner appRunner() {
        return new AppRunner();
    }

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(AppExample.class, args);
        System.out.println("Context ready : " + context);
        MyBean myBean = context.getBean(MyBean.class);
        myBean.doSomething();
    }

    private static class AppRunner implements ApplicationRunner {

        @Override
        public void run(ApplicationArguments args) throws Exception {
            System.out.println("running appRunner#run: " + Arrays.toString(args.getSourceArgs()));
        }
    }

    private static class MyBean {

        public void doSomething() {
            System.out.println("In a bean doing something");
        }
    }
}
```

```java
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.5.3.RELEASE)

2017-05-17 11:56:49.642  INFO 12072 --- [pExample.main()] com.logicbig.example.AppExample          : Starting AppExample on JoeMsi with PID 12072 (D:\LogicBig\example-projects\spring-boot\boot-app-and-cmd-runner\target\classes started by Joe in D:\LogicBig\example-projects\spring-boot\boot-app-and-cmd-runner)
2017-05-17 11:56:49.644  INFO 12072 --- [pExample.main()] com.logicbig.example.AppExample          : No active profile set, falling back to default profiles: default
running appRunner#run: []
2017-05-17 11:56:49.808  INFO 12072 --- [pExample.main()] com.logicbig.example.AppExample          : Started AppExample in 0.33 seconds (JVM running for 2.923)
In a bean doing something
```

## ApplicationRunner vs CommandLineRunner

> Technically there's no difference between the two, both are called at the end of SpringApplication#run. There's only one difference; ApplicationRunner#run is called with ApplicationArguments instead of String[] args.

- 准确来说没有区别,只有调用时候参数形参不一致

> Multiple ApplicationRunner/CommandLineRunner beans can be registered within the same application context and can be ordered using the Ordered interface or @Order annotation.

- 可以注册多个 ApplicationRunner/CommandLineRunner 排序使用 @Order