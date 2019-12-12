# Profile-specific Logback configuration selection in logback-spring.xml file

Spring Boot allows to use` <springProfile>` tag to conditionally include or exclude sections of configurations based on the active Spring profiles. Profile sections are supported within the `<configuration>` element of logback configuration file.

# Example

#### src\main\resources\logback-spring.xml

```
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appender name="stdout" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <springProfile name="dev">
                <pattern>%d{yy-MMMM-dd HH:mm:ss:SSS} %5p %t %c{2}:%L - %m%n</pattern>
            </springProfile>
            <springProfile name="prod">
                <pattern>%d{yy-MM-dd E HH:mm:ss.SSS} %5p %t %c{2}:%L - %m%n</pattern>
            </springProfile>
        </encoder>
    </appender>
    <springProfile name="dev">
        <root level="DEBUG">
            <appender-ref ref="stdout"/>
        </root>
    </springProfile>
    <springProfile name="prod">
        <root level="INFO">
            <appender-ref ref="stdout"/>
        </root>
    </springProfile>
</configuration>
```

#### src/main/resources/application.properties

```
spring.main.banner-mode=off
spring.main.logStartupInfo=false
spring.profiles.active=prod
@SpringBootApplication
public class ExampleMain {
  private static final Logger logger = LoggerFactory.getLogger(ExampleMain.class);

  public static void main(String[] args) throws InterruptedException {
      SpringApplication.run(ExampleMain.class, args);
      logger.info("just a test info log");
  }
}
```

#### Output

```
17-11-08 Wed 22:30:45.689  INFO main o.s.c.a.AnnotationConfigApplicationContext:583 - Refreshing org.springframework.context.annotation.AnnotationConfigApplicationContext@551bdc27: startup date [Wed Nov 08 22:30:45 CST 2017]; root of context hierarchy
17-11-08 Wed 22:30:46.115  INFO main o.s.j.e.a.AnnotationMBeanExporter:431 - Registering beans for JMX exposure on startup
17-11-08 Wed 22:30:46.126  INFO main c.l.e.ExampleMain:14 - just a test info log
17-11-08 Wed 22:30:46.127  INFO Thread-2 o.s.c.a.AnnotationConfigApplicationContext:984 - Closing org.springframework.context.annotation.AnnotationConfigApplicationContext@551bdc27: startup date [Wed Nov 08 22:30:45 CST 2017]; root of context hierarchy
17-11-08 Wed 22:30:46.128  INFO Thread-2 o.s.j.e.a.AnnotationMBeanExporter:449 - Unregistering JMX-exposed beans on shutdown
```

Let's use 'dev' as active profile:

```
spring.main.banner-mode=off
spring.main.logStartupInfo=false
spring.profiles.active=dev
17-November-08 22:39:10:983 DEBUG main o.s.b.l.ClasspathLoggingApplicationListener:52 - Application started with classpath: [file:/C:/Program%20Files/Java/jdk1.8.0_65/jre/lib/charsets.jar, .......
17-November-08 22:39:11:001 DEBUG main o.s.c.e.PropertySourcesPropertyResolver:92 - Could not find key 'context.listener.classes' in any property source
17-November-08 22:39:11:020 DEBUG main o.s.c.e.StandardEnvironment:106 - Adding PropertySource 'systemProperties' with lowest search precedence
17-November-08 22:39:11:020 DEBUG main o.s.c.e.StandardEnvironment:106 - Adding PropertySource 'systemEnvironment' with lowest search precedence
17-November-08 22:39:11:020 DEBUG main o.s.c.e.StandardEnvironment:124 - Initialized StandardEnvironment with PropertySources [MapPropertySource@1173643169 {name='systemProperties', properties={java.runtime.name=Java(TM) SE Runtime Environment, sun.boot.library.path=C:\Program Files\Java\jdk1.8.0_65\jre\bin, .......................................
17-November-08 22:39:11:067 DEBUG main o.s.c.e.PropertySourcesPropertyResolver:92 - Could not find key 'context.initializer.classes' in any property source
17-November-08 22:39:11:067 DEBUG main o.s.c.e.PropertySourcesPropertyResolver:92 - Could not find key 'spring.config.name:application' in any property source
17-November-08 22:39:11:067 DEBUG main o.s.c.e.PropertySourcesPropertyResolver:92 - Could not find key 'spring.config.name' in any property source
17-November-08 22:39:11:067 DEBUG main o.s.c.e.PropertySourcesPropertyResolver:92 - Could not find key 'vcap.application.name:application' in any property source
17-November-08 22:39:11:067 DEBUG main o.s.c.e.PropertySourcesPropertyResolver:92 - Could not find key 'vcap.application.name' in any property source
17-November-08 22:39:11:067 DEBUG main o.s.c.e.PropertySourcesPropertyResolver:92 - Could not find key 'spring.application.name:application' in any property source
17-November-08 22:39:11:067 DEBUG main o.s.c.e.PropertySourcesPropertyResolver:92 - Could not find key 'spring.application.name' in any property source
17-November-08 22:39:11:067 DEBUG main o.s.c.e.PropertySourcesPropertyResolver:92 - Could not find key 'PORT:null' in any property source
17-November-08 22:39:11:067 DEBUG main o.s.c.e.PropertySourcesPropertyResolver:92 - Could not find key 'PORT' in any property source
17-November-08 22:39:11:067 DEBUG main o.s.c.e.PropertySourcesPropertyResolver:92 - Could not find key 'server.port:null' in any property source
17-November-08 22:39:11:067 DEBUG main o.s.c.e.PropertySourcesPropertyResolver:92 - Could not find key 'server.port' in any property source
17-November-08 22:39:11:067 DEBUG main o.s.c.e.PropertySourcesPropertyResolver:92 - Could not find key 'spring.application.index:null' in any property source
17-November-08 22:39:11:067 DEBUG main o.s.c.e.PropertySourcesPropertyResolver:92 - Could not find key 'spring.application.index' in any property source
17-November-08 22:39:11:067 DEBUG main o.s.c.e.PropertySourcesPropertyResolver:92 - Could not find key 'vcap.application.instance_index:null' in any property source
17-November-08 22:39:11:067 DEBUG main o.s.c.e.PropertySourcesPropertyResolver:92 - Could not find key 'vcap.application.instance_index' in any property source
17-November-08 22:39:11:067 DEBUG main o.s.b.f.s.DefaultListableBeanFactory:251 - Returning cached instance of singleton bean 'autoConfigurationReport'
17-November-08 22:39:11:067 DEBUG main o.s.b.SpringApplication:621 - Loading source class com.logicbig.example.ExampleMain
17-November-08 22:39:11:121 DEBUG main o.s.b.c.c.ConfigFileApplicationListener:172 - Activated profiles dev
17-November-08 22:39:11:121 DEBUG main o.s.b.c.c.ConfigFileApplicationListener:172 - Loaded config file 'file:/D:/example-projects/spring-boot/boot-profile-and-logging-config-file/target/classes/application.properties' (classpath:/application.properties)
    ................
```