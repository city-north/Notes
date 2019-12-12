# 在Property 文件中使用${}占位符

- 使用${someProp}的方式写到配置文件中
- 可以在使用 jar 包运行时使用`--someProp=theValue`

#### src/main/resources/application.properties

```properties
app.title=Boot ${app} @project.artifactId@
```

```java
@SpringBootConfiguration
public class ExampleMain {
    @Bean
    MyBean myBean() {
        return new MyBean();
    }

    public static void main(String[] args) throws InterruptedException {
        SpringApplication bootApp = new SpringApplication(ExampleMain.class);
        bootApp.setBannerMode(Banner.Mode.OFF);
        bootApp.setLogStartupInfo(false);
        ConfigurableApplicationContext context = bootApp.run(args);
        MyBean myBean = context.getBean(MyBean.class);
        myBean.doSomething();
    }

    private static class MyBean {

        @Value("${app.title}")
        private String appTitle;

        public void doSomething() {
            System.out.printf("App title : %s%n", appTitle);
        }
    }
}
```

运行命令:

```
mvn spring-boot:run -Dapp=Example
```

输出

```
c:\example-projects\spring-boot\place-holders-in-properties>mvn spring-boot:run -Dapp=Example
App title : Boot Example place-holders-in-properties
```