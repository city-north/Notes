# 010-SpringBoot应用的划分

[TOC]

## 一言蔽之

SpringBoot应场景中,使用SpringApplication API 对程序进行引导启动

如果从应用类型上划分,可以分为

- Web应用
- 非Web应用

## Web应用和非Web应用

Web应用在Spring 2.0 开始, 支持

- Servlet 容器的实现 Spring Web MVC
- Reactive Web 容器实现 Spring WebFlux

## 如何切换Web应用和非Web应用

```
SpringApplicationBuilder builder = new SpringApplicationBuilder(HlsCoreApplication.class)
.web(WebApplicationType.NONE);
final SpringApplication build = builder.build(args);
build.setWebApplicationType(WebApplicationType.NONE);
```

的方式进行设置

在切换的同时要加入相应的Starter

#### Web应用的Starter

```xml
<dependency>            
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-web</artifactId>
</dependency>
```

#### WebFlux应用的Starter

```xml
<dependency>
  <groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-webflux</artifactId>
</dependency>
```

如果都不添加,则正常使用的是非Web应用运行

#### WebApplicationType类型

```java
	NONE,

	/**
	 * The application should run as a servlet-based web application and should start an
	 * embedded servlet web server.
	 */
	SERVLET,

	/**
	 * The application should run as a reactive web application and should start an
	 * embedded reactive web server.
	 */
	REACTIVE;
```

