# 不继承 SpringBoot 提供的父 starter 

我们可以使用`spring-boot-dependencies`来替换掉**spring-boot-starter-parent**.

设置:

- scope = import
- type  = pom

如下:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project ......>
 <modelVersion>4.0.0</modelVersion>

 <groupId>com.logicbig.example</groupId>
 <artifactId>spring-boot-parent-pom-import</artifactId>
 <version>1.0-SNAPSHOT</version>

 <dependencyManagement>
 <dependencies>
  <dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-dependencies</artifactId>
   <version>1.4.2.RELEASE</version>
   <type>pom</type>
   <scope>import</scope>
  </dependency>
 </dependencies>
 </dependencyManagement>

 <dependencies>
  <dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter</artifactId>
  </dependency>
  <dependency>
   <groupId>commons-collections</groupId>
   <artifactId>commons-collections</artifactId>
  </dependency>
 </dependencies>

</project>
```



