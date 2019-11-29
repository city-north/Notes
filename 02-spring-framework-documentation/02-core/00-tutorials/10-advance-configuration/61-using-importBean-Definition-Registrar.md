# 使用 ImportBeanDefinitionRegistrar



## `ImportBeanDefinitionRegistrar`接口

```java
public interface ImportBeanDefinitionRegistrar {

	public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata,BeanDefinitionRegistry registry);

}

```

这个接口的`registerBeanDefinitions`方法可以完成注册一个 Bean

- AnnotationMetadata 注解元信息
- BeanDefinitionRegistry Bean定义注册器

## 代码实例



```java
/**
 * 示例: 使用 {@link ImportBeanDefinitionRegistrar} 的实现类注册 bean
 * 使用 @Import 注解注入注册类
 *
 * @author EricChen 2019/11/28 09:41
 */
public class ImportBeanDefinitionRegistrarExample {
    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(MyConfig.class);
        ClientBean bean = context.getBean(ClientBean.class);
        bean.doSomething();
    }

    @Configuration
    @Import(MyBeanRegistrar.class)
    public static class MyConfig {

        @Bean
        ClientBean clientBean() {
            return new ClientBean();
        }
    }


    private static class MyBeanRegistrar implements ImportBeanDefinitionRegistrar {

        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            GenericBeanDefinition gbd = new GenericBeanDefinition();
            gbd.setBeanClass(AppBean.class);
            gbd.getPropertyValues().addPropertyValue("str", "value set from registrar");
            registry.registerBeanDefinition("appBean", gbd);
        }
    }

    private static class ClientBean {
        @Autowired
        private AppBean appBean;

        public void doSomething() {
            appBean.process();
        }
    }

    private static class AppBean {
        private String str;

        public void setStr(String str) {
            this.str = str;
        }

        public void process() {
            System.out.println(str);
        }
    }
}

```

