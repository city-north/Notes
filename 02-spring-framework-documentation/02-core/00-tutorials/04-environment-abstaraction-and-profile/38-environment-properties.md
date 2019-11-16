# Environment properties 

> 版权声明
>
> 此篇文章参考于[logicbig](https://www.logicbig.com/),引用其文字/代码/图片仅用于学习笔记,不用于商业用途

Spring试图将所有key/value 属性对的访问统一到`org.springframework.core.env.Environment`中。

- 这个属性可以是从 java系统/环境属性中或者是文件中加载`java.util.Properties`

- 如果是 Servlet container 环境,这个源可能是`javax.servlet.servletConfig`或者是`java.servlet.ServletContest`

## 获取Environment中的 properties

```java
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Map;

public class DefaultSystemSourcesExample {
    public static void main (String[] args) {
        AnnotationConfigApplicationContext context =
                            new AnnotationConfigApplicationContext();
        ConfigurableEnvironment env = context.getEnvironment();
        printSources(env);
        System.out.println("---- System properties -----");
        printMap(env.getSystemProperties());
        System.out.println("---- System Env properties -----");
        printMap(env.getSystemEnvironment());
    }

    private static void printSources (ConfigurableEnvironment env) {
        System.out.println("---- property sources ----");
        for (PropertySource<?> propertySource : env.getPropertySources()) {
            System.out.println("name =  " + propertySource.getName() + "\nsource = " + propertySource
                                .getSource().getClass()+"\n");
        }
    }

    private static void printMap (Map<?, ?> map) {
        map.entrySet()
           .stream()
           .forEach(e -> System.out.println(e.getKey() + " = " + e.getValue()));

    }
}
```

#### 值得注意的是

- 默认情况下，系统属性优先于环境变量当调用`env.getProperty(“foo”)`,返回系统属性(System properties)属性值不会被合并,而是被前面的条目覆盖

## 使用注解`@PropertySource`

`org.springframework.context.annotation.PropertySource`注解提供了一个便利的,声明式的机制,让我们能够添加配置源到环境(Environment)

#### src/main/resources/tutorials/properties/app.properties

```properties
some-strProp=helloworld
```

```java
/**
 * {@link @PropertySource} 注解提供了便利的机制让我们能添加属性源(properties source)到我们环境中
 * (Environment)中
 *
 * @author EricChen 2019/11/16 21:20
 */
@Configuration
@PropertySource("classpath:tutorials/properties/app.properties")
public class PropertySourceExample {

    public static void main (String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(PropertySourceExample.class);
        ConfigurableEnvironment env = context.getEnvironment();
        String property = env.getProperty("some-strProp");
        System.out.println("some-strProp value is " + property);

        //printing all sources
        System.out.println(env.getPropertySources());
    }
}
```

#### 输出

```
some-strProp value is helloworld
[MapPropertySource {name='systemProperties'}, SystemEnvironmentPropertySource {name='systemEnvironment'}, ResourcePropertySource {name='class path resource [tutorials/properties/app.properties]'}]
```



## 使用`@Value`注解注入属性

For injecting properties by their names, we can use the placeholder ${....} as value element of @Value annotation. We also have to register `PropertySourcesPlaceholderConfigurer` as bean:

根据属性的名称注入属性,我们可以使用占位符`${...}`的格式,但是首先我们要注册一个 bean``PropertySourcesPlaceholderConfigurer``

```java
@Configuration
@PropertySource("classpath:tutorials/properties/app.properties")
public class BeanValueExample {
    @Bean
    public MyBean myBean () {
        return new MyBean();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    public static void main (String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(BeanValueExample.class);
        context.getBean(MyBean.class).showProp();
    }

    public static class MyBean {
        @Value("${some-strProp:defaultStr}")
        private String str;


        public void showProp () {
            System.out.println(str);
        }
    }
}
```

## 使用 Spring 表达式语言 SpEL

We can use spring expression as value element of @Value. In that case we don't have to set up PropertySource or `PropertySourcesPlaceholderConfigurer`:

```java
@Configuration
public class BeanExprExample {
    @Bean
    public MyBean myBean () {
        return new MyBean();
    }

    public static void main (String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(BeanExprExample.class);
        context.getBean(MyBean.class)
                .showProp();
    }

    public static class MyBean {
        @Value("#{systemProperties['user.home']}")
        private String userHome;

        public void showProp () {
            System.out.println(userHome);
        }
    }
}
```

