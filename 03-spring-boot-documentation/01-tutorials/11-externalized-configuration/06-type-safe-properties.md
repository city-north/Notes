# Type safe properties binding with @ConfigurationProperties

Spring Boot的 annotation `@ConfigurationProperties`是一种将外部属性自动绑定到Java对象的类型安全方法。

目标对象字段的名称应该与外部属性名称兼容。Spring Boot提供了轻松的绑定规则，可以自动将外部属性解析为Java字段/属性。这些规则负责处理连字符和下划线等内容。点击这里查看规则。

## Example

#### src/main/resources/application.yml

```java
app:
  send-email-on-errors: true
  exit-on-errors: true
  order-screen-properties:
       title:      Order Placement
       item-label: Item
       qty-label:  Qunatity
  customer-screen-properties:
       title:       Customer Profile
       name-label:  Customer Name
       phone-label: Contact Number

```

#### src/main/resources/application.properties

```java
spring.main.banner-mode=off 
spring.main.logStartupInfo=false
app.refresh-rate=15
app.admin-emails[0]=jim@example.com
app.admin-emails[1]=gina@example.com
app.admin-emails[2]=tom@examle.com
```

## Binding properties with @ConfigurationProperties

```java
@Component
@ConfigurationProperties("app")
public class MyAppProperties {
  private List<String> adminEmails;
  boolean sendEmailOnErrors;
  boolean exitOnErrors;
  private int refreshRate;
  private Map<String, String> orderScreenProperties;
  private Map<String, String> customerScreenProperties;
    .............
}
```

## The Main class

```java
@SpringBootApplication
public class ExampleMain {

  public static void main(String[] args) throws InterruptedException {
      ConfigurableApplicationContext context = SpringApplication.run(ExampleMain.class, args);
      MyAppProperties bean = context.getBean(MyAppProperties.class);
      System.out.println(bean);
  }
}
```

输出

```
2017-07-31 22:06:41.113  INFO 13048 --- [mpleMain.main()] o.s.j.e.a.AnnotationMBeanExporter        : Registering beans for JMX exposure on startup
MyAppProperties{
 adminEmails=[jim@example.com, gina@example.com, tom@examle.com],
 sendEmailOnErrors=true,
 exitOnErrors=true,
 refreshRate=15,
 orderScreenProperties={title=Order Placement, item-label=Item, qty-label=Qunatity},
 customerScreenProperties={title=Customer Profile, name-label=Customer Name, phone-label=Contact Number}
}
2017-07-31 22:06:41.223  INFO 13048 --- [       Thread-2] o.s.j.e.a.AnnotationMBeanExporter        : Unregistering JMX-exposed beans on shutdown
```

## 值得注意的是

如果我们注册我们的配置类`configuration`.通过@Bean 方法注册 bean,而不是 使用 `@Component`

- `ConfigurationProperties` 注解可以在@Bean方法上修饰

```java
@SpringBootApplication
public class ExampleMain {
    @Bean
    @ConfigurationProperties("app")
    MyAppProperties myAppProperties(){
        return new MyAppProperties();
    }
 .....
}
```

由于我们不需要在目标类上使用任何注释(在上面的例子中是MyAppProperties)，所以当我们想要将属性绑定到不在我们控制范围内的第三方组件时，这种配置风格会很有用。

- 我们还可以使用`@EnableConfigurationProperties(MyAppProperties.class)`注释和`@SpringBootApplication`(主配置类)。在这种情况下，我们不必在配置类上使用@Component或通过@Bean注册它。配置类将在Spring Boot时自动注册为bean。


```java
@SpringBootApplication
@EnableConfigurationProperties(MyAppProperties.class)
public class ExampleMain {
 ...
}
```

```java
@ConfigurationProperties("app")
public class MyAppProperties {
...
}
```