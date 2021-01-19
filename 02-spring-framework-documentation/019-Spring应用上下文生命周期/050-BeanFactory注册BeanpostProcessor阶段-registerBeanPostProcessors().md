# 050-BeanFactory注册BeanPostProcessor阶段-registerBeanPostProcessors().md

[TOC]

## 图示

![image-20201007151953236](../../assets/image-20201007151953236.png)

## BeanFactory注册BeanpostProcessor阶段做了什么?

- org.springframework.context.support.AbstractApplicationContext#registerBeanPostProcessors(ConfigurableListableBeanFactory) 方法
  - 注册 PriorityOrdered 类型的BeanPostProcessor Beans
  - 注册 Ordered 类型的BeanPostProcessor Beans
  - 注册普通 BeanPostProcessor Beans
  - 注册 MergedBeanDefinitionPostProcessor Beans
  - 注册 ApplicationListenerDetector对象

## registerBeanPostProcessors

从方法名来说,是在注册BeanPostProcessors

注册 PriorityOrdered 类型的BeanPostProcessorBeans

```java
public static void registerBeanPostProcessors(
  ConfigurableListableBeanFactory beanFactory, AbstractApplicationContext applicationContext) {
	
  //获取所有BeanPostProcess的Bean名称
  String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true, false);

  // Register BeanPostProcessorChecker that logs an info message when
  // a bean is created during BeanPostProcessor instantiation, i.e. when
  // a bean is not eligible for getting processed by all BeanPostProcessors.
  int beanProcessorTargetCount = beanFactory.getBeanPostProcessorCount() + 1 + postProcessorNames.length;
  beanFactory.addBeanPostProcessor(new BeanPostProcessorChecker(beanFactory, beanProcessorTargetCount));

  // Separate between BeanPostProcessors that implement PriorityOrdered,
  // Ordered, and the rest.
  
  //标记过 注册 PriorityOrdered 接口的 BeanPostProcessor
  List<BeanPostProcessor> priorityOrderedPostProcessors = new ArrayList<>();
  List<BeanPostProcessor> internalPostProcessors = new ArrayList<>();
  List<String> orderedPostProcessorNames = new ArrayList<>();
  List<String> nonOrderedPostProcessorNames = new ArrayList<>();
  for (String ppName : postProcessorNames) {
    if (beanFactory.isTypeMatch(ppName, PriorityOrdered.class)) {
      BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
      priorityOrderedPostProcessors.add(pp);
      //注册 MergedBeanDefinitionPostProcessor Beans
      if (pp instanceof MergedBeanDefinitionPostProcessor) {
        internalPostProcessors.add(pp);
      }
    }
    else if (beanFactory.isTypeMatch(ppName, Ordered.class)) {
      orderedPostProcessorNames.add(ppName);
    }
    else {
      nonOrderedPostProcessorNames.add(ppName);
    }
  }

  // First, register the BeanPostProcessors that implement PriorityOrdered.
  //排序 PriorityOrdered
  sortPostProcessors(priorityOrderedPostProcessors, beanFactory);
 //标记过 注册 PriorityOrdered 接口的 BeanPostProcessor
  registerBeanPostProcessors(beanFactory, priorityOrderedPostProcessors);

  // Next, register the BeanPostProcessors that implement Ordered.
  List<BeanPostProcessor> orderedPostProcessors = new ArrayList<>(orderedPostProcessorNames.size());
  for (String ppName : orderedPostProcessorNames) {
    BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
    orderedPostProcessors.add(pp);
    if (pp instanceof MergedBeanDefinitionPostProcessor) {
      internalPostProcessors.add(pp);
    }
  }
  sortPostProcessors(orderedPostProcessors, beanFactory);
  registerBeanPostProcessors(beanFactory, orderedPostProcessors);

  // Now, register all regular BeanPostProcessors.
  List<BeanPostProcessor> nonOrderedPostProcessors = new ArrayList<>(nonOrderedPostProcessorNames.size());
  for (String ppName : nonOrderedPostProcessorNames) {
    BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
    nonOrderedPostProcessors.add(pp);
    if (pp instanceof MergedBeanDefinitionPostProcessor) {
      internalPostProcessors.add(pp);
    }
  }
  registerBeanPostProcessors(beanFactory, nonOrderedPostProcessors);

  // Finally, re-register all internal BeanPostProcessors.
  sortPostProcessors(internalPostProcessors, beanFactory);
  registerBeanPostProcessors(beanFactory, internalPostProcessors);

  // Re-register post-processor for detecting inner beans as ApplicationListeners,
  // moving it to the end of the processor chain (for picking up proxies etc).
  //注册 ApplicationListenerDetector对象
  beanFactory.addBeanPostProcessor(new ApplicationListenerDetector(applicationContext));
}
```



#### 