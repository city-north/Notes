# SpringBean的生命周期

Spring Bean的生命周期分为`四个阶段`和`多个扩展点`。扩展点又可以分为`影响多个Bean`和`影响单个Bean`。

执行流程如下:

实例化 -> 属性赋值 -> 初始化 -> 销毁

## 四个阶段

- [实例化 (Instantiation)](#实例化 (Instantiation))
- [属性赋值(Populate)](#属性赋值(Populate))
- [初始化(Initialization)](#初始化(Initialization))
- [销毁(Destruction)](#销毁(Destruction))

## 多个扩展点

- 影响多个Bean
  -  [BeanPostProcessor(作用于**初始化**阶段的前后)](011-BeanPostProcessor-Bean后置处理器.md) 
  - [InstantiationAwareBeanPostProcessor(作用于**实例化**阶段的前后)](012-InstantiationAwareBeanPostProcessor-实例化感知后置处理器.md) 
  
- 影响单个Bean

  - Aware相关Bean

- 生命周期

  - [InitializingBean](030-InitializingBean.md) 

    > 

  - [DisposableBean](031-DisposableBean.md) 

    >  类似于InitializingBean，对应生命周期的销毁阶段，以ConfigurableApplicationContext#close()方法作为入口，实现是通过循环取所有实现了DisposableBean接口的Bean然后调用其destroy()方法 。感兴趣的可以自行跟一下源码

## Aware相关Bean

| 序号 | Aware Group1         | 调用的方法         |      |
| ---- | -------------------- | ------------------ | ---- |
| 1    | BeanNameAware        | setBeanName        | ALL  |
| 2    | BeanClassLoaderAware | setBeanClassLoader | ALL  |
| 3    | BeanFactoryAware     | setBeanFactory     | ALL  |

| 序号 | Aware Group2                                       | 调用的方法                      |         |                                                              |
| ---- | -------------------------------------------------- | ------------------------------- | ------- | ------------------------------------------------------------ |
| 4    | EnvironmentAware                                   | setEnvironment                  | ALL     |                                                              |
| 5    | EmbeddedValueResolverAware                         | setEmbeddedValueResolver        | ALL     | 实现该接口能够获取Spring EL解析器，用户的自定义注解需要支持spel表达式的时候可以使用，非常方便。 |
| 6    | ResourceLoaderAware                                | setResourceLoader               | ALL     |                                                              |
| 7    | ApplicationEventPublisherAware                     | setApplicationEventPublisher    | ALL     |                                                              |
| 8    | MessageSourceAware                                 | setMessageSource                | ALL     |                                                              |
| 9    | ApplicationContextAware                            | setApplicationContext           | ALL     |                                                              |
| 10   | ServletContextAware                                | setServletContext               | web应用 |                                                              |
| 11   | BeanPostProcessors#postProcessBeforeInitialization | postProcessBeforeInitialization | ALL     |                                                              |
| 12   | InitializingBean                                   | afterPropertiesSet              | ALL     |                                                              |
| 13   | 自定义 init-method 方法                            | init-method                     | ALL     |                                                              |
| 14   | BeanPostProcessors#postProcessAfterInitialization  | postProcessAfterInitialization  | ALL     |                                                              |

#### Aware调用时机源码分析

详情如下，忽略了部分无关代码。代码位置就是我们上文提到的initializeBean方法详情，这也说明了Aware都是在初始化阶段之前调用的！

```dart
    // 见名知意，初始化阶段调用的方法
    protected Object initializeBean(final String beanName, final Object bean, @Nullable RootBeanDefinition mbd) {

        // 这里调用的是Group1中的三个Bean开头的Aware
        invokeAwareMethods(beanName, bean);

        Object wrappedBean = bean;
        
        // 这里调用的是Group2中的几个Aware，
        // 而实质上这里就是前面所说的BeanPostProcessor的调用点！
        // 也就是说与Group1中的Aware不同，这里是通过BeanPostProcessor（ApplicationContextAwareProcessor）实现的。
        wrappedBean = applyBeanPostProcessorsBeforeInitialization(wrappedBean, beanName);
        // 下文即将介绍的InitializingBean调用点
        invokeInitMethods(beanName, wrappedBean, mbd);
        // BeanPostProcessor的另一个调用点
        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);

        return wrappedBean;
    }
```

可以看到并不是所有的Aware接口都使用同样的方式调用。

- Bean××Aware都是在代码中直接调用的，
- ApplicationContext相关的Aware都是通过BeanPostProcessor#postProcessBeforeInitialization()实现的。

ApplicationContextAwareProcessor这个类的源码，就是判断当前创建的Bean是否实现了相关的Aware方法，如果实现了会调用回调方法将资源传递给Bean。
至于Spring为什么这么实现，应该没什么特殊的考量。也许和Spring的版本升级有关。基于对修改关闭，对扩展开放的原则，Spring对一些新的Aware采用了扩展的方式添加。

BeanPostProcessor的调用时机也能在这里体现，包围住invokeInitMethods方法，也就说明了在初始化阶段的前后执行。

关于Aware接口的执行顺序，其实只需要记住第一组在第二组执行之前就行了。每组中各个Aware方法的调用顺序其实没有必要记，有需要的时候点进源码一看便知。

因为Aware方法都是执行在初始化方法之前，所以可以在初始化方法中放心大胆的使用Aware接口获取的资源，这也是我们自定义扩展Spring的常用方式。




## 实例化 (Instantiation)

## 属性赋值(Populate)

## 初始化(Initialization)

## 销毁(Destruction)

| 销毁顺序 | 执行类                             | 方法                         |
| -------- | ---------------------------------- | ---------------------------- |
| 1        | DestructionAwareBeanPostProcessors | postProcessBeforeDestruction |
| 2        | DisposableBean                     | destroy                      |
| 3        | 自定义的destroy-method             | destroy-method               |




