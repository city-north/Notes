# Using FreeMarker View

- 引入spring-boot-starter-freemarker
- 模板文件放入src/main/resources/templates/

## 

```xml
<project .....>
    <modelVersion>4.0.0</modelVersion>
    <groupId>com.logicbig.example</groupId>
    <artifactId>spring-boot-freemarker-example</artifactId>
    <version>1.0-SNAPSHOT</version>
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.5.9.RELEASE</version>
    </parent>
    <properties>
        <java.version>1.8</java.version>
    </properties>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-freemarker</artifactId>
        </dependency>
    </dependencies>
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
</project>
Note that spring-boot-starter-freemarker will pull all freemarker related dependencies and Spring Web MVC dependencies as well.

FreeMarker Template File
/src/main/resources/templates/my-page.ftl
<!DOCTYPE html>
<html lang="en">
<body>
<h2>FreeMarker View</h3>
	<div> Message: ${msg}</div>
	<div> Time: ${time} </div>
</body>
</html>
```

#### MVC Controller

```java
@Controller
@RequestMapping("/")
public class MyController {
  
  @RequestMapping
  public String handleRequest (Model model) {
      model.addAttribute("msg", "A message from the controller");
      model.addAttribute("time", LocalTime.now());
      return "my-page";
  }
}
```

### Spring boot main class

```java
@SpringBootApplication
public class ExampleMain {

  public static void main(String[] args) throws InterruptedException {
      SpringApplication.run(ExampleMain.class, args);
  }
}
```

