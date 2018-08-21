[返回目录](/README.md)

# Bean的生命周期

bean从装在的Spring上下文中的一个典型生命周期过程：

![](/assets/import01.png)

1. Spring对bean进行实例化
2. Spring将值和bean的引用注入到bean对应的属性中。
3. 如果bean实现了BeanNameAware接口，Spring将bean的ID传递给setBeanName\(\)方法
4. 如果bean实现了BeanFactoryAware接口，Spring将调用setBeanFactory\(\)方法，将BeanFactory容器实例传入
5. 如果bean实现了ApplicationContextAware接口,Spring将调用setApplicationContext\(\)方法，将bean所在的应用上下文的引用传入进来
6. 如果bean实现了BeanPostProcessor接口，Spring将调用它们的postProcessBeforeInitialization\(\)方法。
7. 如果bean实现了InitializingBean接口，Spring将调用它们的afterPropertiesSet\(\)方法，。类似的没如果bean使用init-method声明了初始化方法，该方法也会被调用
8. 如果bean实现了BeanPostProcessor接口，Spring将调用它们的postProcessAfterInitialization\(\)方法
9. 此时，bean已经准备就绪，可以被应用程序使用了，它们将一直驻留在应用上下文中，直至应用上下文被销毁
10. 如果bean实现了DisposableBean接口，Spring将调用它的destory\(\)接口方法，通用，如果bean使用destory-method声明了销毁方法，也会被调用。

[返回目录](/README.md)

