# 050-Spring模式注解-Stereotype-Annotations

[TOC]

## 理解@Component派生性

模式注解(stereotype annotation)是一个注解，用来声明一个组件在应用程序中所扮演的角色。

例如，Spring框架中的@Repository注释是任何满足存储库角色或*原型*(也称为数据访问对象或DAO)的类的标记。

**@Component 是任何spring管理组件的通用原型。**

任何带“@Component”注解的组件都可以进行组件扫描。类似地，任何被注释了一个本身用“@Component”进行元注释的注释的组件也可以进行组件扫描。例如，' @Service '是用' @Component '做元注释的。

Spring Core提供了几种开箱即用的构造型注释，包括但不限于:

- @Component
- @Service
- @Repository
-  @Controller
-  @RestController
-  @Configuration
-  @Repository
-  @Service 

等都是 @Component 的派生。

元标注@Component的注解在XML元素=<context:context-scan> 或者注解 `@ComponentScan`扫描中派生了`Conponent`的特性，并且从Spring Framework 4.0 之后开始支持多层次的派生性

注解之间没有继承关系,只能通过元标注的方式来说明之间的层次关系

## 举例说明

- @Repository
- @Service
- @Controller
- @Configuration
- @SpringBootConfiguration(Spring Boot) 多层次派生

```java
@MyComponentScan2(basePackages = "org.geekbang.thinking.in.spring.annotation") // 指定 Class-Path(s)
//@ComponentScan(value = "org.geekbang.thinking.in.spring.annotation") // 指定 Class-Path(s)
public class ComponentScanDemo {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        // 注册 Configuration Class
        context.register(ComponentScanDemo.class);

        // 启动 Spring 应用上下文
        context.refresh();

        // 依赖查找 TestClass Bean
        // TestClass 标注 @MyComponent2
        // @MyComponent2 <- @MyComponent <- @Component
        // 从 Spring 4.0 开始支持多层次 @Component "派生"
        TestClass testClass = context.getBean(TestClass.class);

        // Annotation -> AnnotationAttributes(Map)

        System.out.println(testClass);

        // 关闭 Spring 应用上下文
        context.close();
    }
}

```











