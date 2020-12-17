# 使用 Log4j

## 依赖

```xml
 <dependency>
   <groupId>log4j</groupId>
   <artifactId>log4j</artifactId>
   <version>1.2.17</version>
  </dependency>
```

## 代码实例

```java
/**
 * 代码实例 log4j 使用
 *
 * @author EricChen 2019/11/29 13:32
 */
@Configuration
public class Log4jExample {
    @Bean
    public MyBean myBean() {
        return new MyBean();
    }

    public static void main(String[] args) {
        ConfigurableApplicationContext context =
                new AnnotationConfigApplicationContext(Log4jExample.class);

        MyBean bean = context.getBean(MyBean.class);
        bean.doSomething();
    }

    public static class MyBean {
        private static Log log = LogFactory.getLog(MyBean.class);

        public void doSomething() {
            log.info("doing something");
        }
    }
}

```

