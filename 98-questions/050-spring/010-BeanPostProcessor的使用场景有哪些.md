# BeanPostProcessor的使用场景有哪些

BeanPostProcessor 提供SpringBean的初始化前和初始化后的生命回调

分别对应

- postProcessBeforeInitialization
- postProcessAfterInitialization

允许我们对我们关心的bean进行拓展甚至替换

其中, ApplicationContext相关的Aware回调也是基于BeanPostProcessor

ApplicationContextAwareProcessor

生命周期注解支持

- @PostConstroctor
- @PreDestory

是由 CommonAnnotationBeanPostProcessor  去实现的