# `@ConfigurationProperties `Validation by using JSR-303/349 Annotations

使用 JSR-303/349校验`@ConfigurationProperties `

在 SpringBoot 应用中,我们可以`JSR-303/349`类去校验` @ConfigurationProperties`类的属性

- 使用`@Validated`注解
- classpath 下有`JSR-303/349`的实现

## Example

添加 hibernate validator 校验依赖

- Hibernate validator 是` JSR-303/349`的实现

```
<dependency>
   <groupId>org.hibernate</groupId>
   <artifactId>hibernate-validator</artifactId>
</dependency>
```

- 不添加版本,因为 Spring 类默认就添加了,继承`spring-boot-starter-parent`

### 外部属性

#### src/main/resources/application.properties

```
spring.main.banner-mode=off 
spring.main.logStartupInfo=false
app.admin-contact-number=111-111-111
app.refresh-rate=0
```

### @ConfigurationProperties class

```java
@Component
@ConfigurationProperties("app")
@Validated
public class MyAppProperties {
  @Pattern(regexp = "\\d{3}-\\d{3}-\\d{4}")
  private String adminContactNumber;
  @Min(1)
  private int refreshRate;
    .............
}
```

输出

```
@SpringBootApplication
public class ExampleMain {

  public static void main(String[] args) throws InterruptedException {
      ConfigurableApplicationContext context = SpringApplication.run(ExampleMain.class, args);
      MyAppProperties bean = context.getBean(MyAppProperties.class);
      System.out.println(bean);
  }
}
```

```
2017-08-09 15:01:32.304  INFO 14256 --- [mpleMain.main()] s.c.a.AnnotationConfigApplicationContext : Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@4f1072ab: startup date [Wed Aug 09 15:01:32 CDT 2017]; root of context hierarchy
2017-08-09 15:01:32.572  WARN 14256 --- [mpleMain.main()] o.h.v.m.ParameterMessageInterpolator     : HV000184: ParameterMessageInterpolator has been chosen, EL interpolation will not be supported
2017-08-09 15:01:32.636  WARN 14256 --- [mpleMain.main()] o.h.v.m.ParameterMessageInterpolator     : HV000184: ParameterMessageInterpolator has been chosen, EL interpolation will not be supported
2017-08-09 15:01:32.706 ERROR 14256 --- [mpleMain.main()] o.s.b.b.PropertiesConfigurationFactory   : Properties configuration failed validation
2017-08-09 15:01:32.707 ERROR 14256 --- [mpleMain.main()] o.s.b.b.PropertiesConfigurationFactory   : Field error in object 'app' on field 'adminContactNumber': rejected value [111-111-111]; codes [Pattern.app.adminContactNumber,Pattern.adminContactNumber,Pattern.java.lang.String,Pattern]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [app.adminContactNumber,adminContactNumber]; arguments []; default message [adminContactNumber],[Ljavax.validation.constraints.Pattern$Flag;@57cd9090,org.springframework.validation.beanvalidation.SpringValidatorAdapter$ResolvableAttribute@5f1d413f]; default message [must match "\d{3}-\d{3}-\d{4}"]
2017-08-09 15:01:32.707 ERROR 14256 --- [mpleMain.main()] o.s.b.b.PropertiesConfigurationFactory   : Field error in object 'app' on field 'refreshRate': rejected value [0]; codes [Min.app.refreshRate,Min.refreshRate,Min.int,Min]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [app.refreshRate,refreshRate]; arguments []; default message [refreshRate],1]; default message [must be greater than or equal to 1]
2017-08-09 15:01:32.707  WARN 14256 --- [mpleMain.main()] s.c.a.AnnotationConfigApplicationContext : Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'myAppProperties': Could not bind properties to MyAppProperties (prefix=app, ignoreInvalidFields=false, ignoreUnknownFields=true, ignoreNestedProperties=false); nested exception is org.springframework.validation.BindException: org.springframework.boot.bind.RelaxedDataBinder$RelaxedBeanPropertyBindingResult: 2 errors
Field error in object 'app' on field 'adminContactNumber': rejected value [111-111-111]; codes [Pattern.app.adminContactNumber,Pattern.adminContactNumber,Pattern.java.lang.String,Pattern]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [app.adminContactNumber,adminContactNumber]; arguments []; default message [adminContactNumber],[Ljavax.validation.constraints.Pattern$Flag;@57cd9090,org.springframework.validation.beanvalidation.SpringValidatorAdapter$ResolvableAttribute@5f1d413f]; default message [must match "\d{3}-\d{3}-\d{4}"]
Field error in object 'app' on field 'refreshRate': rejected value [0]; codes [Min.app.refreshRate,Min.refreshRate,Min.int,Min]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [app.refreshRate,refreshRate]; arguments []; default message [refreshRate],1]; default message [must be greater than or equal to 1]
2017-08-09 15:01:32.711  INFO 14256 --- [mpleMain.main()] utoConfigurationReportLoggingInitializer : 

Error starting ApplicationContext. To display the auto-configuration report re-run your application with 'debug' enabled.
2017-08-09 15:01:32.713 ERROR 14256 --- [mpleMain.main()] o.s.b.d.LoggingFailureAnalysisReporter   : 

***************************
APPLICATION FAILED TO START
***************************

Description:

Binding to target MyAppProperties{adminContactNumber='111-111-111', refreshRate=0} failed:

    Property: app.adminContactNumber
    Value: 111-111-111
    Reason: must match "\d{3}-\d{3}-\d{4}"

    Property: app.refreshRate
    Value: 0
    Reason: must be greater than or equal to 1


Action:

Update your application's configuration

[WARNING] 
java.lang.reflect.InvocationTargetException
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.codehaus.mojo.exec.ExecJavaMojo$1.run(ExecJavaMojo.java:294)
	at java.lang.Thread.run(Thread.java:745)
Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'myAppProperties': Could not bind properties to MyAppProperties (prefix=app, ignoreInvalidFields=false, ignoreUnknownFields=true, ignoreNestedProperties=false); nested exception is org.springframework.validation.BindException: org.springframework.boot.bind.RelaxedDataBinder$RelaxedBeanPropertyBindingResult: 2 errors
Field error in object 'app' on field 'adminContactNumber': rejected value [111-111-111]; codes [Pattern.app.adminContactNumber,Pattern.adminContactNumber,Pattern.java.lang.String,Pattern]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [app.adminContactNumber,adminContactNumber]; arguments []; default message [adminContactNumber],[Ljavax.validation.constraints.Pattern$Flag;@57cd9090,org.springframework.validation.beanvalidation.SpringValidatorAdapter$ResolvableAttribute@5f1d413f]; default message [must match "\d{3}-\d{3}-\d{4}"]
Field error in object 'app' on field 'refreshRate': rejected value [0]; codes [Min.app.refreshRate,Min.refreshRate,Min.int,Min]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [app.refreshRate,refreshRate]; arguments []; default message [refreshRate],1]; default message [must be greater than or equal to 1]
	at org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor.postProcessBeforeInitialization(ConfigurationPropertiesBindingPostProcessor.java:334)
	at org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor.postProcessBeforeInitialization(ConfigurationPropertiesBindingPostProcessor.java:291)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.applyBeanPostProcessorsBeforeInitialization(AbstractAutowireCapableBeanFactory.java:409)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.initializeBean(AbstractAutowireCapableBeanFactory.java:1620)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:555)
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
	at com.logicbig.example.ExampleMain.main(ExampleMain.java:11)
	... 6 more
Caused by: org.springframework.validation.BindException: org.springframework.boot.bind.RelaxedDataBinder$RelaxedBeanPropertyBindingResult: 2 errors
Field error in object 'app' on field 'adminContactNumber': rejected value [111-111-111]; codes [Pattern.app.adminContactNumber,Pattern.adminContactNumber,Pattern.java.lang.String,Pattern]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [app.adminContactNumber,adminContactNumber]; arguments []; default message [adminContactNumber],[Ljavax.validation.constraints.Pattern$Flag;@57cd9090,org.springframework.validation.beanvalidation.SpringValidatorAdapter$ResolvableAttribute@5f1d413f]; default message [must match "\d{3}-\d{3}-\d{4}"]
Field error in object 'app' on field 'refreshRate': rejected value [0]; codes [Min.app.refreshRate,Min.refreshRate,Min.int,Min]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [app.refreshRate,refreshRate]; arguments []; default message [refreshRate],1]; default message [must be greater than or equal to 1]
	at org.springframework.boot.bind.PropertiesConfigurationFactory.checkForBindingErrors(PropertiesConfigurationFactory.java:359)
	at org.springframework.boot.bind.PropertiesConfigurationFactory.doBindPropertiesToTarget(PropertiesConfigurationFactory.java:276)
	at org.springframework.boot.bind.PropertiesConfigurationFactory.bindPropertiesToTarget(PropertiesConfigurationFactory.java:240)
	at org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor.postProcessBeforeInitialization(ConfigurationPropertiesBindingPostProcessor.java:329)
	... 24 more
```

