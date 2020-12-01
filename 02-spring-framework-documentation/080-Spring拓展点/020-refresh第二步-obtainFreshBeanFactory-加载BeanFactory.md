# 第二步-obtainFreshBeanFactory-加载BeanFactory

![image-20201007151953236](../../assets/image-20201007151953236.png)

refresh方法中的第二步:

```java
//2、告诉子类启动refreshBeanFactory()方法，Bean定义资源文件的载入从
//子类的refreshBeanFactory()方法启动
ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
```

我们可以

- [定制BeanFactory](#定制BeanFactory)
- [定制加载BeanDefinition的逻辑](#定制加载BeanDefinition的逻辑)

## 简介

obtainFreshBeanFactory方法从字面理解是获取BeanFactory。之前有说过，ApplicationContext是对BeanFactory的功能上的扩展，不但包含了BeanFactory的全部功能,更在其基础上添加了大量的拓展应用

```java
ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();
```

经过这一步之后,ApplicationContext 就拥有了BeanFactory的全部功能

主要实现

```java
@Override
protected final void refreshBeanFactory() throws BeansException {
  //如果已经有容器，销毁容器中的bean，关闭容器
  if (hasBeanFactory()) {
    destroyBeans();
    closeBeanFactory();
  }
  try {
    //创建IOC容器
    DefaultListableBeanFactory beanFactory = createBeanFactory();
    beanFactory.setSerializationId(getId());
    //对IOC容器进行定制化，如设置启动参数，开启注解的自动装配等
    customizeBeanFactory(beanFactory);
    //调用载入Bean定义的方法，主要这里又使用了一个委派模式，在当前类中只定义了抽象的loadBeanDefinitions方法，具体的实现调用子类容器
    loadBeanDefinitions(beanFactory);
    synchronized (this.beanFactoryMonitor) {
      this.beanFactory = beanFactory;
    }
  }
  catch (IOException ex) {
    throw new ApplicationContextException("I/O error parsing bean definition source for " + getDisplayName(), ex);
  }
}
```

我们详细分析上面的每个步骤。

1. 创建DefaultListableBeanFactory。
   在介绍BeanFactory的时候，不知道读者是否还有印象，声明方式为：`BeanFactory bf = new XmlBeanFactory("beanFactoryTest.xml")`，其中的XmlBeanFactory继承自DefaultListableBeanFactory，并提供了XmlBeanDefinitionReader类型的reader属性，也就是说DefaultListableBeanFactory是容器的基础。必须首先要实例化，那么在这里就是实例化DefaultListableBeanFactory的步骤。
2. 指定序列化ID。
3. 定制BeanFactory。
4. 加载BeanDefinition。
5. 使用全局变量记录BeanFactory类实例。
   因为DefaultListableBeanFactory类型的变量beanFactory是函数内的局部变量，所以要使用全局变量记录解析结果。

## 定制BeanFactory

这里已经开始了对BeanFactory的扩展，在基本容器的基础上，增加了是否允许覆盖是否允许扩展的设置并提供了注解@Qualifier和@Autowired的支持。

```java
protected void customizeBeanFactory(DefaultListableBeanFactory beanFactory) {
         //如果属性allowBeanDefinitionOverriding不为空，设置给beanFactory对象相应属性，
         //此属性的含义：是否允许覆盖同名称的不同定义的对象
         if (this.allowBeanDefinitionOverriding != null) {
             beanFactory.setAllowBeanDefinitionOverriding(this.allowBeanDefinitionOverriding);
         }
         //如果属性allowCircularReferences不为空，设置给beanFactory对象相应属性，         
  			//此属性的含义：是否允许bean之间存在循环依赖
         if (this.allowCircularReferences != null) {
             beanFactory.setAllowCircularReferences(this.allowCircularReferences);
         }
         //用于@Qualifier和@Autowired 
         beanFactory.setAutowireCandidateResolver(new QualifierAnnotationAutowireCandidateResolver  
());
}
```

对于允许覆盖和允许依赖的设置这里只是判断了是否为空，如果不为空要进行设置，但是并没有看到在哪里进行设置，究竟这个设置是在哪里进行设置的呢？还是那句话，使用子类覆盖方法，例如：

```java
public class MyClassPathXmlApplicationContext extends ClassPathXmlApplicationContext{
     ... ...
     protected void  customizeBeanFactory(DefaultListableBeanFactory beanFactory) {
             super.setAllowBeanDefinitionOverriding(false);
             super.setAllowCircularReferences(false);
             super.customizeBeanFactory(beanFactory);
     }
}
```

设置完后相信大家已经对于这两个属性的使用有所了解，或者可以回到前面的章节进行再一次查看。对于定制BeanFactory，Spring还提供了另外一个重要的扩展，就是设置AutowireCandidateResolver，在bean加载部分中讲解创建Bean时，

- 如果采用autowireByType方式注入，那么默认会使用Spring提供的SimpleAutowireCandidateResolver，而对于默认的实现并没有过多的逻辑处理。

在这里，Spring使用了QualifierAnnotationAutowireCandidateResolver，设置了这个解析器后Spring就可以支持注解方式的注入了。
在讲解根据类型自定注入的时候，我们说过解析autowire类型时首先会调用方法：

```java
Object value = getAutowireCandidateResolver().getSuggestedValue(descriptor);
```

因此我们知道，在QualifierAnnotationAutowireCandidateResolver中一定会提供了解析Qualifier与Autowire注解的方法。

```java
//QualifierAnnotationAutowireCandidateResolver.java
public Object getSuggestedValue(DependencyDescriptor descriptor) {
  Object value = findValue(descriptor.getAnnotations());
  if (value == null) {
    MethodParameter methodParam = descriptor.getMethodParameter();
    if (methodParam != null) {
      value = findValue(methodParam.getMethodAnnotations());
    }
  }
  return value;
}
```

## 定制加载BeanDefinition的逻辑

在第一步中提到了将ClassPathXmlApplicationContext与XmlBeanFactory创建的对比，在实现配置文件的加载功能中除了我们在第一步中已经初始化的DefaultListableBeanFactory外，还需要XmlBeanDefinitionReader来读取XML，那么在这个步骤中首先要做的就是初始化XmlBeanDefinitionReader。


```java
@Override
     protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException, IOException {
     //为指定beanFactory创建XmlBeanDefinitionReader
     XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory);
		 //对beanDefinitionReader进行环境变量的设置
     beanDefinitionReader.setEnvironment(this.getEnvironment());
     beanDefinitionReader.setResourceLoader(this);
     beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));
     //对BeanDefinitionReader进行设置，可以覆盖
     initBeanDefinitionReader(beanDefinitionReader);
     loadBeanDefinitions(beanDefinitionReader);
}
```
在初始化了DefaultListableBeanFactory和XmlBeanDefinitionReader后就可以进行配置文件的读取了。

```java
protected void loadBeanDefinitions(XmlBeanDefinitionReader reader) throws BeansException, IOException {
         Resource[] configResources = getConfigResources();if (configResources != null) {
             reader.loadBeanDefinitions(configResources);
         }
         String[] configLocations = getConfigLocations();
         if (configLocations != null) {
             reader.loadBeanDefinitions(configLocations);
         }
}
```

使用XmlBeanDefinitionReader的loadBeanDefinitions方法进行配置文件的加载机注册相信大家已经不陌生，这完全就是开始BeanFactory的套路。

因为在XmlBeanDefinitionReader中已经将之前初始化的 DefaultListableBeanFactory 注册进去了，所以XmlBeanDefinitionReader所读取的BeanDefinitionHolder都会注册到DefaultListableBeanFactory中，也就是经过此步骤，类型DefaultListableBeanFactory的变量beanFactory已经包含了所有解析好的配置。

