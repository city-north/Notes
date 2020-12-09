# 130-基于Properties文件装载外部化配置

[TOC]

## 基于Properties文件装载外部化配置详情

- propertySource是有优先级的，先到的会优先覆盖掉后到的

- 可以在运行期间通过Environment机制动态增加或者减少PropertySource
  - SpringBoot
  - SpringCloud

```java
//参数
Map<String, Object> propertiesSource = new HashMap<>();
propertiesSource.put("user.name", "xiaomage");
org.springframework.core.env.PropertySource propertySource = new MapPropertySource("first-property-source", propertiesSource);
//添加到第一个
context.getEnvironment().getPropertySources().addFirst(propertySource);
```

## DEMO

```java
/**
 * 外部化配置示例
 */
@PropertySource("classpath:/META-INF/user-bean-definitions.properties")
public class PropertySourceDemo {

    /**
     * user.name 是 Java Properties 默认存在，当前用户：mercyblitz，而非配置文件中定义"小马哥"
     *
     * @param id
     * @param name
     * @return
     */
    @Bean
    public User user(@Value("${user.id}") Long id, @Value("${user.name}") String name) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        return user;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();

        // 扩展 Environment 中的 PropertySources
        // 添加 PropertySource 操作必须在 refresh 方法之前完成
        Map<String, Object> propertiesSource = new HashMap<>();
        propertiesSource.put("user.name", "xiaomage");
        org.springframework.core.env.PropertySource propertySource = new MapPropertySource("first-property-source", propertiesSource);
        context.getEnvironment().getPropertySources().addFirst(propertySource);

        // 注册当前类作为 Configuration Class
        context.register(PropertySourceDemo.class);
        // 启动 Spring 应用上下文
        context.refresh();
        // beanName 和 bean 映射
        Map<String, User> usersMap = context.getBeansOfType(User.class);
        for (Map.Entry<String, User> entry : usersMap.entrySet()) {
            System.out.printf("User Bean name : %s , content : %s \n", entry.getKey(), entry.getValue());
        }
        System.out.println(context.getEnvironment().getPropertySources());
        // 关闭 Spring 应用上下文
        context.close();
    }
}

```

