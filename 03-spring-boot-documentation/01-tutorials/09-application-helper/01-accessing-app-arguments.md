# Accessing application arguments

在 SpringBoot 应用中,有两种方式访问应用参数

- 注入 [ApplicationArguments](http://docs.spring.io/spring-boot/docs/current/api/org/springframework/boot/ApplicationArguments.html),它提供了访问参数的方法
- SpringBoot 隐式地注册 [CommandLinePropertySource with the Spring Environment](https://www.logicbig.com/tutorials/spring-framework/spring-core/command-line-property-source.html),这意味着@Value 注解可以直接访问采参数

## 注入 ApplicationArguments

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
    }

    private static class MyBean {
        @Autowired
        ApplicationArguments appArgs;

        public void doSomething() {
            List<String> args = appArgs.getOptionValues("myArg");
            if (args.size() > 0) {
                System.out.printf("The value of application arg myArg: %s%n", args.get(0));
            }
        }
    }
}
```

```mvn -q spring-boot:run -DtheMainClass=&quot;com.logicbig.example.ExampleMain&quot; -Drun.arguments=&quot;--myArg=myArgVal&quot;
mvn -q spring-boot:run -DtheMainClass="com.logicbig.example.ExampleMain" -Drun.arguments="--myArg=myArgVal"
```

```
The value of application arg myArg: myArgVal
```

注意命令行参数`-DtheMainclass`是一个占位符

```java
   <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <mainClass>${theMainClass}</mainClass>
                </configuration>
            </plugin>
        </plugins>
    </build>
```

## 使用@Value 注解

我们不用隐式添加 CommandLinePropertySource 到 Environment 对象,直接使用@Value 注解:

```java
@SpringBootConfiguration
public class ExampleMain2 {

    @Bean
    MyBean myBean() {
        return new MyBean();
    }

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ExampleMain2.class, args);
        MyBean myBean = context.getBean(MyBean.class);
        myBean.doSomething();
    }

    private static class MyBean {
        @Value("${myArg}")
        private String myArgStr;

        public void doSomething() {
            System.out.printf("The value of application arg myArg: %s%n", myArgStr);
        }
    }
}
```

```
mvn -q spring-boot:run -DtheMainClass="com.logicbig.example.ExampleMain2" -Drun.arguments="--myArg=myArgVal"
```

```
The value of application arg myArg: myArgVal
```