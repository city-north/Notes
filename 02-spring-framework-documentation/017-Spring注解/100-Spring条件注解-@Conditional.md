# 100-Spring条件注解-@Conditional

[TOC]

## 什么是条件注解



### Spring条件注解

- 基于配置条件注解-org.springframework.context.annotation.Profile
  - 关联对象-org.springframework.core.env.Environment 中的Profiles
  - 实现变化：从Spring 4.0 开始，@Profile基于 @Conditional实现
- 基于编程条件注解-org.springframework.context.annotation.Conditional
  - 关联对象-org.springframework.context.annotation.Condition具体实现

### Conditional实现原理

- 上下文对象-org.springframework.context.annotation.ConditionContext
- 条件判断-org.springframework.context.annotation.ConditionEvaluator
- 配置阶段-org.springframework.context.annotation.ConfigurationCondition
- 判断入口- org.springframework.context.annotation.ConfigurationClassPostProcessor
  - org.springframework.context.annotation.ConfigurationClassParser

## 配置格式

- 基于配置(setDefaultProfile)
- 基于编程条件（Conditional)

## 基于配置条件注解-org.springframework.context.annotation.Profile

实现变化：从Spring 4.0 开始，@Profile基于 @Conditional重构

```java
@Configuration
public class ProfileDemo {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        // 注册 Configuration Class
        context.register(ProfileDemo.class);

        // 获取 Environment 对象（可配置的）
        ConfigurableEnvironment environment = context.getEnvironment();
        // 默认 profiles = [ "odd" ] （兜底 profiles)
        environment.setDefaultProfiles("odd");
        // 增加活跃 profiles
//        environment.addActiveProfile("even");

        // --spring.profiles.active = even
        // -Dspring.profiles.active=even

        // 启动 Spring 应用上下文
        context.refresh();

        Integer number = context.getBean("number", Integer.class);

        System.out.println(number);

        // 关闭 Spring 应用上下文
        context.close();
    }

    @Bean(name = "number")
    @Profile("odd") // 奇数
    public Integer odd() {
        return 1;
    }

}
```



## 基于编程条件注解-org.springframework.context.annotation.Conditional

```java
@Configuration
public class ProfileDemo {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        // 注册 Configuration Class
        context.register(ProfileDemo.class);

        // 获取 Environment 对象（可配置的）
        ConfigurableEnvironment environment = context.getEnvironment();
        // 默认 profiles = [ "odd" ] （兜底 profiles)
        environment.setDefaultProfiles("odd");
        // 增加活跃 profiles
//        environment.addActiveProfile("even");

        // --spring.profiles.active = even
        // -Dspring.profiles.active=even

        // 启动 Spring 应用上下文
        context.refresh();

        Integer number = context.getBean("number", Integer.class);

        System.out.println(number);

        // 关闭 Spring 应用上下文
        context.close();
    }


    @Bean(name = "number")
    @Conditional(EvenProfileCondition.class)
    public Integer even() {
        return 2;
    }
}
```

条件

```java
public class EvenProfileCondition implements Condition {

    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // 条件上下文
        Environment environment = context.getEnvironment();
        return environment.acceptsProfiles("even");
    }
}

```

