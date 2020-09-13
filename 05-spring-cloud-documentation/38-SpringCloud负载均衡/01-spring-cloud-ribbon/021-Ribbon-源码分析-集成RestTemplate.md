## Ribbon-源码分析-集成RestTemplate

@RibbonClient注解可以声明Ribbon客户端，设置Ribbon客户端的名称和配置类，configuration属性可以指定@Configuration的配置类，进行Ribbon相关的配置。@RibbonClient还会导入(import)RibbonClientConfigurationRegistrar类来动态注册Ribbon相关的BeanDefinition。@RibbonClient注解的具体实现如下所示：
@Import(RibbonClientConfigurationRegistrar.class)
public @interface RibbonClient {
    String value() default "";
    /**
     * 配置Ribbon客户端名称
     */
    String name() default "";
    /**
     * Ribbon客户端的自定义配置，可以配置生成客户端的各个组件，比如说ILoadBalancer、ServerListFilter和IRule。默认的配置为RibbonClientConfiguration.java
     */
    Class〈?〉[] configuration() default {};
}

RibbonClientConfigurationRegistrar是ImportBeanDefinitionRegistrar的实现类，ImportBeanDefinitionRegistrar是Spring动态注册BeanDefinition的接口，可以用来注册Ribbon所需的BeanDefinition，比如说Ribbon客户端实例(Ribbon Client)。ImportBeanDefinitionRegistrar的registerBeanDefinitions方法可以注册Ribbon客户端的配置类，也就是@RibbonClient的configuration属性值。registerBeanDefinitions方法的具体实现如下所示：
//RibbonClientConfigurationRegistrar.java
public void registerBeanDefinitions(AnnotationMetadata metadata,
        BeanDefinitionRegistry registry) {
    ...
    //获取@RibbonClient的参数数值，获取clientName后进行configuration的注册
    Map〈String, Object〉 client = metadata.getAnnotationAttributes(
            RibbonClient.class.getName(), true);
    String name = getClientName(client);//获取RibbonClient的value或者name数值。
    if (name != null) {
        registerClientConfiguration(registry, name, client.get("configuration"));
    }
}

Ribbon对于组件实例的管理配置机制和OpenFeign相同，都是通过NamedContextFactory创建带名称的AnnotationConfigApplicationContext子上下文来存