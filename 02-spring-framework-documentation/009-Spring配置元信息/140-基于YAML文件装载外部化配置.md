# 140-基于YAML文件装载外部化配置

[TOC]

## SpringYAML文件基本API

在Spring中，YAML的配置不能够直接转化为BeanDefinition，或者是增加外部化配置原。但是我们可以通过注册PropertySourceFactory工厂的方式间接使用

- org.springframework.beans.factory.config.YamlProcessor

  - org.springframework.beans.factory.config.YamlMapFactoryBean
  - org.springframework.beans.factory.config.YamlPropertiesFactoryBean

  

## 基于XML资源的YAML外部化配置示例

```java
public class XmlBasedYamlPropertySourceDemo {

    public static void main(String[] args) {
        // 创建 IoC 底层容器
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        // 创建 XML 资源的 BeanDefinitionReader
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        // 记载 XML 资源
        reader.loadBeanDefinitions("classpath:/META-INF/yaml-property-source-context.xml");
        // 获取 Map YAML 对象
        Map<String, Object> yamlMap = beanFactory.getBean("yamlMap", Map.class);
        System.out.println(yamlMap);
    }
}
```

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans
        xmlns="http://www.springframework.org/schema/beans"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
        xsi:schemaLocation="http://www.springframework.org/schema/beans
        https://www.springframework.org/schema/beans/spring-beans.xsd">

   <bean id="yamlMap" class="org.springframework.beans.factory.config.YamlMapFactoryBean" >
       <!-- 关联 user.yaml 配置 -->
       <property name="resources" value="classpath:/META-INF/user.yaml" />
   </bean>

</beans>
```

通过这种方式我们可以获取Yaml中的数据

```
{user={id=2, name=小马哥, city=BEIJING}}
```

## 基于Java注解的YAML外部化配置示例

我们可以通过

```java
/**
 * 基于 Java 注解的 YAML 外部化配置示例
 *
 */
@PropertySource(
        name = "yamlPropertySource",
        value = "classpath:/META-INF/user.yaml",
        factory = YamlPropertySourceFactory.class)
public class AnnotatedYamlPropertySourceDemo {

    /**
     * user.name 是 Java Properties 默认存在，当前用户：mercyblitz，而非配置文件中定义"小马哥"
     *
     * @param id
     * @param name
     * @return
     */
    @Bean
    public User user(@Value("${user.id}") Long id, @Value("${user.name}") String name, @Value("${user.city}") City city) {
        User user = new User();
        user.setId(id);
        user.setName(name);
        user.setCity(city);
        return user;
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        // 注册当前类作为 Configuration Class
        context.register(AnnotatedYamlPropertySourceDemo.class);
        // 启动 Spring 应用上下文
        context.refresh();
        // 获取 Map YAML 对象
        User user = context.getBean("user", User.class);
        System.out.println(user);
        // 关闭 Spring 应用上下文
        context.close();
    }
}

```



```java
/**
 * YAML 格式的 {@link PropertySourceFactory} 实现
 */
public class YamlPropertySourceFactory implements PropertySourceFactory {
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource resource) throws IOException {
        YamlPropertiesFactoryBean yamlPropertiesFactoryBean = new YamlPropertiesFactoryBean();
        yamlPropertiesFactoryBean.setResources(resource.getResource());
        Properties yamlProperties = yamlPropertiesFactoryBean.getObject();
        return new PropertiesPropertySource(name, yamlProperties);
    }
}

```

```xml

```

