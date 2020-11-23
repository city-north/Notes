# 030-SpringBean注册阶段

补充说明 [030-BeanDefinition的注册.md](../003-SpringBean的定义-BeanDefiniation/030-BeanDefinition的注册.md) 

- BeanDefinition注册接口
  - BeanDefinitionRegistry

DefaultListableBeanFactory实际上实现了注册中心 

## BeanDefinitionRegistry

```java
//BeanDefinitionRegistry#registerBeanDefinition
@Override
public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition)
      throws BeanDefinitionStoreException {

	...

	//判断是否存在
   BeanDefinition existingDefinition = this.beanDefinitionMap.get(beanName);
  
  
   ///////////////---------------------bean存在---------------------------/////////////////// 
   if (existingDefinition != null) {
     //是否允许覆盖,默认情况下是true; SpringBoot 里面显示的将这个值改为false
      if (!isAllowBeanDefinitionOverriding()) {
         throw new BeanDefinitionOverrideException(beanName, beanDefinition, existingDefinition);
      }
      else if (existingDefinition.getRole() < beanDefinition.getRole()) {
         // e.g. was ROLE_APPLICATION, now overriding with ROLE_SUPPORT or ROLE_INFRASTRUCTURE
         if (logger.isInfoEnabled()) {
            logger.info("Overriding user-defined bean definition for bean '" + beanName +
                  "' with a framework-generated bean definition: replacing [" +
                  existingDefinition + "] with [" + beanDefinition + "]");
         }
      }
      else if (!beanDefinition.equals(existingDefinition)) {
         if (logger.isDebugEnabled()) {
            logger.debug("Overriding bean definition for bean '" + beanName +
                  "' with a different definition: replacing [" + existingDefinition +
                  "] with [" + beanDefinition + "]");
         }
      }
      else {
         if (logger.isTraceEnabled()) {
            logger.trace("Overriding bean definition for bean '" + beanName +
                  "' with an equivalent definition: replacing [" + existingDefinition +
                  "] with [" + beanDefinition + "]");
         }
      }
     //设置到注册中心
      this.beanDefinitionMap.put(beanName, beanDefinition);
   }
 ///////////////---------------------bean不存在---------------------------/////////////////// 
 
  //如果不存在
   else {
     //是否已经创建
      if (hasBeanCreationStarted()) {
         // Cannot modify startup-time collection elements anymore (for stable iteration)
         synchronized (this.beanDefinitionMap) {
            this.beanDefinitionMap.put(beanName, beanDefinition);
            List<String> updatedDefinitions = new ArrayList<>(this.beanDefinitionNames.size() + 1);
            updatedDefinitions.addAll(this.beanDefinitionNames);
            updatedDefinitions.add(beanName);
            this.beanDefinitionNames = updatedDefinitions;
            removeManualSingletonName(beanName);
         }
      }
      else 
        //正在创建
         // Still in startup registration phase
         this.beanDefinitionMap.put(beanName, beanDefinition);
     	//由于ConcurrentHashMap是乱序的,但是要确保Bean创建的顺序,所以要用一个ArrayList去维护一个名字的列表
         this.beanDefinitionNames.add(beanName);
         removeManualSingletonName(beanName);
      }
      this.frozenBeanDefinitionNames = null;
   }

   if (existingDefinition != null || containsSingleton(beanName)) {
      resetBeanDefinition(beanName);
   }
}
```

## 值得注意的是

```java
private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);
/** List of bean definition names, in registration order. */
private volatile List<String> beanDefinitionNames = new ArrayList<>(256);//保持注册的顺序
```

- 由于ConcurrentHashMap是乱序的,但是要确保Bean创建(注册)的顺序,所以要用一个ArrayList去维护一个名字的列表

