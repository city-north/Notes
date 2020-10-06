# 拓展点-BeanPostProcessor

- [BeanPostProcessor介绍](#BeanPostProcessor介绍)
- [源码分析-BeanPostProcessor注册过程](#源码分析-BeanPostProcessor注册过程)

## BeanPostProcessor介绍

BeanPostProcessor真正的调用其实是在bean的实例化阶段进行的

这是一个很重要的步骤，也是很多功能BeanFactory不支持的重要原因。Spring中大部分功能都是通过后处理器的方式进行扩展的，这是Spring框架的一个特性，但是在BeanFactory中其实并没有实现后处理器的自动注册，所以在调用的时候如果没有进行手动注册其实是不能使用的。但是在ApplicationContext中却添加了自动注册功能，如自定义这样一个后处理器：

```java
public class MyInstantiationAwareBeanPostProcessor implements InstantiationAwareBeanPostProcessor{
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    System.out.println("====");
    return null;
 	}
 ... ...
}
```

在配置文件中添加配置：

```xml
<bean class="processors.MyInstantiationAwareBeanPostProcessor"/>
```

那么使用BeanFactory方式进行Spring的bean的加载时是不会有任何改变的，但是使用ApplicationContext方式获取bean的时候会在获取每个bean时打印出“====”，而这个特性就是在 registerBeanPostProcessors 方法中完成

## 源码分析-BeanPostProcessor注册过程

```java
protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) {
         String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanPostProcessor.class, true, false);

         /*
          * BeanPostProcessorChecker是一个普通的信息打印，可能会有些情况，
          * 当Spring的配置中的后处理器还没有被注册就已经开始了bean的初始化时
          * 便会打印出BeanPostProcessorChecker中设定的信息
          */
         int beanProcessorTargetCount = beanFactory.getBeanPostProcessorCount() + 1 + postProcessorNames.length;
         beanFactory.addBeanPostProcessor(new BeanPostProcessorChecker(beanFactory,   
beanProcessorTargetCount));

         //使用PriorityOrdered保证顺序
         List<BeanPostProcessor> priorityOrderedPostProcessors = new ArrayList<Bean-   
PostProcessor>();
         //MergedBeanDefinitionPostProcessor
         List<BeanPostProcessor> internalPostProcessors = new ArrayList<BeanPost-   
Processor>();
         //使用Ordered保证顺序
         List<String> orderedPostProcessorNames = new ArrayList<String>();
         //无序BeanPostProcessor
         List<String> nonOrderedPostProcessorNames = new ArrayList<String>();

         for (String ppName : postProcessorNames) {
             if (isTypeMatch(ppName, PriorityOrdered.class)) {
                 BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
                 priorityOrderedPostProcessors.add(pp);
                 if (pp instanceof MergedBeanDefinitionPostProcessor) {
                     internalPostProcessors.add(pp);
                 }
}else if (isTypeMatch(ppName, Ordered.class)) {
                 orderedPostProcessorNames.add(ppName);
             }else {
                 nonOrderedPostProcessorNames.add(ppName);
             }
         }

         //第1步，注册所有实现PriorityOrdered的BeanPostProcessor
         OrderComparator.sort(priorityOrderedPostProcessors);
         registerBeanPostProcessors(beanFactory, priorityOrderedPostProcessors);

         //第2步，注册所有实现Ordered的BeanPostProcessor
         List<BeanPostProcessor> orderedPostProcessors = new ArrayList<BeanPostProcessor>();
         for (String ppName : orderedPostProcessorNames) {
             BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
             orderedPostProcessors.add(pp);
             if (pp instanceof MergedBeanDefinitionPostProcessor) {
                 internalPostProcessors.add(pp);
             }
         }
         OrderComparator.sort(orderedPostProcessors);
         registerBeanPostProcessors(beanFactory, orderedPostProcessors);

         //第3步，注册所有无序的BeanPostProcessor
         List<BeanPostProcessor> nonOrderedPostProcessors = new ArrayList<BeanPost-  
Processor>();
         for (String ppName : nonOrderedPostProcessorNames) {
             BeanPostProcessor pp = beanFactory.getBean(ppName, BeanPostProcessor.class);
             nonOrderedPostProcessors.add(pp);
             if (pp instanceof MergedBeanDefinitionPostProcessor) {
                 internalPostProcessors.add(pp);
             }
         }
  registerBeanPostProcessors(beanFactory, nonOrderedPostProcessors);

         //第4步，注册所有MergedBeanDefinitionPostProcessor类型的BeanPostProcessor，并非
         //重复注册，
         //在beanFactory.addBeanPostProcessor中会先移除已经存在的BeanPostProcessor
         OrderComparator.sort(internalPostProcessors);
         registerBeanPostProcessors(beanFactory, internalPostProcessors);

         //添加ApplicationListener探测器
         beanFactory.addBeanPostProcessor(new ApplicationListenerDetector());
}
```

配合源码以及注释，在registerBeanPostProcessors方法中所做的逻辑相信大家已经很清楚了，我们再做一下总结。

首先我们会发现，对于BeanPostProcessor的处理与BeanFactoryPostProcessor的处理极为相似，但是似乎又有些不一样的地方。经过反复的对比发现，对于BeanFactoryPostProcessor的处理要区分两种情况，一种方式是通过硬编码方式的处理，另一种是通过配置文件方式的处理。那么为什么在BeanPostProcessor的处理中只考虑了配置文件的方式而不考虑硬编码的方式呢？提出这个问题，还是因为读者没有完全理解两者实现的功能。对于BeanFactoryPostProcessor的处理，不但要实现注册功能，而且还要实现对后处理器的激活操作，所以需要载入配置中的定义，并进行激活；而对于BeanPostProcessor并不需要马上调用，再说，硬编码的方式实现的功能是将后处理器提取并调用，这里并不需要调用，当然不需要考虑硬编码的方式了，这里的功能只需要将配置文件的BeanPostProcessor提取出来并注册进入beanFactory就可以了。
对于beanFactory的注册，也不是直接注册就可以的。在Spring中支持对于BeanPost- Processor的排序，比如根据PriorityOrdered进行排序、根据Ordered进行排序或者无序，而Spring在BeanPostProcessor的激活顺序的时候也会考虑对于顺序的问题而先进行排序。
这里可能有个地方读者不是很理解，对于internalPostProcessors中存储的后处理器也就是MergedBeanDefinitionPostProcessor类型的处理器，在代码中似乎是被重复调用了，如：

```java
for (String ppName : postProcessorNames) {
             if (isTypeMatch(ppName, PriorityOrdered.class)) {
                 BeanPostProcessor pp=beanFactory.getBean(ppName, BeanPostProcessor.class);
                 priorityOrderedPostProcessors.add(pp);
                 if (pp instanceof MergedBeanDefinitionPostProcessor) {
                     internalPostProcessors.add(pp);
                 }
             }else if (isTypeMatch(ppName, Ordered.class)) {
                 orderedPostProcessorNames.add(ppName);

);
             }else {
                 nonOrderedPostProcessorNames.add(ppName);
             }
}
```

其实不是，我们可以看看对于registerBeanPostProcessors方法的实现方式。


    private void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory, List<BeanPostProcessor> postProcessors) {  
      for (BeanPostProcessor postProcessor : postProcessors) {
             beanFactory.addBeanPostProcessor(postProcessor);
         }
     }
     public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
         Assert.notNull(beanPostProcessor, "BeanPostProcessor must not be null");
         this.beanPostProcessors.remove(beanPostProcessor);
         this.beanPostProcessors.add(beanPostProcessor);
         if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
             this.hasInstantiationAwareBeanPostProcessors = true;
         }
         if (beanPostProcessor instanceof DestructionAwareBeanPostProcessor) {
             this.hasDestructionAwareBeanPostProcessors = true;
         }
}

可以看到，在registerBeanPostProcessors方法的实现中其实已经确保了beanPostProcessor的唯一性，个人猜想，之所以选择在registerBeanPostProcessors中没有进行重复移除操作或许是为了保持分类的效果，使逻辑更为清晰吧。

