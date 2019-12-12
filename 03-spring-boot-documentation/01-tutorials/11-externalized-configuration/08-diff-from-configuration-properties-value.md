# `@ConfigurationProperties` 和 `@Value`的区别

我们从之前的例子看到如何使用松散的绑定规则应用`@ConfigurationProperties.`

- 无论使用的是`@ConfigurationProperties`还是`@Value`注解,底层都是隐式地调用了 `type conversion`相关的技术,通过 [44-property-editors.md](../../../02-spring-framework-documentation/02-core/00-tutorials/07-data-binding-validation-conversion-and-formatting/44-property-editors.md) 和 [45-core-data-binding.md](../../../02-spring-framework-documentation/02-core/00-tutorials/07-data-binding-validation-conversion-and-formatting/45-core-data-binding.md) 和  [50-conversion-service.md](../../../02-spring-framework-documentation/02-core/00-tutorials/07-data-binding-validation-conversion-and-formatting/50-conversion-service.md) 三个技术
- `@ConfigurationProperties` 使用的是松散绑定,属性值不必总是区分大小写(例如Enum元素值)
- `@Value`属性值必须是完全相同的字母大小写

## 代码实例

#### 使用`@ConfigurationProperties`

#### src/main/resources/application.properties

```java
spring.main.banner-mode=off 
spring.main.logStartupInfo=false
app.trade-currency=USD
app.refresh-time-unit=seconds
app.refresh-rate=5
```

```java
@Component
@ConfigurationProperties("app")
public class MyAppProperties {
  private int refreshRate;
  private TimeUnit refreshTimeUnit;
  private Currency tradeCurrency;
    .............
}
```

#### main

```
@SpringBootApplication
public class ConfigPropertyExampleMain {

  public static void main(String[] args) throws InterruptedException {
      ConfigurableApplicationContext context = SpringApplication.run(ConfigPropertyExampleMain.class, args);
      MyAppProperties bean = context.getBean(MyAppProperties.class);
      System.out.println(bean);
  }
}
```

输出

```
2017-08-05 00:02:16.947  INFO 9092 --- [mpleMain.main()] o.s.j.e.a.AnnotationMBeanExporter        : Registering beans for JMX exposure on startup
MyAppProperties{
refreshRate=5,
 refreshTimeUnit=SECONDS,
 tradeCurrency=USD
}
2017-08-05 00:02:17.086  INFO 9092 --- [       Thread-2] o.s.j.e.a.AnnotationMBeanExporter        : Unregistering JMX-exposed beans on shutdown
```

在上面的例子中,

- `app.refresh-time-unit=seconds`属性被转换成了 [TimeUnit.SECONDS](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/TimeUnit.html#SECONDS),即使我们使用了小写的 seconds

- `app.trade-currency=USD`,我们不能使用小写,因为转换不是针对类型,而是使用属性值作为`java.util.Currency.getInstance(source)`的参数

#### 使用 `@Value`

```java
public class MyConfigBean {
  @Value("${app.refresh-rate}")
  private int refreshRate;
  @Value("${app.refresh-time-unit}")
  private TimeUnit refreshTimeUnit;
  @Value("${app.trade-currency}")
  private Currency tradeCurrency;
    .............
}
```

```java
@SpringBootApplication
public class ValueExampleMain {
  @Bean
  public MyConfigBean myBean() {
      return new MyConfigBean();
  }

  public static void main(String[] args) {
      ConfigurableApplicationContext context = SpringApplication.run(ValueExampleMain.class, args);
      MyConfigBean bean = context.getBean(MyConfigBean.class);
      System.out.println(bean);
  }
}
```

```
org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'myBean': Unsatisfied dependency expressed through field 'refreshTimeUnit'; nested exception is org.springframework.beans.ConversionNotSupportedException: Failed to convert value of type 'java.lang.String' to required type 'java.util.concurrent.TimeUnit'; nested exception is java.lang.IllegalStateException: Cannot convert value of type 'java.lang.String' to required type 'java.util.concurrent.TimeUnit': no matching editors or conversion strategy found
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredFieldElement.inject(AutowiredAnnotationBeanPostProcessor.java:588) ~[spring-beans-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.beans.factory.annotation.InjectionMetadata.inject(InjectionMetadata.java:88) ~[spring-beans-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor.postProcessPropertyValues(AutowiredAnnotationBeanPostProcessor.java:366) ~[spring-beans-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.populateBean(AbstractAutowireCapableBeanFactory.java:1264) ~[spring-beans-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:553) ~[spring-beans-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:483) ~[spring-beans-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory$1.getObject(AbstractBeanFactory.java:306) ~[spring-beans-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:230) ~[spring-beans-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:302) ~[spring-beans-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:197) ~[spring-beans-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:761) ~[spring-beans-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:867) ~[spring-context-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:543) ~[spring-context-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:693) [spring-boot-1.5.6.RELEASE.jar:1.5.6.RELEASE]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:360) [spring-boot-1.5.6.RELEASE.jar:1.5.6.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:303) [spring-boot-1.5.6.RELEASE.jar:1.5.6.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1118) [spring-boot-1.5.6.RELEASE.jar:1.5.6.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1107) [spring-boot-1.5.6.RELEASE.jar:1.5.6.RELEASE]
	at com.logicbig.example.ValueExampleMain.main(ValueExampleMain.java:16) [classes/:na]
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_111]
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:1.8.0_111]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_111]
	at java.lang.reflect.Method.invoke(Method.java:498) ~[na:1.8.0_111]
	at org.codehaus.mojo.exec.ExecJavaMojo$1.run(ExecJavaMojo.java:294) [exec-maven-plugin-1.5.0.jar:na]
	at java.lang.Thread.run(Thread.java:745) [na:1.8.0_111]
Caused by: org.springframework.beans.ConversionNotSupportedException: Failed to convert value of type 'java.lang.String' to required type 'java.util.concurrent.TimeUnit'; nested exception is java.lang.IllegalStateException: Cannot convert value of type 'java.lang.String' to required type 'java.util.concurrent.TimeUnit': no matching editors or conversion strategy found
	at org.springframework.beans.TypeConverterSupport.doConvert(TypeConverterSupport.java:74) ~[spring-beans-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.beans.TypeConverterSupport.convertIfNecessary(TypeConverterSupport.java:54) ~[spring-beans-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1092) ~[spring-beans-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1066) ~[spring-beans-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredFieldElement.inject(AutowiredAnnotationBeanPostProcessor.java:585) ~[spring-beans-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	... 24 common frames omitted
Caused by: java.lang.IllegalStateException: Cannot convert value of type 'java.lang.String' to required type 'java.util.concurrent.TimeUnit': no matching editors or conversion strategy found
	at org.springframework.beans.TypeConverterDelegate.convertIfNecessary(TypeConverterDelegate.java:306) ~[spring-beans-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.beans.TypeConverterDelegate.convertIfNecessary(TypeConverterDelegate.java:125) ~[spring-beans-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	at org.springframework.beans.TypeConverterSupport.doConvert(TypeConverterSupport.java:61) ~[spring-beans-4.3.10.RELEASE.jar:4.3.10.RELEASE]
	... 28 common frames omitted

[WARNING]
java.lang.reflect.InvocationTargetException
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.codehaus.mojo.exec.ExecJavaMojo$1.run(ExecJavaMojo.java:294)
	at java.lang.Thread.run(Thread.java:745)
Caused by: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'myBean': Unsatisfied dependency expressed through field 'refreshTimeUnit'; nested exception is org.springframework.beans.ConversionNotSupportedException: Failed to convert value of type 'java.lang.String' to required type 'java.util.concurrent.TimeUnit'; nested exception is java.lang.IllegalStateException: Cannot convert value of type 'java.lang.String' to required type 'java.util.concurrent.TimeUnit': no matching editors or conversion strategy found
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredFieldElement.inject(AutowiredAnnotationBeanPostProcessor.java:588)
	at org.springframework.beans.factory.annotation.InjectionMetadata.inject(InjectionMetadata.java:88)
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor.postProcessPropertyValues(AutowiredAnnotationBeanPostProcessor.java:366)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.populateBean(AbstractAutowireCapableBeanFactory.java:1264)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:553)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:483)
	at org.springframework.beans.factory.support.AbstractBeanFactory$1.getObject(AbstractBeanFactory.java:306)
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:230)
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:302)
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:197)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:761)
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:867)
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:543)
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:693)
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:360)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:303)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1118)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1107)
	at com.logicbig.example.ValueExampleMain.main(ValueExampleMain.java:16)
	... 6 more
Caused by: org.springframework.beans.ConversionNotSupportedException: Failed to convert value of type 'java.lang.String' to required type 'java.util.concurrent.TimeUnit'; nested exception is java.lang.IllegalStateException: Cannot convert value of type 'java.lang.String' to required type 'java.util.concurrent.TimeUnit': no matching editors or conversion strategy found
	at org.springframework.beans.TypeConverterSupport.doConvert(TypeConverterSupport.java:74)
	at org.springframework.beans.TypeConverterSupport.convertIfNecessary(TypeConverterSupport.java:54)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1092)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1066)
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredFieldElement.inject(AutowiredAnnotationBeanPostProcessor.java:585)
	... 24 more
Caused by: java.lang.IllegalStateException: Cannot convert value of type 'java.lang.String' to required type 'java.util.concurrent.TimeUnit': no matching editors or conversion strategy found
	at org.springframework.beans.TypeConverterDelegate.convertIfNecessary(TypeConverterDelegate.java:306)
	at org.springframework.beans.TypeConverterDelegate.convertIfNecessary(TypeConverterDelegate.java:125)
	at org.springframework.beans.TypeConverterSupport.doConvert(TypeConverterSupport.java:61)
	... 28 more
```

上面的输出表示,`seconds`不能被转换成`TimeUnit`枚举,为了解决这个问题,我们必须得用小写的`seconds`转换成`SECONDS`