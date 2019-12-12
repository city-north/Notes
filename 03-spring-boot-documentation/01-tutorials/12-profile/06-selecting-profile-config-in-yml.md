# Selecting Profile configuration in YAML property file

- 在 YAML 文件中,可以指定多个激活的profile
- 使用`---`进行分割

## Example

#### src/main/resources/application.yml

```
refresh:
   rate: 5
---
spring:
   profiles: dev, test
refresh:
   rate: 10
---
spring:
   profiles: prod
refresh:
   rate: 8
```

## The Main class

```
@SpringBootConfiguration
public class ExampleMain {
    @Bean
    MyBean myBean() {
        return new MyBean();
    }

    public static void main(String[] args) throws InterruptedException {
        ConfigurableApplicationContext context = SpringApplication.run(ExampleMain.class, args);

        String[] profiles = context.getEnvironment().getActiveProfiles();
        System.out.println("Active Profiles: "+ Arrays.toString(profiles));

        MyBean myBean = context.getBean(MyBean.class);
        myBean.doSomething();
    }

    private static class MyBean {

        @Value("${refresh.rate}")
        private int refreshRate;

        public void doSomething() {
            System.out.printf("Refresh Rate : %s%n", refreshRate);
        }
    }
}
```

#### Output

```
mvn -q spring-boot:run
2017-07-27 16:21:21.466  INFO 10572 --- [           main] s.c.a.AnnotationConfigApplicationContext : Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@7a52f2a2: startup date [Thu Jul 27 16:21:21 CDT 2017]; root of context hierarchy
Active Profiles: []
Refresh Rate : 5
2017-07-27 16:21:21.619  INFO 10572 --- [       Thread-1] s.c.a.AnnotationConfigApplicationContext : Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@7a52f2a2: startup date [Thu Jul 27 16:21:21 CDT 2017]; root of context hierarchy
```

In above example, we didn't specify any profile during startup, so default value of 'refresh.rate' was used.

Let's select profile 'dev' by specifying the value for 'spring.profiles.active' property:

#### Output

```
mvn -q spring-boot:run -Dspring.profiles.active=dev
2017-07-27 16:20:05.705  INFO 15776 --- [           main] s.c.a.AnnotationConfigApplicationContext : Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@78047b92: startup date [Thu Jul 27 16:20:05 CDT 2017]; root of context hierarchy
Active Profiles: [dev]
Refresh Rate : 10
2017-07-27 16:20:05.927  INFO 15776 --- [       Thread-1] s.c.a.AnnotationConfigApplicationContext 
```