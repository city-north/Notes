# 使用 DefferrdImportSelector

接口`DefferrdImportSelector`继承`ImportSelector`

配置文件类直接注册

与导入的类相比，配置类直接在应用程序上下文中注册。这意味着

- 将使用在主配置中配置的类型为T的bean，而不是导入配置中的类型为T的bean。这也适用于`ImportSelector`。

- 在处理完所有其他配置bean之后应用`DeferredImportSelector`。

## 代码示例

```java
public class ImportSelectorOrderExample {
    public static void main (String[] args) {
        System.setProperty("myProp", "someValue");
        ApplicationContext context = new AnnotationConfigApplicationContext(MainConfig.class);
        ClientBean bean = context.getBean(ClientBean.class);
        bean.doSomething();
    }

    @Configuration
    @Import(MyImportSelector.class)
    public static class MainConfig {

        @Bean
        ClientBean clientBean () {
            return new ClientBean();
        }

        @Bean
        AppBean appBean () {
            return new AppBean("from main config ");
        }

    }

    public static class ClientBean {
        @Autowired
        private AppBean appBean;

        public void doSomething () {
            System.out.println(appBean.getMessage());
        }
    }

    public static class MyImportSelector implements ImportSelector {

        @Override
        public String[] selectImports (AnnotationMetadata importingClassMetadata) {
            String prop = System.getProperty("myProp");
            if ("someValue".equals(prop)) {
                return new String[]{MyConfig1.class.getName()};
            } else {
                return new String[]{MyConfig2.class.getName()};
            }
        }
    }

    public static class AppBean {
        private String message;

        public AppBean (String message) {
            this.message = message;
        }

        public String getMessage () {
            return message;
        }
    }

    @Configuration
    public static class MyConfig1 {
        @Bean
        AppBean appBean () {
            return new AppBean("from config 1");
        }
    }

    @Configuration
    public static class MyConfig2 {
        @Bean
        AppBean appBean () {
            return new AppBean("from config 2");
        }
    }
}
```

```
from main config

```

使用的是主配置类里的声明

## Configuration order with DeferredImportSelector

```java
/**
 * Configuration order with DeferredImportSelector
 *
 * @author EricChen 2019/11/27 22:28
 */
public class DeferredImportSelectorExample {

    public static void main (String[] args) {
        System.setProperty("myProp", "someValue");

        ApplicationContext context = new AnnotationConfigApplicationContext(
                        MainConfig.class);
        ClientBean bean = context.getBean(ClientBean.class);
        bean.doSomething();
    }

    @Configuration
    @Import(MyImportSelector.class)
    public static class MainConfig {

        @Bean
        ClientBean clientBean () {
            return new ClientBean();
        }

        @Bean
        AppBean appBean () {
            return new AppBean("from main config");
        }

    }

    public static class ClientBean {
        @Autowired
        private AppBean appBean;

        public void doSomething () {
            System.out.println(appBean.getMessage());
        }

    }


    public static class MyImportSelector implements DeferredImportSelector {

        @Override
        public String[] selectImports (AnnotationMetadata importingClassMetadata) {
            String prop = System.getProperty("myProp");
            if ("someValue".equals(prop)) {
                return new String[]{MyConfig1.class.getName()};
            } else {
                return new String[]{MyConfig2.class.getName()};
            }
        }
    }

    public static class AppBean {
        private String message;

        public AppBean (String message) {
            this.message = message;
        }

        public String getMessage () {
            return message;
        }
    }

    @Configuration
    public static class MyConfig1 {
        @Bean
        AppBean appBean () {
            return new AppBean("from config 1");
        }
    }

    @Configuration
    public static class MyConfig2 {
        @Bean
        AppBean appBean () {
            return new AppBean("from config 2");
        }
    }
}
```

```java

from config 1

```

从上面的例子可以看出,使用的是DeferredImportSelector实现类选择的配置文件