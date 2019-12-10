# Loading application property files from Current Directory

在本例中:我们要学习如何加载 SpringBoot 属性文件

- 从当前路径中
- 从当前路径的/config 子路径中

## 书写一个简单的 Boot 应用

```java
@SpringBootConfiguration
public class ExampleMain {
    @Bean
    MyBean myBean() {
        return new MyBean();
    }

    public static void main(String[] args) throws InterruptedException {
        SpringApplication bootApp = new SpringApplication(ExampleMain.class);
        bootApp.setBannerMode(Banner.Mode.OFF);
        bootApp.setLogStartupInfo(false);
        ConfigurableApplicationContext context = bootApp.run(args);
        MyBean myBean = context.getBean(MyBean.class);
        myBean.startApplication();
    }

    private static class MyBean {

        @Value("${app.title}")
        private String appTitle;

        public void startApplication() {
            System.out.printf("-- in MyBean app title: %s --%n", appTitle);
        }
    }
}
```

在 pom.xml 同级目录昂志 properties 文件

#### application.properties

```
app.title=My Spring Application
```

## 使用 Boot maven 插件运行

```
mvn -q spring-boot:run
```



```
c:\example-projects\spring-boot\boot-current-directory-prop-file>mvn -q spring-boot:run
-- in MyBean app title: My Spring Application --
```

## Running executable jar

```
mvn -q clean package spring-boot:repackage
```

```
c:\example-projects\spring-boot\boot-current-directory-prop-file>mvn -q clean package spring-boot:repackage
```

### Placing the property file at 'current directory' location

```
copy application.properties target\application.properties
```

```
c:\example-projects\spring-boot\boot-current-directory-prop-file>copy application.properties target\application.properties
        1 file(s) copied.
```

```
java -jar boot-current-directory-prop-file-1.0-SNAPSHOT.jar
```

```
D:\example-projects\spring-boot\boot-current-directory-prop-file>cd target

D:\example-projects\spring-boot\boot-current-directory-prop-file\target>dir
 Volume in drive D is Data
 Volume Serial Number is 68F9-EDFA

 Directory of D:\example-projects\spring-boot\boot-current-directory-prop-file\target

07/12/2017  10:48 PM    <DIR>          .
07/12/2017  10:48 PM    <DIR>          ..
07/06/2017  07:26 PM                31 application.properties
07/12/2017  10:41 PM         6,651,337 boot-current-directory-prop-file-1.0-SNAPSHOT.jar
07/12/2017  10:41 PM             4,428 boot-current-directory-prop-file-1.0-SNAPSHOT.jar.original
07/12/2017  10:41 PM    <DIR>          classes
07/12/2017  10:41 PM    <DIR>          generated-sources
07/12/2017  10:41 PM    <DIR>          maven-archiver
07/12/2017  10:41 PM    <DIR>          maven-status
               3 File(s)      6,655,796 bytes
               6 Dir(s)  60,773,568,512 bytes free

D:\example-projects\spring-boot\boot-current-directory-prop-file\target>java -jar boot-current-directory-prop-file-1.0-SNAPSHOT.jar
-- in MyBean app title: My Spring Application --

D:\example-projects\spring-boot\boot-current-directory-prop-file\target>
```