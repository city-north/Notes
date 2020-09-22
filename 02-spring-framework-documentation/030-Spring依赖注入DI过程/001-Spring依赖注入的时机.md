# Spring依赖注入的时机

# 目录

- [依赖注入发生的时间](#依赖注入发生的时间)
- [核心接口](#核心接口)

---

## 依赖注入发生的时间

当Spring IoC容器完成了Bean定义资源的定位、载入和解析注册，IoC容器就可以管理Bean定义的相关数据了，但是此时IoC容器还没有对所管理的Bean进行依赖注入（DI），依赖注入在以下两种情况下发生：

- 用户第一次调用 **getBean()** 方法时，IoC容器触发依赖注入。
- 当用户在配置文件中将`＜bean＞`元素配置了 lazy-init=false 属性时，即让容器在解析注册Bean定义时进行预实例化，触发依赖注入。

## 核心接口

- [BeanFactory](#BeanFactory)
- [AbstractBeanFactory](#AbstractBeanFactory)

#### BeanFactory

BeanFactory 接口定义了Spring IoC容器的基本功能规范，是Spring IoC容器所应遵守的最低层和最基本的编程规范。

BeanFactory接口中定义了几个 **getBean** 方法，用于用户向IoC容器索取被管理的Bean的方法，我们通过分析其子类的具体实现来理解Spring IoC容器在用户索取Bean时如何完成依赖注入。

在BeanFactory中我们可以看到 getBean（String...）方法，但它的具体实现在 AbstractBeanFactory 中

#### AbstractBeanFactory

通过向IoC容器获取Bean的方法的分析，我们可以看到，在Spring中如果Bean定义为单例模式（Singleton）的，则容器在创建之前先从缓存中查找，以确保整个容器中只存在一个实例对象。如果Bean定义为原型模式（Prototype）的，则容器每次都会创建一个新的实例对象。除此之外，Bean定义还可以指定其生命周期范围。

- org.springframework.beans.factory.support.AbstractBeanFactory#getBean(java.lang.String, java.lang.Class<T>)
- org.springframework.beans.factory.support.AbstractBeanFactory#doGetBean

 [AbstractBeanFactory源码](010-核心类/010-AbstractBeanFactory.md) 

上面的源码只定义了根据 Bean 定义的不同模式采取的创建 Bean 实例对象的不同策略，具体的Bean 实例对象的创建过程由实现了 ObjectFactory 接口的匿名内部类的 createBean()方法完成，ObjectFactory 接口使 用 委 派 模 式，具体的 Bean 实例创建过程交由其实现类 AbstractAutowireCapableBeanFactory 完成。下面我们继续分析AbstractAutowireCapableBeanFactory的createBean()方法的源码，理解创建Bean实例的具体过程。

- org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory#createBean(java.lang.String, org.springframework.beans.factory.support.RootBeanDefinition, java.lang.Object[])

## 初始化流程

- [020-AbstractAutowireCapableBeanFactory.md](010-核心类/020-AbstractAutowireCapableBeanFactory.md) 
-  [030-SimpleInstantiationStrategy.md](010-核心类/030-SimpleInstantiationStrategy.md) 

## 初始化完毕

## 依赖注入

![一步一步手绘Spring DI运行时序图](../../assets/一步一步手绘Spring DI运行时序图.png)

## AbstractBeanFactory的getBean()方法

1. [转换对应beanName](#转换对应beanName)

2. [尝试从缓存中加载单例](#尝试从缓存中加载单例)

3. [bean的实例化](#bean的实例化)

4. [原型模式的依赖检查](#原型模式的依赖检查)

5. [检测parentBeanFactory](#检测parentBeanFactory)

6. [将存储XML配置文件的GenericBeanDefinition转化成RootBeanDefinition](#将存储XML配置文件的GenericBeanDefinition转化成RootBeanDefinition)

7. [寻找依赖](#寻找依赖)

8. [针对不同的scope进行bean的创建](#针对不同的scope进行bean的创建)

9. [类型转换](#类型转换)

### 转换对应beanName

或许很多人不理解转换对应beanName是什么意思，传入的参数name不就是beanName吗？其实不是，这里传入的参数可能是别名，也可能是FactoryBean，所以需要进行一系列的解析，这些解析内容包括如下内容。

去除FactoryBean的修饰符，也就是如果name="&aa"，那么会首先去除&而使name="aa"。

取指定alias所表示的最终beanName，例如别名A指向名称为B的bean则返回B；若别名A指向别名B，别名B又指向名称为C

### 尝试从缓存中加载单例

单例在Spring的同一个容器内只会被创建一次，后续再获取bean，就直接从单例缓存中获取了。当然这里也只是尝试加载，首先尝试从缓存中加载，如果加载不成功则再次尝试从singletonFactories中加载。因为在创建单例bean的时候会存在依赖注入的情况，而在创建依赖的时候为了避免循环依赖，在Spring中创建bean的原则是不等bean创建完成就会将创建bean的ObjectFactory提早曝光加入到缓存中，一旦下一个bean创建时候需要依赖上一个bean则直接使用ObjectFactory。

### bean的实例化

如果从缓存中得到了bean的原始状态，则需要对bean进行实例化。这里有必要强调一下，缓存中记录的只是最原始的bean状态，并不一定是我们最终想要的bean。举个例子，假如我们需要对工厂bean进行处理，那么这里得到的其实是工厂bean的初始状态，但是我们真正需要的是工厂bean中定义的factory-method方法中返回的bean，而getObjectForBeanInstance就是完成这个工作的，后续会详细讲解。

### 原型模式的依赖检查

只有在单例情况下才会尝试解决循环依赖，如果存在A中有B的属性，B中有A的属性，那么当依赖注入的时候，就会产生当A还未创建完的时候因为对于B的创建再次返回创建A，造成循环依赖，也就是情况：isPrototypeCurrentlyInCreation(beanName)判断true。

### 检测parentBeanFactory

从代码上看，如果缓存没有数据的话直接转到父类工厂上去加载了，这是为什么呢？
可能读者忽略了一个很重要的判断条件：parentBeanFactory != null && !containsBean Definition (beanName)，parentBeanFactory != null。parentBeanFactory如果为空，则其他一切都是浮云，这个没什么说的，但是!containsBeanDefinition(beanName)就比较重要了，它是在检测如果当前加载的XML配置文件中不包含beanName所对应的配置，就只能到parentBeanFactory去尝试下了，然后再去递归的调用getBean方法。

#### 将存储XML配置文件的GernericBeanDefinition转换为RootBeanDefinition

因为从XML配置文件中读取到的bean信息是存储在GernericBeanDefinition中的，但是所有的bean后续处理都是针对于RootBeanDefinition的，所以这里需要进行一个转换，转换的同时如果父类bean不为空的话，则会一并合并父类的属性。

### 寻找依赖

因为bean的初始化过程中很可能会用到某些属性，而某些属性很可能是动态配置的，并且配置成依赖于其他的bean，那么这个时候就有必要先加载依赖的bean，所以，在Spring的加载顺寻中，在初始化某一个bean的时候首先会初始化这个bean所对应的依赖。

### 针对不同的scope进行bean的创建

我们都知道，在Spring中存在着不同的scope，其中默认的是singleton，但是还有些其他的配置诸如prototype、request之类的。在这个步骤中，Spring会根据不同的配置进行不同的初始化策略。

### 类型转换

程序到这里返回bean后已经基本结束了，通常对该方法的调用参数requiredType是为空的，但是可能会存在这样的情况，返回的bean其实是个String，但是requiredType却传入Integer类型，那么这时候本步骤就会起作用了，它的功能是将返回的bean转换为requiredType所指定的类型。当然，String转换为Integer是最简单的一种转换，在Spring中提供了各种各样的转换器，用户也可以自己扩展转换器来满足需求。

经过上面的步骤后bean的加载就结束了，这个时候就可以返回我们所需要的bean了，下图直观地反映了整个过程。

其中最重要的就是步骤8，针对不同的scope进行bean的创建，你会看到各种常用的Spring特性在这里的实现。

![image-20200922192538797](../../assets/image-20200922192538797.png)