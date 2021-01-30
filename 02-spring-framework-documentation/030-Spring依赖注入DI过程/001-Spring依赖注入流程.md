# Spring依赖注入过程

[TOC]

<img src="../../assets/image-20200919224648982.png" alt="image-20200919224648982" style="zoom:80%;" />

## 基本步骤

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

 [002-DI第一步-转换对应beanName.md](002-DI第一步-转换对应beanName.md) 

或许很多人不理解转换对应beanName是什么意思，传入的参数name不就是beanName吗？其实不是，这里传入的参数可能是别名，也可能是FactoryBean，所以需要进行一系列的解析，这些解析内容包括如下内容。

去除FactoryBean的修饰符，也就是如果name="&aa"，那么会首先去除&而使name="aa"。

取指定alias所表示的最终beanName，例如别名A指向名称为B的bean则返回B；若别名A指向别名B，别名B又指向名称为C

### 尝试从缓存中加载单例

 [003-DI第二步-尝试从缓存中加载单例.md](003-DI第二步-尝试从缓存中加载单例.md) 

单例在Spring的同一个容器内只会被创建一次，后续再获取bean，就直接从单例缓存中获取了。当然这里也只是尝试加载，首先尝试从缓存中加载，如果加载不成功则再次尝试从singletonFactories中加载。因为在创建单例bean的时候会存在依赖注入的情况，而在创建依赖的时候为了避免循环依赖，在Spring中创建bean的原则是不等bean创建完成就会将创建bean的ObjectFactory提早曝光加入到缓存中，一旦下一个bean创建时候需要依赖上一个bean则直接使用ObjectFactory。

### bean的实例化

 [004-DI第三步-bean的实例化.md](004-DI第三步-bean的实例化.md) 

如果从缓存中得到了bean的原始状态，则需要对bean进行实例化。这里有必要强调一下，缓存中记录的只是最原始的bean状态，并不一定是我们最终想要的bean。举个例子，假如我们需要对工厂bean进行处理，那么这里得到的其实是工厂bean的初始状态，但是我们真正需要的是工厂bean中定义的 factory-method方法中返回的 bean，而 getObjectForBeanInstance 就是完成这个工作的，后续会详细讲解。

### 原型模式的依赖检查

 [005-DI第四步-原型模式的依赖检查.md](005-DI第四步-原型模式的依赖检查.md) 

只有在单例情况下才会尝试解决循环依赖，如果存在A中有B的属性，B中有A的属性，那么当依赖注入的时候，就会产生当A还未创建完的时候因为对于B的创建再次返回创建A，造成循环依赖，也就是情况：isPrototypeCurrentlyInCreation(beanName) 判断true。

### 检测parentBeanFactory

 [006-DI第五步-检测parentBeanFactory.md](006-DI第五步-检测parentBeanFactory.md) 

从代码上看，如果缓存没有数据的话直接转到父类工厂上去加载了，这是为什么呢？
可能读者忽略了一个很重要的判断条件：parentBeanFactory != null && !containsBean Definition (beanName)，parentBeanFactory != null。parentBeanFactory如果为空，则其他一切都是浮云，这个没什么说的，但是!containsBeanDefinition(beanName)就比较重要了，它是在检测如果当前加载的XML配置文件中不包含beanName所对应的配置，就只能到parentBeanFactory去尝试下了，然后再去递归的调用getBean方法。

### 将存储XML配置文件的GernericBeanDefinition转换为RootBeanDefinition

 [007-DI第六步-将存储XML配置文件的GernericBeanDefinition转换为RootBeanDefinition.md](007-DI第六步-将存储XML配置文件的GernericBeanDefinition转换为RootBeanDefinition.md) 

因为从XML配置文件中读取到的bean信息是存储在GernericBeanDefinition中的，但是所有的bean后续处理都是针对于RootBeanDefinition的，所以这里需要进行一个转换，转换的同时如果父类bean不为空的话，则会一并合并父类的属性。

### 寻找依赖

 [008-DI第七步Spring依赖注入流程-寻找依赖.md](008-DI第七步Spring依赖注入流程-寻找依赖.md) 

因为bean的初始化过程中很可能会用到某些属性，而某些属性很可能是动态配置的，并且配置成依赖于其他的bean，那么这个时候就有必要先加载依赖的bean，所以，在Spring的加载顺寻中，在初始化某一个bean的时候首先会初始化这个bean所对应的依赖。

### 针对不同的scope进行bean的创建

 [009-DI第八步-针对不同的scope进行bean的创建.md](009-DI第八步-针对不同的scope进行bean的创建.md) 

我们都知道，在Spring中存在着不同的scope，其中默认的是singleton，但是还有些其他的配置诸如prototype、request之类的。在这个步骤中，Spring会根据不同的配置进行不同的初始化策略。

### 类型转换

 [010-DI第九步-类型转换.md](010-DI第九步-类型转换.md) 

程序到这里返回bean后已经基本结束了，通常对该方法的调用参数 requiredType 是为空的，但是可能会存在这样的情况，返回的bean其实是个String，但是requiredType 却传入Integer类型，那么这时候本步骤就会起作用了，它的功能是将返回的bean转换为requiredType所指定的类型。当然，String转换为Integer是最简单的一种转换，在Spring中提供了各种各样的转换器，用户也可以自己扩展转换器来满足需求。

经过上面的步骤后 bean 的加载就结束了，这个时候就可以返回我们所需要的 bean了，下图直观地反映了整个过程。

其中最重要的就是步骤8，针对不同的 scope 进行 bean 的创建，你会看到各种常用的 Spring 特性在这里的实现。

![image-20200922192538797](../../assets/image-20200922192538797.png)