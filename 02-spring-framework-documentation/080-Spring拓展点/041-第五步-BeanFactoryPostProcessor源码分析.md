# BeanFactoryPostProcessor源码分析

![image-20201007151953236](../../assets/image-20201007151953236.png)

## 目录

- [源码分析-BeanFactoryPostProcessor注册过程](#源码分析-BeanFactoryPostProcessor注册过程)

- [源码分析-BeanFactoryPostProcessor调用过程](#源码分析-BeanFactoryPostProcessor调用过程)

## 源码分析-BeanFactoryPostProcessor注册过程

ApplicationContext 会自动检测实现了BeanFactoryPostProcessor的bean,并且会在bean创建之前执行他们

ConfigurableApplicationContext 也会自动注册

注册和执行都是在 invokeBeanFactoryPostProcessors 方法里,看下面

## 源码分析-BeanFactoryPostProcessor调用过程

refresh第五步

```java
//5、调用所有注册的BeanFactoryPostProcessor的Bean
invokeBeanFactoryPostProcessors(beanFactory);
```

```java
protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) {
         // Invoke BeanDefinitionRegistryPostProcessors first, if any.
         Set<String> processedBeans = new HashSet<String>();
         //对BeanDefinitionRegistry类型的处理
         if (beanFactory instanceof BeanDefinitionRegistry) {
             BeanDefinitionRegistry registry = (BeanDefinitionRegistry) beanFactory;

             List<BeanFactoryPostProcessor> regularPostProcessors = new LinkedList   
<BeanFactoryPostProcessor>();
             /**
              * BeanDefinitionRegistryPostProcessor
              */
             List<BeanDefinitionRegistryPostProcessor> registryPostProcessors = new   
LinkedList<BeanDefinitionRegistryPostProcessor>();
             /*>();
             /*
              * 硬编码注册的后处理器
              */
             for (BeanFactoryPostProcessor postProcessor : getBeanFactoryPostProcessors()) {
                 if (postProcessor instanceof BeanDefinitionRegistryPostProcessor) {
                     BeanDefinitionRegistryPostProcessor registryPostProcessor =(Bean   
DefinitionRegistryPostProcessor) postProcessor;
                     //对于BeanDefinitionRegistryPostProcessor类型，在BeanFactoryPostProcessor的基础上还有自己定义的方法，需要先调用
                     registryPostProcessor.postProcessBeanDefinitionRegistry(registry);
                     registryPostProcessors.add(registryPostProcessor);
                 }else {
                     //记录常规BeanFactoryPostProcessor
                     regularPostProcessors.add(postProcessor);
                 }
             }
             /*
              * 配置注册的后处理器
              */
             Map<String, BeanDefinitionRegistryPostProcessor> beanMap = beanFactory.   
getBeansOfType(BeanDefinitionRegistryPostProcessor.class, true, false);
             List<BeanDefinitionRegistryPostProcessor> registryPostProcessorBeans =     new ArrayList<BeanDefinitionRegistryPostProcessor>(beanMap.values());
             OrderComparator.sort(registryPostProcessorBeans);
             for (BeanDefinitionRegistryPostProcessor postProcessor : registryPostProcessorBeans) {
                 //BeanDefinitionRegistryPostProcessor的特殊处理
                 postProcessor.postProcessBeanDefinitionRegistry(registry);
             }

             //激活postProcessBeanFactory方法，之前激活的是postProcessBeanDefinitionRegistry
             //硬编码设置的BeanDefinitionRegistryPostProcessor
             invokeBeanFactoryPostProcessors(registryPostProcessors, beanFactory);
             //配置的BeanDefinitionRegistryPostProcessor
             invokeBeanFactoryPostProcessors(registryPostProcessorBeans, beanFactory);
           );
             //常规BeanFactoryPostProcessor
             invokeBeanFactoryPostProcessors(regularPostProcessors, beanFactory);
             processedBeans.addAll(beanMap.keySet());
         }
         else {
             // Invoke factory processors registered with the context instance.
             invokeBeanFactoryPostProcessors(getBeanFactoryPostProcessors(), beanFactory);
         }

         //对于配置中读取的BeanFactoryPostProcessor的处理
         String[] postProcessorNames = beanFactory.getBeanNamesForType(BeanFactoryPost Processor.class, true, false);

         List<BeanFactoryPostProcessor> priorityOrderedPostProcessors = new ArrayList <BeanFactoryPostProcessor>();
         List<String> orderedPostProcessorNames = new ArrayList<String>();
         List<String> nonOrderedPostProcessorNames = new ArrayList<String>();
         //对后处理器进行分类
         for (String ppName : postProcessorNames) {
             if (processedBeans.contains(ppName)) {
                 //已经处理过
             }else if (isTypeMatch(ppName, PriorityOrdered.class)) {
                 priorityOrderedPostProcessors.add(beanFactory.getBean(ppName, BeanFactoryPostProcessor.class));
             }else if (isTypeMatch(ppName, Ordered.class)) {
                 orderedPostProcessorNames.add(ppName);
             }else {
                 nonOrderedPostProcessorNames.add(ppName);
             }
         }

         //按照优先级进行排序
         OrderComparator.sort(priorityOrderedPostProcessors);
         invokeBeanFactoryPostProcessors(priorityOrderedPostProcessors, beanFactory);

         // Next, invoke the BeanFactoryPostProcessors that implement Ordered.
  				List<BeanFactoryPostProcessor> orderedPostProcessors = new ArrayList<BeanFactory PostProcessor>();
         for (String postProcessorName : orderedPostProcessorNames) {
             orderedPostProcessors.add(getBean(postProcessorName, BeanFactoryPostProcessor. class));
         }
         //按照order排序
         OrderComparator.sort(orderedPostProcessors);
         invokeBeanFactoryPostProcessors(orderedPostProcessors, beanFactory);


         //无序，直接调用
         List<BeanFactoryPostProcessor> nonOrderedPostProcessors = new ArrayList<BeanFactoryPostProcessor>();
         for (String postProcessorName : nonOrderedPostProcessorNames) {
             nonOrderedPostProcessors.add(getBean(postProcessorName,BeanFactoryPostProcessor.class));
         }
         invokeBeanFactoryPostProcessors(nonOrderedPostProcessors, beanFactory);
}
```

从上面的方法中我们看到，对于BeanFactoryPostProcessor的处理主要分两种情况进行，

- 一个是对于BeanDefinitionRegistry类的特殊处理，
- 另一种是对普通的BeanFactoryPostProcessor进行处理

而对于每种情况都需要考虑硬编码注入注册的后处理器以及通过配置注入的后处理器。

对于BeanDefinitionRegistry类型的处理类的处理主要包括以下内容。

#### 硬编码注册的后处理器的处理

对于硬编码注册的后处理器的处理，主要是通过AbstractApplicationContext中的添加处理器方法addBeanFactoryPostProcessor进行添加。

```java
public void addBeanFactoryPostProcessor(BeanFactoryPostProcessor beanFactoryPostProcessor) {
         this.beanFactoryPostProcessors.add(beanFactoryPostProcessor);
}
```

添加后的后处理器会存放在beanFactoryPostProcessors中，而在处理BeanFactoryPostProcessor时候会首先检测beanFactoryPostProcessors是否有数据。当然，BeanDefinitionRegistryPostProcessor继承自BeanFactoryPostProcessor，不但有BeanFactoryPostProcessor的特性，同时还有自己定义的个性化方法，也需要在此调用。所以，这里需要从beanFactoryPostProcessors中挑出BeanDefinitionRegistryPostProcessor的后处理器，并进行其postProcessBeanDefinitionRegistry方法的激活。

#### 记录后处理器主要使用了3个List完成

- registryPostProcessors：记录通过硬编码方式注册的BeanDefinitionRegistryPostProcessor类型的处理器。
- regularPostProcessors：记录通过硬编码方式注册的BeanFactoryPostProcessor类型的处理器。
- registryPostProcessorBeans：记录通过配置方式注册的BeanDefinitionRegistryPostProcessor类型的处理器。

#### 其他注意点

- 对以上所记录的List中的后处理器进行统一调用BeanFactoryPostProcessor的postProcessBeanFactory方法。

- 对beanFactoryPostProcessors中非BeanDefinitionRegistryPostProcessor类型的后处理器进行统一的BeanFactoryPostProcessor的postProcessBeanFactory方法调用。

- 普通beanFactory处理。

BeanDefinitionRegistryPostProcessor只对BeanDefinitionRegistry类型的ConfigurableListableBeanFactory有效，所以如果判断所示的beanFactory并不是BeanDefinitionRegistry，那么便可以忽略BeanDefinitionRegistryPostProcessor，而直接处理BeanFactoryPostProcessor，当然获取的方式与上面的获取类似。

这里需要提到的是，对于硬编码方式手动添加的后处理器是不需要做任何排序的，但是在配置文件中读取的处理器，Sping并不保证读取的顺序。所以，为了保证用户的调用顺序的要求，Spring对于后处理器的调用支持按照PriorityOrdered或者Ordered的顺序调用。

