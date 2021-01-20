# 030-SpringBoot可执行Jar资源结构

[TOC]

## 可执行jar的打包方式

SpringBoot的打包实际上是通过定制maven的打包插件方式打成jar包

比如在pom.xml中

```xml
	<build>
		<plugins>
			<plugin>
				<groupId>org.springframework.boot</groupId>
				<artifactId>spring-boot-maven-plugin</artifactId>
			</plugin>
		</plugins>
	</build>
```

我们看到是没有版本信息的,原因是 springBoot 使用了固化的 maven 管理机制:

`spring-boot-starter-parent-2.2.6.RELEASE.pom`文件中实际上存储了依赖版本

```xml
<plugin>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-maven-plugin</artifactId>
  <executions>
    <execution>
      <id>repackage</id>
      <goals>
        <goal>repackage</goal>
      </goals>
    </execution>
  </executions>
  <configuration>
    <mainClass>${start-class}</mainClass>
  </configuration>
</plugin>
```

## 生成的文件

- first-spring-boot-application-0.0.1-SNAPSHOT.jar : 可执行Jar文件
- first-spring-boot-application-0.0.1-SNAPSHOT.jar.original : 正常jar文件

使用 mvn clean package 之后: 

![image-20200506121734300](../../assets/image-20200506121734300.png)

## 生成的文件目录

```
$ tree -d
.
├── BOOT-INF
│   ├── classes  // 存放应用编译后的 class 文件
│   │   └── vip
│   │       └── ericchen
│   │           └── study
│   │               └── firstspringbootapplication
│   └── lib  // 目录存放应用依赖的 jar 包
├── META-INF //存放的是应用相关的元信息 , 可执行的 jar 包都有 `MANIFEST.MF`文件
│   ├── MANIFEST.MF
│   └── maven
│       └── vip.ericchen.study
│           └── first-spring-boot-application
└── org
    └── springframework
        └── boot
            └── loader // SpringBoot 引导相关文件,在 maven 插件打包时就添加进去了
                ├── archive
                ├── data
                ├── jar
                └── util

19 directories
```

#### 分析一下目录结构

- BOOT-INF/classes 存放应用编译后的 class 文件
- BOOT-INF/lib  目录存放应用依赖的 jar 包
- META-INF 存放的是应用相关的元信息 , 可执行的 jar 包都有 `MANIFEST.MF`文件
- org/springframework/boot/loader SpringBoot 引导相关文件,在 maven 插件打包时就添加进去了



也就是jarLauncher去引导启动的类,执行 main 方法

