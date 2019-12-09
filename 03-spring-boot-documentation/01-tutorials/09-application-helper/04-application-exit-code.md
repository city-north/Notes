# Application exit code

Spring 提供了多种方式在程序退出时返回一个[exit code](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/System.html#exit-int-) 

- 实现ExitCodeGenerator
- 监听ExitCodeEvent

## 实现ExitCodeGenerator

实现接口 `org. springframework. boot. Exit Code Generator` ,通过方法`getExitCode`返回一个自定义 code

我们需要显式地调用 SpringApplication.exit()方法

```java
@SpringBootConfiguration
public class ExampleExitCodeGenerator {

    @Bean
    MyBean myBean() {
        return new MyBean();
    }

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(ExampleExitCodeGenerator.class, args);

        MyBean myBean = context.getBean(MyBean.class);
        myBean.doSomething();

        int exitValue = SpringApplication.exit(context);
        System.exit(exitValue);
    }

    private static class MyBean implements ExitCodeGenerator {

        public void doSomething() {
            System.out.println("in doSomething()");
        }

        @Override
        public int getExitCode() {
            return 500;
        }
    }
```

```
 .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.5.3.RELEASE)

2017-05-17 11:56:07.831  INFO 10420 --- [enerator.main()] c.l.example.ExampleExitCodeGenerator     : Starting ExampleExitCodeGenerator on JoeMsi with PID 10420 (D:\LogicBig\example-projects\spring-boot\boot-application-exit-code\target\classes started by Joe in D:\LogicBig\example-projects\spring-boot\boot-application-exit-code)
2017-05-17 11:56:07.833  INFO 10420 --- [enerator.main()] c.l.example.ExampleExitCodeGenerator     : No active profile set, falling back to default profiles: default
2017-05-17 11:56:08.006  INFO 10420 --- [enerator.main()] c.l.example.ExampleExitCodeGenerator     : Started ExampleExitCodeGenerator in 0.381 seconds (JVM running for 4.602)
in doSomething()

Process finished with exit code 500
```

## 监听 ExitCodeEvent

```java
@SpringBootConfiguration
public class ExampleExitCodeEvent {

    @Bean
    MyBean myBean() {
        return new MyBean();
    }

    @Bean
    MyBean2 myBean2() {
        return new MyBean2();
    }

    public static void main(String[] args) {
        ApplicationContext context =
                SpringApplication.run(ExampleExitCodeEvent.class, args);
        MyBean myBean = context.getBean(MyBean.class);
        myBean.doSomething();

        int exit = SpringApplication.exit(context);
        System.exit(exit);
    }

    private static class MyBean implements ExitCodeGenerator {

        public void doSomething() {
            System.out.println("in doSomething()");
        }

        @Override
        public int getExitCode() {
            return 100;
        }
    }

    private static class MyBean2 {
        @EventListener
        public void exitEvent(ExitCodeEvent event) {
            System.out.println("-- ExitCodeEvent --");
            System.out.println("exit code: " + event.getExitCode());
        }
    }
}
```

#### 输出

```

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.5.3.RELEASE)

2017-05-17 11:56:10.921  INFO 9208 --- [odeEvent.main()] c.logicbig.example.ExampleExitCodeEvent  : Starting ExampleExitCodeEvent on JoeMsi with PID 9208 (D:\LogicBig\example-projects\spring-boot\boot-application-exit-code\target\classes started by Joe in D:\LogicBig\example-projects\spring-boot\boot-application-exit-code)
2017-05-17 11:56:10.924  INFO 9208 --- [odeEvent.main()] c.logicbig.example.ExampleExitCodeEvent  : No active profile set, falling back to default profiles: default
2017-05-17 11:56:11.111  INFO 9208 --- [odeEvent.main()] c.logicbig.example.ExampleExitCodeEvent  : Started ExampleExitCodeEvent in 0.379 seconds (JVM running for 3.028)
in doSomething()
-- ExitCodeEvent --
exit code: 100

Process finished with exit code 100
```

## Throwing a custom exception from run method of ApplicationRunner/CommandLineRunner

```java
@SpringBootConfiguration
public class ExampleExitCodeGeneratorException {
    @Bean
    ApplicationRunner appRunner() {
        return new MyAppRunner();
    }

    @Bean
    MyBean myBean() {
        return new MyBean();
    }

    public static void main(String[] args) {
        SpringApplication.run(ExampleExitCodeGeneratorException.class, args);
    }

    private static class MyAppRunner implements ApplicationRunner {

        @Override
        public void run(ApplicationArguments args) throws Exception {
            System.out.println("in ApplicationRunner#run method");
            throw new MyExitCodeException("test exception");
        }
    }

    private static class MyExitCodeException extends RuntimeException
            implements ExitCodeGenerator {

        public MyExitCodeException(String message) {
            super(message);
        }

        @Override
        public int getExitCode() {
            return 5;
        }
    }

    private static class MyBean {
        @EventListener
        public void exitEvent(ExitCodeEvent event) {
            System.out.println("-- ExitCodeEvent --");
            System.out.println("exit code: " + event.getExitCode());
        }
    }
}
```

#### output

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.5.3.RELEASE)

2017-05-17 11:56:14.097  INFO 12088 --- [xception.main()] c.l.e.ExampleExitCodeGeneratorException  : Starting ExampleExitCodeGeneratorException on JoeMsi with PID 12088 (D:\LogicBig\example-projects\spring-boot\boot-application-exit-code\target\classes started by Joe in D:\LogicBig\example-projects\spring-boot\boot-application-exit-code)
2017-05-17 11:56:14.099  INFO 12088 --- [xception.main()] c.l.e.ExampleExitCodeGeneratorException  : No active profile set, falling back to default profiles: default
2017-05-17 11:56:14.117  INFO 12088 --- [xception.main()] s.c.a.AnnotationConfigApplicationContext : Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@349186e0: startup date [Wed May 17 11:56:14 CDT 2017]; root of context hierarchy
in ApplicationRunner#run method
-- ExitCodeEvent --
exit code: 5
2017-05-17 11:56:14.273 ERROR 12088 --- [xception.main()] o.s.boot.SpringApplication               : Application startup failed

java.lang.IllegalStateException: Failed to execute ApplicationRunner
	at org.springframework.boot.SpringApplication.callRunner(SpringApplication.java:770) [spring-boot-1.5.3.RELEASE.jar:1.5.3.RELEASE]
	at org.springframework.boot.SpringApplication.callRunners(SpringApplication.java:757) [spring-boot-1.5.3.RELEASE.jar:1.5.3.RELEASE]
	at org.springframework.boot.SpringApplication.afterRefresh(SpringApplication.java:747) [spring-boot-1.5.3.RELEASE.jar:1.5.3.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:315) [spring-boot-1.5.3.RELEASE.jar:1.5.3.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1162) [spring-boot-1.5.3.RELEASE.jar:1.5.3.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1151) [spring-boot-1.5.3.RELEASE.jar:1.5.3.RELEASE]
	at com.logicbig.example.ExampleExitCodeGeneratorException.main(ExampleExitCodeGeneratorException.java:20) [classes/:na]
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_111]
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:1.8.0_111]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_111]
	at java.lang.reflect.Method.invoke(Method.java:498) ~[na:1.8.0_111]
	at org.codehaus.mojo.exec.ExecJavaMojo$1.run(ExecJavaMojo.java:294) [exec-maven-plugin-1.5.0.jar:na]
	at java.lang.Thread.run(Thread.java:745) [na:1.8.0_111]
Caused by: com.logicbig.example.ExampleExitCodeGeneratorException$MyExitCodeException: test exception
	at com.logicbig.example.ExampleExitCodeGeneratorException$MyAppRunner.run(ExampleExitCodeGeneratorException.java:28) ~[classes/:na]
	at org.springframework.boot.SpringApplication.callRunner(SpringApplication.java:767) [spring-boot-1.5.3.RELEASE.jar:1.5.3.RELEASE]
	... 12 common frames omitted

2017-05-17 11:56:14.273  INFO 12088 --- [xception.main()] s.c.a.AnnotationConfigApplicationContext : Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@349186e0: startup date [Wed May 17 11:56:14 CDT 2017]; root of context hierarchy
[WARNING] 
java.lang.reflect.InvocationTargetException
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.codehaus.mojo.exec.ExecJavaMojo$1.run(ExecJavaMojo.java:294)
	at java.lang.Thread.run(Thread.java:745)
Caused by: java.lang.IllegalStateException: Failed to execute ApplicationRunner
	at org.springframework.boot.SpringApplication.callRunner(SpringApplication.java:770)
	at org.springframework.boot.SpringApplication.callRunners(SpringApplication.java:757)
	at org.springframework.boot.SpringApplication.afterRefresh(SpringApplication.java:747)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:315)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1162)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1151)
	at com.logicbig.example.ExampleExitCodeGeneratorException.main(ExampleExitCodeGeneratorException.java:20)
	... 6 more
Caused by: com.logicbig.example.ExampleExitCodeGeneratorException$MyExitCodeException: test exception
	at com.logicbig.example.ExampleExitCodeGeneratorException$MyAppRunner.run(ExampleExitCodeGeneratorException.java:28)
	at org.springframework.boot.SpringApplication.callRunner(SpringApplication.java:767)
	... 12 more

Process finished with exit code 5
```

## Using ExitCodeExceptionMapper

ExitCodeExceptionMapper is a strategy interface that can be used to provide a mapping between exceptions and exit codes as shown in the following example.

```java
@SpringBootConfiguration
public class ExampleExitCodeExceptionMapper {

    @Bean
    MyBean myBean() {
        return new MyBean();
    }

    @Bean
    ApplicationRunner appRunner() {
        return new MyAppRunner();
    }

    @Bean
    ExitCodeExceptionMapper exitCodeExceptionMapper() {
        return exception -> {

            if (exception.getCause() instanceof MyException) {
                return 10;
            }
            return 1;
        };
    }

    public static void main(String[] args) {
        SpringApplication.run(ExampleExitCodeExceptionMapper.class, args);
    }

    private static class MyAppRunner implements ApplicationRunner {

        @Override
        public void run(ApplicationArguments args) throws Exception {
            System.out.println("in command line");
            if (true) throw new MyException("test");
        }
    }

    private static class MyBean {

        @EventListener
        public void exitEvent(ExitCodeEvent event) {
            System.out.println("-- ExitCodeEvent --");
            System.out.println("exit code: " + event.getExitCode());
        }
    }

    private static class MyException extends RuntimeException {

        public MyException(String message) {
            super(message);
        }

    }
}
```

#### output

```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.5.3.RELEASE)

2017-05-17 11:56:17.316  INFO 2640 --- [onMapper.main()] c.l.e.ExampleExitCodeExceptionMapper     : Starting ExampleExitCodeExceptionMapper on JoeMsi with PID 2640 (D:\LogicBig\example-projects\spring-boot\boot-application-exit-code\target\classes started by Joe in D:\LogicBig\example-projects\spring-boot\boot-application-exit-code)
2017-05-17 11:56:17.318  INFO 2640 --- [onMapper.main()] c.l.e.ExampleExitCodeExceptionMapper     : No active profile set, falling back to default profiles: default
2017-05-17 11:56:17.346  INFO 2640 --- [onMapper.main()] s.c.a.AnnotationConfigApplicationContext : Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@33d045f8: startup date [Wed May 17 11:56:17 CDT 2017]; root of context hierarchy
in command line
-- ExitCodeEvent --
exit code: 10
2017-05-17 11:56:17.519 ERROR 2640 --- [onMapper.main()] o.s.boot.SpringApplication               : Application startup failed

java.lang.IllegalStateException: Failed to execute ApplicationRunner
	at org.springframework.boot.SpringApplication.callRunner(SpringApplication.java:770) [spring-boot-1.5.3.RELEASE.jar:1.5.3.RELEASE]
	at org.springframework.boot.SpringApplication.callRunners(SpringApplication.java:757) [spring-boot-1.5.3.RELEASE.jar:1.5.3.RELEASE]
	at org.springframework.boot.SpringApplication.afterRefresh(SpringApplication.java:747) [spring-boot-1.5.3.RELEASE.jar:1.5.3.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:315) [spring-boot-1.5.3.RELEASE.jar:1.5.3.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1162) [spring-boot-1.5.3.RELEASE.jar:1.5.3.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1151) [spring-boot-1.5.3.RELEASE.jar:1.5.3.RELEASE]
	at com.logicbig.example.ExampleExitCodeExceptionMapper.main(ExampleExitCodeExceptionMapper.java:32) [classes/:na]
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_111]
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:1.8.0_111]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_111]
	at java.lang.reflect.Method.invoke(Method.java:498) ~[na:1.8.0_111]
	at org.codehaus.mojo.exec.ExecJavaMojo$1.run(ExecJavaMojo.java:294) [exec-maven-plugin-1.5.0.jar:na]
	at java.lang.Thread.run(Thread.java:745) [na:1.8.0_111]
Caused by: com.logicbig.example.ExampleExitCodeExceptionMapper$MyException: test
	at com.logicbig.example.ExampleExitCodeExceptionMapper$MyAppRunner.run(ExampleExitCodeExceptionMapper.java:40) ~[classes/:na]
	at org.springframework.boot.SpringApplication.callRunner(SpringApplication.java:767) [spring-boot-1.5.3.RELEASE.jar:1.5.3.RELEASE]
	... 12 common frames omitted

2017-05-17 11:56:17.520  INFO 2640 --- [onMapper.main()] s.c.a.AnnotationConfigApplicationContext : Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@33d045f8: startup date [Wed May 17 11:56:17 CDT 2017]; root of context hierarchy
[WARNING] 
java.lang.reflect.InvocationTargetException
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.lang.reflect.Method.invoke(Method.java:498)
	at org.codehaus.mojo.exec.ExecJavaMojo$1.run(ExecJavaMojo.java:294)
	at java.lang.Thread.run(Thread.java:745)
Caused by: java.lang.IllegalStateException: Failed to execute ApplicationRunner
	at org.springframework.boot.SpringApplication.callRunner(SpringApplication.java:770)
	at org.springframework.boot.SpringApplication.callRunners(SpringApplication.java:757)
	at org.springframework.boot.SpringApplication.afterRefresh(SpringApplication.java:747)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:315)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1162)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1151)
	at com.logicbig.example.ExampleExitCodeExceptionMapper.main(ExampleExitCodeExceptionMapper.java:32)
	... 6 more
Caused by: com.logicbig.example.ExampleExitCodeExceptionMapper$MyException: test
	at com.logicbig.example.ExampleExitCodeExceptionMapper$MyAppRunner.run(ExampleExitCodeExceptionMapper.java:40)
	at org.springframework.boot.SpringApplication.callRunner(SpringApplication.java:767)
	... 12 more

Process finished with exit code 10
```

