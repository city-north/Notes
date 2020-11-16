# 从bean的实例中获取对象

在getBean方法中，getObjectForBeanInstance是个高频率使用的方法，无论是从缓存中获得bean还是根据不同的scope策略加载bean。总之，我们得到bean的实例后要做的第一步就是调用这个方法来检测一下正确性，

其实就是用于检测当前bean是否是FactoryBean类型的bean，如果是，那么需要调用该bean对应的FactoryBean实例中的getObject()作为返回值。

无论是从缓存中获取到的bean还是通过不同的scope策略加载的bean都只是最原始的bean状态，并不一定是我们最终想要的bean。举个例子，假如我们需要对工厂bean进行处理，那么这里得到的其实是工厂bean的初始状态，但是我们真正需要的是工厂bean中定义的factory-method方法中返回的bean，而getObjectForBeanInstance方法就是完成这个工作的。





```java
   protected Object getObjectForBeanInstance( Object beanInstance, String name, String beanName, RootBeanDefinition mbd) {
   //如果指定的name是工厂相关(以&为前缀)且beanInstance又不是FactoryBean类型则验证不通过
     if (BeanFactoryUtils.isFactoryDereference(name) && !(beanInstance instanceof FactoryBean)) {
         throw new BeanIsNotAFactoryException(transformedBeanName(name), beanInstance.etClass());
         }
     //现在我们有了个bean的实例，这个实例可能会是正常的bean或者是FactoryBean
     //如果是FactoryBean我们使用它创建实例，但是如果用户想要直接获取工厂实例而不是工厂的  
     //getObject方法对应的实例那么传入的name应该加入前缀&
     if (!(beanInstance instanceof FactoryBean) || BeanFactoryUtils. IsFactoryDereference(name)) {
             return beanInstance;
         }
              //加载FactoryBean
     Object object = null;
     if (mbd == null) {
         //尝试从缓存中加载bean
         object = getCachedObjectForFactoryBean(beanName);
     }
     if (object == null) {
         //到这里已经明确知道beanInstance一定是FactoryBean类型
         FactoryBean<?> factory = (FactoryBean<?>) beanInstance;
         //containsBeanDefinition检测beanDefinitionMap中也就是在所有已经加载的类中检测  
         //是否定义beanName
         if (mbd == null && containsBeanDefinition(beanName)) {
              //将存储XML配置文件的GernericBeanDefinition转换为RootBeanDefinition，如  
          //果指定BeanName是子Bean的话同时会合并父类的相关属性
             mbd = getMergedLocalBeanDefinition(beanName);
         }
         //是否是用户定义的而不是应用程序本身定义的
         boolean synthetic = (mbd != null && mbd.isSynthetic());
         object = getObjectFromFactostance.  
 getClass());
         }

         //现在我们有了个bean的实例，这个实例可能会是正常的bean或者是FactoryBean
         //如果是FactoryBean我们使用它创建实例，但是如果用户想要直接获取工厂实例而不是工厂的  
         //getObject方法对应的实例那么传入的name应该加入前缀&
         if (!(beanInstance instanceof FactoryBean) || BeanFactoryUtils. IsFactory   
Dereference(name)) {
             return beanInstance;
         }

         //加载FactoryBean
         Object object = null;
         if (mbd == null) {
             //尝试从缓存中加载bean
             object = getCachedObjectForFactoryBean(beanName);
         }
         if (object == null) {
             //到这里已经明确知道beanInstance一定是FactoryBean类型
             FactoryBean<?> factory = (FactoryBean<?>) beanInstance;
             //containsBeanDefinition检测beanDefinitionMap中也就是在所有已经加载的类中检测  
             //是否定义beanName
             if (mbd == null && containsBeanDefinition(beanName)) {
                  //将存储XML配置文件的GernericBeanDefinition转换为RootBeanDefinition，如  
              //果指定BeanName是子Bean的话同时会合并父类的相关属性
                 mbd = getMergedLocalBeanDefinition(beanName);
             }
             //是否是用户定义的而不是应用程序本身定义的
             boolean synthetic = (mbd != null && mbd.isSynthetic());
             object = getObjectFromFactoryBean(factory, beanName, !synthetic);
         }
         return object;
}
```
从上面的代码来看，其实这个方法并没有什么重要的信息，大多是些辅助代码以及一些功能性的判断，而真正的核心代码却委托给了getObjectFromFactoryBean，我们来看看getObjectForBeanInstance中的所做的工作。

- 对FactoryBean正确性的验证。
- 对非FactoryBean不做任何处理。
- 对bean进行转换。
- 将从Factory中解析bean的工作委托给getObjectFromFactoryBean。

