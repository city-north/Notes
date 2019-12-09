# 使用 SpringBoot 的 maven 插件

SpringBoot 框架提供了一个 maven 插件(spring-boot) 我们可以使用它完成:

- **spring-boot:run** 以 exploded 方式运行 springboot
- **spring-boot:repackage** to package executable jar and war files.



## 使用方式

在pom.xml 中引入这个插件

```xml
<project ..>
  ......
 <groupId>com.logicbig.example</groupId>
 <artifactId>boot-mvn-plugin-example</artifactId>
 <version>1.0-SNAPSHOT</version>

 <parent>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-parent</artifactId>
  <version>1.4.2.RELEASE</version>
 </parent>
 <dependencies>
  <dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
 </dependencies>

 <build>
  <plugins>
   <plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <version>1.4.2.RELEASE</version>
   </plugin>
  </plugins>
 </build>

</project>
```

#### 列出goals

使用

```
mvn help:describe
```

列出所有命令

```
D:\boot-mvn-plugin-example> mvn help:describe -Dplugin=spring-boot
  .....
[INFO] org.springframework.boot:spring-boot-maven-plugin:1.4.2.RELEASE

Name: Spring Boot Maven Plugin
Description: Spring Boot Maven Plugin
Group Id: org.springframework.boot
Artifact Id: spring-boot-maven-plugin
Version: 1.4.2.RELEASE
Goal Prefix: spring-boot

This plugin has 6 goals:

spring-boot:build-info
  Description: Generate a build-info.properties file based the content of the
    current MavenProject.

spring-boot:help
  Description: Display help information on spring-boot-maven-plugin.
    Call mvn spring-boot:help -Ddetail=true -Dgoal=
 
   to display
    parameter details.

spring-boot:repackage
  Description: Repackages existing JAR and WAR archives so that they can be
    executed from the command line using java -jar. With layout=NONE can also
    be used simply to package a JAR with nested dependencies (and no main
    class, so not executable).

spring-boot:run
  Description: Run an executable archive application.

spring-boot:start
  Description: Start a spring application. Contrary to the run goal, this
    does not block and allows other goal to operate on the application. This
    goal is typically used in integration test scenario where the application
    is started before a test suite and stopped after.

spring-boot:stop
  Description: Stop a spring application that has been started by the 'start'
    goal. Typically invoked once a test suite has completed.

For more information, run 'mvn help:describe [...] -Ddetail'

 .....

 
```

#### 将应用打包成可执行 jar

Running **mvn package spring-boot:repackage** will create the executable jar.

```
D:\boot-mvn-plugin-example>mvn package spring-boot:repackage
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building boot-mvn-plugin-example 1.0-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] --- maven-resources-plugin:2.6:resources (default-resources) @ boot-mvn-plugin-example ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] skip non existing resourceDirectory D:\boot-mvn-plugin-example\src\main\resources
[INFO] skip non existing resourceDirectory D:\boot-mvn-plugin-example\src\main\resources
[INFO]
[INFO] --- maven-compiler-plugin:3.1:compile (default-compile) @ boot-mvn-plugin-example ---
[INFO] Nothing to compile - all classes are up to date
[INFO]
[INFO] --- maven-resources-plugin:2.6:testResources (default-testResources) @ boot-mvn-plugin-example ---
[INFO] Using 'UTF-8' encoding to copy filtered resources.
[INFO] skip non existing resourceDirectory D:\boot-mvn-plugin-example\src\test\resources
[INFO]
[INFO] --- maven-compiler-plugin:3.1:testCompile (default-testCompile) @ boot-mvn-plugin-example ---
[INFO] No sources to compile
[INFO]
[INFO] --- maven-surefire-plugin:2.18.1:test (default-test) @ boot-mvn-plugin-example ---
[INFO] No tests to run.
[INFO]
[INFO] --- maven-jar-plugin:2.6:jar (default-jar) @ boot-mvn-plugin-example ---
[INFO] Building jar: D:\boot-mvn-plugin-example\target\boot-mvn-plugin-example-1.0-SNAPSHOT.jar
[INFO]
[INFO] --- spring-boot-maven-plugin:1.4.2.RELEASE:repackage (default) @ boot-mvn-plugin-example ---
[INFO]
[INFO] --- spring-boot-maven-plugin:1.4.2.RELEASE:repackage (default-cli) @ boot-mvn-plugin-example ---
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 1.966 s
[INFO] Finished at: 2016-12-08T21:51:49-06:00
[INFO] Final Memory: 18M/309M
[INFO] ------------------------------------------------------------------------
```

值得注意的是我们在 pim.xml 中添加了 repackage 的 goals

```
<project ....>

......
    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>1.4.2.RELEASE</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>

</project>
```

执行

```java
mvn package
```

## 多个 mainClass 冲突

SpringBoot 会自动找到 main 类并且执行,如果多于一个 main class就会报错

```
[ERROR] Failed to execute goal org.springframework.boot:spring-boot-maven-plugin:1.4.2.RELEASE:run (default-cli) on project boot-mvn-plugin-example: Execution default-cli of goal org.springframework.boot:spring-boot-maven-plugin:1.4.2.RELEASE:run failed: Unable to find a single main class from the following candidates [com.logicbig.example.MainClass2, com.logicbig.example.MainClass] -> [Help 1]

```

引入 mainClass

```
[ERROR] Failed to execute goal org.springframework.boot:spring-boot-maven-plugin:1.4.2.RELEASE:run (default-cli) on project boot-mvn-plugin-example: Execution default-cli of goal org.springframework.boot:spring-boot-maven-plugin:1.4.2.RELEASE:run failed: Unable to find a single main class from the following candidates [com.logicbig.example.MainClass2, com.logicbig.example.MainClass] -> [Help 1]

```

我们可以添加一个通配符

```
<plugin>
     <groupId>org.springframework.boot</groupId>
     <artifactId>spring-boot-maven-plugin</artifactId>
     <version>1.4.2.RELEASE</version>
     <configuration>
       <mainClass>${myMainClass}</mainClass>
</configuration>
</plugin>
```

使用命令

```
mvn spring-boot:run -DmyMainClass=com.logicbig.example.MainClass2
```

