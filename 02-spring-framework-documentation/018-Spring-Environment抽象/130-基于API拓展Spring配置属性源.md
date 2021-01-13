# 130-基于API拓展Spring配置属性源

[TOC]

## Spring应用上下文启动前装配 PropertySource

## Spring应用山下文启动之后装配PropertySource

```java
public class EnvironmentPropertySourceChangeDemo {

    @Value("${user.name}")  // 不具备动态更新能力
    private String userName;

    // PropertySource("first-property-source") { user.name = 小马哥}
    // PropertySource( Java System Properties) { user.name = mercyblitz }

    public static void main(String[] args) {

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        // 注册 Configuration Class
        context.register(EnvironmentPropertySourceChangeDemo.class);

        // 在 Spring 应用上下文启动前，调整 Environment 中的 PropertySource
        ConfigurableEnvironment environment = context.getEnvironment();
        // 获取 MutablePropertySources 对象
        MutablePropertySources propertySources = environment.getPropertySources();
        // 动态地插入 PropertySource 到 PropertySources 中
        Map<String, Object> source = new HashMap<>();
        source.put("user.name", "小马哥");
        MapPropertySource propertySource = new MapPropertySource("first-property-source", source);
        propertySources.addFirst(propertySource);

        // 启动 Spring 应用上下文
        context.refresh();

      //	启动后操作,但是不具备刷新能力,需要销毁后重建
        source.put("user.name", "007");

        EnvironmentPropertySourceChangeDemo environmentPropertySourceChangeDemo = context.getBean(EnvironmentPropertySourceChangeDemo.class);

        System.out.println(environmentPropertySourceChangeDemo.userName);

        for (PropertySource ps : propertySources) {
            System.out.printf("PropertySource(name=%s) 'user.name' 属性：%s\n", ps.getName(), ps.getProperty("user.name"));
        }

        // 关闭 Spring 应用上下文
        context.close();
    }
}

```

