# Property expansion using Maven Resource Filtering

## 使用Maven资源筛选的属性扩展

Maven资源插件提供的过滤只是变量替换。在资源文件中，我们可以使用${…}占位符作为变量，在构建期间由system或maven属性替换。

默认情况下，maven资源过滤是不启用的。如果我们从`spring-boot-starter-parent`扩展Spring Boot项目，则默认启用资源筛选。在这种情况下` @..@ ` 分隔符代替`${}`，这是为了避免与spring风格的占位符`${}`发生冲突。

如果我们不扩展spring-boot-start -parent，而是导入spring-boot-dependencies，那么我们必须自己启用maven资源过滤。

让我们来看看这两种情况的例子。

#### src/main/resources/application.properties

```
project-name=@project.name@
app-title=@app.title@
spring-version=@spring.version@
```

- `project.name`,maven 项目制定的属性
- `app.title`是我们自定义的名称
- `spring.version`继承自`spring-boot-starter-parent.`

```xml
<project .....>
 <modelVersion>4.0.0</modelVersion>

 <groupId>com.logicbig.example</groupId>
 <artifactId>maven-property-expansion</artifactId>
 <version>1.0-SNAPSHOT</version>
    <name>Automatic property expansion using Maven Example</name>

 <parent>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-parent</artifactId>
  <version>1.5.4.RELEASE</version>
 </parent>

 <properties>
  <java.version>1.8</java.version>
  <app.title>Example Project</app.title>
 </properties>
 <dependencies>
 <dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter</artifactId>
 </dependency>
 </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                 <addResources>false</addResources>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>

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

        @Value("${project-name}")
        private String projectName;

        @Value("${spring-version}")
        private String springVersion;

        @Value("${app-title}")
        private String appTitle;

        public void doSomething() {
            System.out.printf("Project name: %s%n"
                            + "Spring version: %s%n"
                            + "App title: %s%n",
                    projectName, springVersion, appTitle);
        }
    }
}
```

```
Project name: Automatic property expansion using Maven Example
Spring version: 4.3.9.RELEASE
App title: Example Project
```