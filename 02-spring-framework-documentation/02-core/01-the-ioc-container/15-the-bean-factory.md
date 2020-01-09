# BeanFactory 

`BeanFactory`的 API 提供SpringIOC 的最底层的功能,主要是用来集成 Spring 与其他第三方框架时使用, 一个比较重要的实现类是 **DefaultListableBeanFactory**,实际上 是高级别容器`GenericApplicationContext`内的核心代理

`BeanFactory`和其关联的接口,如`BeanFactoryAware`,`InitializingBean`,`DisposableBean`是其他框架组件的非常重要的集成点.但是不需要任何注解和反射,它们允许容器和组件之间非常高效的交互

核心BeanFactory API级别及其`DefaultListableBeanFactory`实现类不会对配置格式或者任何要使用的组件注解做出假设。所有这些风格都是通过扩展(如`XmlBeanDefinitionReader`和`AutowiredAnnotationBeanPostProcessor`)实现的，并作为核心元数据表示对共享的`BeanDefinition`对象进行操作。这就是Spring容器如此灵活和可扩展的本质。



## BeanFactory 的实现类都支持的Bean 生命周期方法

>  Bean factory implementations should support the standard bean lifecycle interfaces as far as possible. The full set of initialization methods and their standard order is:
>
> 1. BeanNameAware's `setBeanName`
> 2. BeanClassLoaderAware's `setBeanClassLoader`
> 3. BeanFactoryAware's `setBeanFactory`
> 4. EnvironmentAware's `setEnvironment`
> 5. EmbeddedValueResolverAware's `setEmbeddedValueResolver`
> 6. ResourceLoaderAware's `setResourceLoader` (only applicable when running in an application context)
> 7. ApplicationEventPublisherAware's `setApplicationEventPublisher` (only applicable when running in an application context)
> 8. MessageSourceAware's `setMessageSource` (only applicable when running in an application context)
> 9. ApplicationContextAware's `setApplicationContext` (only applicable when running in an application context)
> 10. ServletContextAware's `setServletContext` (only applicable when running in a web application context)
> 11. `postProcessBeforeInitialization` methods of BeanPostProcessors
> 12. InitializingBean's `afterPropertiesSet`
> 13. a custom init-method definition
> 14. `postProcessAfterInitialization` methods of BeanPostProcessors
>
> On shutdown of a bean factory, the following lifecycle methods apply:
>
> 1. `postProcessBeforeDestruction` methods of DestructionAwareBeanPostProcessors
> 2. DisposableBean's `destroy`
> 3. a custom destroy-method definition

## 使用`BeanFactory`还是`ApplicationContext`

大多数情况下,你应该使用`ApplicationConetxt`,除非你非常好的理由不使用它,使用`GenericApplicationContext`和他的子类`AnnotationConfigApplicationContext`作为一个自定义引导的标准实现,它实现了主要的 spring 容器的流程如

- 加载配置文件
- 触发 classpath 扫描
- 以编程方式注册

因为`ApplicationContext`提供了所有 `BeanFactory`提供的功能点,所以推荐`AplicationContext`,除了有一些场景可以考虑使用`BeanFactory`:

- 你需要完全控制 bean的流程

下面列表列出了`BeanFactory`和`ApplicationContext`接口实现类的特性

| Feature                                                 | `BeanFactory` | `ApplicationContext` |
| :------------------------------------------------------ | :------------ | :------------------- |
| Bean instantiation/wiring                               | Yes           | Yes                  |
| Integrated lifecycle management                         | No            | Yes                  |
| Automatic `BeanPostProcessor` registration              | No            | Yes                  |
| Automatic `BeanFactoryPostProcessor` registration       | No            | Yes                  |
| Convenient `MessageSource` access (for internalization) | No            | Yes                  |
| Built-in `ApplicationEvent` publication mechanism       | No            | Yes                  |

想使用`DefaultListableBeanFactory`注册一个 bean 的前置处理器(bean post-processor),你需要手动的调用`addBeanPostProcessor`:

```java
DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
// populate the factory with bean definitions

// now register any needed BeanPostProcessor instances
factory.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
factory.addBeanPostProcessor(new MyBeanPostProcessor());

// now start using the factory
```

为了应用一个`BeanFactoryPostProcessor`到一个基础的`DefaultListableBeanFactory`,你需要调用它的`postProcessBeanFactory`方法

```java
DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(factory);
reader.loadBeanDefinitions(new FileSystemResource("beans.xml"));

// bring in some property values from a Properties file
PropertySourcesPlaceholderConfigurer cfg = new PropertySourcesPlaceholderConfigurer();
cfg.setLocation(new FileSystemResource("jdbc.properties"));

// now actually do the replacement
cfg.postProcessBeanFactory(factory);
```

