# SpringBoot上下文

 [001-SpringCloud中的父子容器抽象.md](../../05-spring-cloud-documentation/25-SpringCloud与Commom抽象/001-SpringCloud中的父子容器抽象/001-SpringCloud中的父子容器抽象.md) 

SpringBoot创建的 Spring容器是最核心的容器，也是使用最多的Spring容器。
创建的对象会有3种类型，Servlet，Reactive，和默认。
在SpringBoot2.x版本中的判断如下：

```java
public class SpringApplication {
    //......
    protected ConfigurableApplicationContext createApplicationContext() {
        Class<?> contextClass = this.applicationContextClass;
        if (contextClass == null) {
            try {
                switch (this.webApplicationType) {
                case SERVLET:
                    contextClass = Class.forName(DEFAULT_WEB_CONTEXT_CLASS);
                    break;
                case REACTIVE:
                    contextClass = Class.forName(DEFAULT_REACTIVE_WEB_CONTEXT_CLASS);
                    break;
                default:
                    contextClass = Class.forName(DEFAULT_CONTEXT_CLASS);
                }
            }
            catch (ClassNotFoundException ex) {
                throw new IllegalStateException(
                        "Unable create a default ApplicationContext, "
                                + "please specify an ApplicationContextClass",
                        ex);
            }
        }
        return (ConfigurableApplicationContext) BeanUtils.instantiateClass(contextClass);
    }
    //......
}
```

