# 040-BeanFactory后置处理阶段

[TOC]

## 一言蔽之

BeanFactory后置处理阶段主要是在BeanFactory初始化好以后执行的后置通知 执行顺序是

- BeanDefinitionRegistryPostProcessor的postProcessBeanDefinitionRegistry

  > 行为的含义是Bean的注册中心初始化完成,允许进行定制

- BeanFactoryPostProcessor的postProcessBeanFactory

  > 行为的含义是BeanFactory已经初始化完毕了,可以进行定制

## BeanFactory后置处理阶段做了什么?

有两种方式可以对BeanFactory后进行处理

- AbstractApplicationContext#postProcessBeanFactory(ConfigurableListableBeanFactory)方法
  - 由子类覆盖该方法
- AbstractApplicationContext#invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory)方法
  - 调用BeanFactoryPostProcessor或者 BeanDefinitionRegistry 后置处理方法
  - 注册LoadTimeWeaverAwareProcessor对象

## 图示

![image-20201007151953236](../../assets/image-20201007151953236.png)



后置阶段主要是第3 和第4步

- [postProcessBeanFactory拓展方式](#postProcessBeanFactory拓展方式)
  - postProcessBeanFactory 实际上采用的继承的方式进行拓展

- [invokeBeanFactoryPostProcessors拓展方式](#invokeBeanFactoryPostProcessors拓展方式)
  - 实际上是组合方法来拓展(调用当前上下文所组合的BeanFactoryPostProcessor来进行操作)

## 代码实例

```java
public class PostFactoryBeanFactoryDemo {

    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.addBeanFactoryPostProcessor(new MyBeanFactoryPostProcessor());
        context.addBeanFactoryPostProcessor(new MyBeanDefinitionRegistryPostProcessor());
        context.refresh();
        context.close();
    }
    static class MyBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            System.out.println("----- MyBeanFactoryPostProcessor- postProcessBeanFactory()------");
        }
    }
    static class MyBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

        @Override
        public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
            System.out.println("----- MyBeanDefinitionRegistryPostProcessor- postProcessBeanDefinitionRegistry()------");

        }

        @Override
        public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
            System.out.println("----- MyBeanDefinitionRegistryPostProcessor- postProcessBeanFactory()------");

        }
    }
}
```

## 源码分析

### postProcessBeanFactory拓展方式

org.springframework.web.context.support.GenericWebApplicationContext#postProcessBeanFactory 中的postProcessBeanFactory主要是添加了一种ServletContextAwareProcessor, 添加 ServletContextAware的后置处理

### 添加自定义的ServletContextAware接口

```java
@Override
protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
  if (this.servletContext != null) {
    //添加后置处理器
    beanFactory.addBeanPostProcessor(new ServletContextAwareProcessor(this.servletContext));
    //忽略依赖注入接口
    beanFactory.ignoreDependencyInterface(ServletContextAware.class);
  }
  //注册Bean的Scope作用域
  WebApplicationContextUtils.registerWebApplicationScopes(beanFactory, this.servletContext);
  //注册环境Bean
  WebApplicationContextUtils.registerEnvironmentBeans(beanFactory, this.servletContext);
}
```

### invokeBeanFactoryPostProcessors拓展方式

```java
/**
* Instantiate and invoke all registered BeanFactoryPostProcessor beans,
* respecting explicit order if given.
* <p>Must be called before singleton instantiation.
*/
protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
  //执行 BeanFactory的后置处理器
  PostProcessorRegistrationDelegate.invokeBeanFactoryPostProcessors(beanFactory, getBeanFactoryPostProcessors());

  // Detect a LoadTimeWeaver and prepare for weaving, if found in the meantime
  // (e.g. through an @Bean method registered by ConfigurationClassPostProcessor)
  if (beanFactory.getTempClassLoader() == null && beanFactory.containsBean(LOAD_TIME_WEAVER_BEAN_NAME)) {
    beanFactory.addBeanPostProcessor(new LoadTimeWeaverAwareProcessor(beanFactory));
    beanFactory.setTempClassLoader(new ContextTypeMatchClassLoader(beanFactory.getBeanClassLoader()));
  }
}
```

`invokeBeanFactoryPostProcessors`方法触发了

- `BeanDefinitionRegistryPostProcessor#postProcessBeanDefinitionRegistry`先执行
- `BeanFactoryPostProcessor#postProcessBeanFactory`



