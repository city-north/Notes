# Generating Random Properties

生成随机属性

- `RandomValuePropertySource`可以生成一个随机的值,如果是`random.`开头语的

## 代码实例

#### src/main/resources/application.properties

```properties
#random int
app.location-x=${random.int}
app.location-y=${random.int}

#random int with max
app.user-age=${random.int(100)}

#random int range
app.max-users=${random.int[1,10000]}

#random long with max
app.refresh-rate-milli=${random.long(1000000)}

#random long range
app.initial-delay-milli=${random.long[100,90000000000000000]}

#random 32 bytes
app.user-password=${random.value}

#random uuid. Uses java.util.UUID.randomUUID()
app.instance-id=${random.uuid}
```

```java
@Component
@ConfigurationProperties("app")
public class MyAppProperties {
  private int locationX;
  private int locationY;
  private int userAge;
  private int maxUsers;
  private long refreshRateMilli;
  private long initialDelayMilli;
  private String userPassword;
  private UUID instanceId;
    .............
}
```

- 我们也可以用`@Value`注解

```java
@SpringBootApplication
public class ExampleMain {

  public static void main(String[] args) throws InterruptedException {
      SpringApplication springApplication = new SpringApplication(ExampleMain.class);
      springApplication.setBannerMode(Banner.Mode.OFF);
      springApplication.setLogStartupInfo(false);
      ConfigurableApplicationContext context = springApplication.run(args);
      MyAppProperties bean = context.getBean(MyAppProperties.class);
      System.out.println(bean);
  }
}
```

输出

```
2017-08-26 11:22:46.923  INFO 236 --- [mpleMain.main()] o.s.j.e.a.AnnotationMBeanExporter        : Registering beans for JMX exposure on startup
MyAppProperties{
 locationX=247678689,
 locationY=632374795,
 userAge=95,
 maxUsers=4709,
 refreshRateMilli=655957,
 initialDelayMilli=48846676108633928,
 userPassword='13f5929528de7f224c96fd51dde283f1',
 instanceId=ad46ce0a-0d50-47f2-b519-bc0b8b0e930b
}
2017-08-26 11:22:47.047  INFO 236 --- [       Thread-2] o.s.j.e.a.AnnotationMBeanExporter        : Unregistering JMX-exposed beans on shutdown

```

