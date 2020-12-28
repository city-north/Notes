# SpringIoc依赖注入

[TOC]

## Spring IoC的依赖来源

- 自定义的Bean
- 容器内建Bean对象
- 容器内建依赖

## 内建依赖

类似于 BeanFactory

## 什么是内建的Bean对象

```java
/**
*  内部内建Bean
*  @param beanFactory
*/
private static void injectInsideBean(BeanFactory beanFactory) {
  final Environment bean = beanFactory.getBean(Environment.class);
  System.out.println(bean);
}
```

#### abstractApplicationContext内建可查找的依赖

| Bean 名称                   | Bean 实例                         | 使用场景                |
| --------------------------- | --------------------------------- | ----------------------- |
| environment                 | Environment 对象                  | 外部化配置以及 Profiles |
| systemProperties            | java.util.Properties 对象         | Java 系统属性           |
| systemEnvironment           | java.util.Map 对象                | 操作系统环境变量        |
| messageSource               | MessageSource 对象                | 国际化文案              |
| lifecycleProcessor          | LifecycleProcessor 对象           | Lifecycle Bean 处理器   |
| applicationEventMulticaster | ApplicationEventMulticaster 对 象 | Spring 事件广播器       |

#### 注解驱动 Spring 应用上下文内建可查找的依赖（部分）

| Bean 实例   | ConfigurationClassPostProcessor 对象                         |
| ----------- | ------------------------------------------------------------ |
| Bean 全路径 | org.springframework.context.annotation.internalConfigurationAnnotationProcessor |
| 使用场景    | 处理 Spring 配置类                                           |
| 基类        | BeanFactoryPostProcessor Spring容器的生命周期处理,BeanFactory后置处理器 |

| Bean 实例   | AutowiredAnnotationBeanPostProcessor 对象                    |
| ----------- | ------------------------------------------------------------ |
| Bean 全路径 | org.springframework.context.annotation.internalAutowiredAnnotationProcessor |
| 使用场景    | 处理 @Autowired 以及 @Value 注解                             |
| 基类        | BeanPostProcessor Bean的生命周期处理,Bean的后置处理器        |

| Bean 实例   | CommonAnnotationBeanPostProcessor 对象                       |
| ----------- | ------------------------------------------------------------ |
| Bean 全路径 | org.springframework.context.annotation.internalCommonAnnotationProcessor |
| 使用场景    | （条件激活）处理 JSR-250 注解，如 @PostConstruct 等          |
| 基类        | BeanPostProcessor Bean的生命周期处理,Bean的后置处理器        |

| Bean 实例   | EventListenerMethodProcessor 对象                            |
| ----------- | ------------------------------------------------------------ |
| Bean 全路径 | org.springframework.context.event.internalEventListenerProcessor |
| 使用场景    | 处理标注 @EventListener 的Spring 事件监听方法                |
| 基类        | BeanFactoryPostProcessor pring容器的生命周期处理,BeanFactory后置处理器 |

| Bean 实例   | DefaultEventListenerFactory 对 象                            |
| ----------- | ------------------------------------------------------------ |
| Bean 全路径 | org.springframework.context.event.internalEventListenerFactory |
| 使用场景    | @EventListener 事件监听方法适配为 ApplicationListener        |
| 基类        | EventListenerFactory                                         |





