# 040-理解条件配置-SpringProfiles

[TOC]

## 一言蔽之

Spring通过不同的Profiles来对配置文件进行分组， profile 可以通过启动脚本来修改

```
spring.profiles.active = dev
```

## Spring3.1条件配置

### 核心API

- org.springframework.core.env.ConfigurableEnvironment
  - 修改: addActiviteProfile(String) / setActivitieProfiles(String...) 和 setDefaultProfiles(String...)
  - 获取: getActiveProfiles 和 getDefaultProfiles()
  - 匹配:#acceptsProfiles(String...) 和 acceptsProfiles(Profiles)
-  注解  org.springframework.context.annotation.Profile

##  外部修改Profile

```java
--spring.profiles.active = even
-Dspring.profiles.active=even  // 通过java -D的方式
```

## 代码

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

    @Bean(name = "number")
//    @Profile("even") // 偶数
    @Conditional(EvenProfileCondition.class)
    public Integer even() {
        return 2;
    }

}
```

