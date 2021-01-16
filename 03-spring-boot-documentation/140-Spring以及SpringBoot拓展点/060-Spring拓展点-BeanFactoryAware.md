# 060-Spring拓展点-BeanFactoryAware

[TOC]

## 一言蔽之

> org.springframework.beans.factory.BeanFactoryAware

这个类只有一个触发点，发生在bean的实例化之后，注入属性之前，也就是Setter之前。这个类的扩展点方法为setBeanFactory，可以拿到BeanFactory这个属性。

使用场景为，你可以在bean实例化之后，但还未初始化之前，拿到 BeanFactory，在这个时候，可以对每个bean作特殊化的定制。也或者可以把BeanFactory拿到进行缓存，日后使用。

```java
public class TestBeanFactoryAware implements BeanFactoryAware {

  @Override
  publicvoid setBeanFactory(BeanFactory beanFactory)throwsBeansException {

    System.out.println("[TestBeanFactoryAware] " + beanFactory.getBean(TestBeanFactoryAware.class).getClass().getSimpleName());
  }

}
```

