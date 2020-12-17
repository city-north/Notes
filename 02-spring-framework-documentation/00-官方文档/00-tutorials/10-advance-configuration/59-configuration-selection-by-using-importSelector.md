# 使用 Import选择进行@Configuration 类的选择

@Import annotation  可以用来导入其他@COnfiguration 类的同时,也可以配置一个 `ImportSelector`来用编程的方式选择配置文件,基于选择条件



## 实例

自定义`ImportSelector`实现类完成对配置文件的选择

```java
/**
 * 示例: 使用 @Import 注解导入 Selector 选择器
 *
 * @author EricChen 2019/11/27 22:10
 */
public class ImportSelectorExample {
    public static void main (String[] args) {
        System.setProperty("myProp", "1");
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
            if ("1".equals(prop)) {
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

自定义注解完成对配置文件的选择

```java
/**
 * 示例: 使用自定义注解的方式,导入选择器
 * 自定义注解上要表名@Import 注解
 *
 * @author EricChen 2019/11/27 22:17
 */
public class ImportSelectorWithAnnotationExample {

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(MainConfig.class);
        ClientBean bean = context.getBean(ClientBean.class);
        bean.doSomething();
    }

    @Configuration
    @EnableSomeModule("someValue")
    public static class MainConfig {
        @Bean
        ClientBean clientBean() {
            return new ClientBean();
        }
    }

    public static class ClientBean {
        @Autowired
        private AppBean appBean;

        public void doSomething() {
            System.out.println(appBean.getMessage());
        }
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @Import(MyImportSelector.class)
    public @interface EnableSomeModule {
        String value() default "";
    }


    public static class MyImportSelector implements ImportSelector {

        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            AnnotationAttributes attributes = AnnotationAttributes.fromMap(importingClassMetadata.getAnnotationAttributes(EnableSomeModule.class.getName(), false));
            String value = attributes.getString("value");
            if ("someValue".equals(value)) {
                return new String[]{MyConfig1.class.getName()};
            } else {
                return new String[]{MyConfig2.class.getName()};
            }
        }
    }

    public static class AppBean {
        private String message;

        public AppBean(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    @Configuration
    public static class MyConfig1 {
        @Bean
        AppBean appBean() {
            return new AppBean("from config 1");
        }
    }

    @Configuration
    public static class MyConfig2 {
        @Bean
        AppBean appBean() {
            return new AppBean("from config 2");
        }
    }
}

```

