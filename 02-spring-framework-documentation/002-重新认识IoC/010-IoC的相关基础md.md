# 重新认识IoC

1. [什么是IoC](#什么是IoC)
2. [IoC主要实现策略](#IoC主要实现策略)
3. IoC容器的职责
4. [IoC容器的实现](#IoC容器的实现)
5. 传统IoC容器实现
6. [轻量级IoC容器](#轻量级IoC容器)
7. [依赖查找VS依赖注入](#依赖查找VS依赖注入)
8. [构造器注入VSSetter注入](#构造器注入VSSetter注入)
9. 面试题

## 什么是IoC

简单来说,IoC是翻转控制,类似于好莱坞原则,主要有

- 依赖查找
- 依赖注入

两种实现方式

那么具体实现说呢,IoC有很多种实现方式

- JavaBeans是一种实现方式
- Servlet容器也是一种实现方式,因为Serlvet可以通过依赖或者JNDI的方式获取到外部的资源,包括DataSource 或者相关的EJB的组件
- SpringFrame 依赖注入的框架也可以帮助我们实现IoC



## IoC主要实现策略

- 服务定义模式 , JNDI
- 依赖注入(DI)
  - 构造注入
  - 参数注入
  - Setter注入
  - 接口注入
- 依赖查询(Dependency Lookup)
- 模板方法设计模式
- 使用策略设计模式

#### 依赖查询

Depenceny Lookup: The Container provides callbacks to components, and a look up context . this is  The EJB and Apache Avalon approach

It leaves the onus on each component to use container APIs to  lookup resources and collaborators

The Inversion of Control is limited to the container invoking callbackmethods that application code can use to obtain resources

通常传统的Java EE 或者EJB 呢都实现的是依赖查找而不是依赖注入

#### 依赖注入

组件不需要查找,通常是由容器帮我们自动去注入一些事情,或者我们手动注入一些事情

# IoC容器的职责

- 通用职责
- 依赖处理: 依赖是真么来的,以及怎么返回客户端的
  - 依赖查找
  - 依赖注入
- 容器周期管理
  - 容器
  - 托管的资源(Java Beans 或者其他资源)
- 配置
  - 容器
  - 外部化配置
  - 托管的资源(JavaBeans 或者其他资源)

## IoC容器的实现

Java SE

- Java Beans
- Java ServiceLoder SPI
- JNDI (Java Naming and Directory Interface)

Java EE

- EJB(Enterprise Java Beans)

- Servlet

  > 

开源

- Apache Avalon
- PicoContainer
- Google Guice
- Spring Framework

## 轻量级IoC容器

- A container that can manage application code 
- A container that is quick to start up
- A container that doesn't require any special deployment steps to deploy object within it 
- A container that has such a light footprint and minimal API dependencies that it can be run in variety of environments 
- A container that sets the bar for adding a managed object so low in terms of deployment effort and preformance overhead that It's passible to deploy and manage fine-grained objects , as well as coaurse-grained compinents 

## 依赖查找VS依赖注入

| 类型           | 依赖处理 | 实现便利性 | 代码入侵性          | APi依赖性     | 可读性 |
| -------------- | -------- | ---------- | ------------------- | ------------- | ------ |
| 依赖查找       | 主动获取 | 相对繁琐   | 侵入业务逻辑        | 依赖容器API   | 良好   |
| 被动提供(push) | 被动提供 | 相对便利   | 低侵入性(Autowired) | 不依赖容器API | 一般   |

通常情况下依赖查找必须依赖容器中的API

- JNDI 比如说LimitContextLookup 

- 不依赖API不代表没有侵入性

## 构造器注入VSSetter注入

- Spring团队通常鼓励使用构造注入
  - 确保对象是一个不变的,确保完整的初始化状态
  - ObjectProvider.getIfAvailable
  - 如果参数过多,代码就是特别良好,我们应该重构
- Setter注入仅用于可选性的注入
  - Set方法可以让对象更加可配