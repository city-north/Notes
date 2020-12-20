# 110-第十一步-初始化所有剩余的非lazy单例Bean

![image-20201007151953236](../../assets/image-20201007151953236.png)

[TOC]

## 主要工作

完成BeanFactory的初始化工作，其中包括ConversionService的设置、配置冻结以及非延迟加载的bean的初始化工作。

## preInstantiateSingletons

完成BeanFactory的初始化工作，其中包括ConversionService的设置、配置冻结以及非延迟加载的bean的初始化工作。

```java
protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {
         // Initialize conversion service for this context.
         if (beanFactory.containsBean(CONVERSION_SERVICE_BEAN_NAME) &&
                 beanFactory.isTypeMatch(CONVERSION_SERVICE_BEAN_NAME, ConversionService.class)) {
             beanFactory.setConversionService(
                     beanFactory.getBean(CONVERSION_SERVICE_BEAN_NAME, ConversionService.class));
         }
       	// Initialize LoadTimeWeaverAware beans early to allow for registering their   
        //transformers early.
         String[] weaverAwareNames = beanFactory.getBeanNamesForType (LoadTimeWeaverAware. class, false, false);
         for (String weaverAwareName : weaverAwareNames) {
             getBean(weaverAwareName);
         }

         // Stop using the temporary ClassLoader for type matching.
         beanFactory.setTempClassLoader(null);

         //冻结所有的bean定义，说明注册的bean定义将不被修改或任何进一步的处理
         beanFactory.freezeConfiguration();

         // Instantiate all remaining (non-lazy-init) singletons.
         //初始化剩下的单实例（非惰性的）
         beanFactory.preInstantiateSingletons();
  }
  
```

