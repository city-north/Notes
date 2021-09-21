# 110-事件总线

[TOC]

传统上，Java的**进程内事件分发**都是通过发布者和订阅者之间的显式注册实现的。设计[EventBus](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/eventbus/EventBus.html)就是为了取代这种显示注册方式，使组件间有了更好的解耦。EventBus不是通用型的发布-订阅实现，不适用于进程间通信。

## 范例

```java
public class EventBusTest {

    public static void main(String[] args) {
        EventBus eventBus = new EventBus();
        eventBus.register(new EventBusChangeRecorder());
        MyEventObject event = new MyEventObject("123");
        eventBus.post(event);
    }

    static class EventBusChangeRecorder {
        @Subscribe
        public void recordCustomerChange(EventObject e) {
            System.out.println(e);
        }
    }
    static class MyEventObject extends EventObject {
        public MyEventObject(Object source) {
            super(source);
        }
    }
}
```



## 一分钟指南

把已有的进程内事件分发系统迁移到EventBus非常简单。

### 事件监听者[Listeners]

监听特定事件（如，CustomerChangeEvent）：

- 传统实现：定义相应的事件监听者类，如CustomerChangeEventListener；
- EventBus实现：以CustomerChangeEvent为唯一参数创建方法，并用[Subscribe](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/eventbus/Subscribe.html)注解标记。

把事件监听者注册到事件生产者：

- 传统实现：调用事件生产者的registerCustomerChangeEventListener方法；这些方法很少定义在公共接口中，因此开发者必须知道所有事件生产者的类型，才能正确地注册监听者；
- EventBus实现：在EventBus**实例**上调用[EventBus.register(Object)](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/eventbus/EventBus.html#register(java.lang.Object))方法；请保证事件生产者和监听者共享相同的EventBus**实例**。

按事件超类监听（如，EventObject甚至Object）：

- 传统实现：很困难，需要开发者自己去实现匹配逻辑；
- EventBus实现：EventBus自动把事件分发给事件超类的监听者，并且允许监听者声明监听接口类型和泛型的通配符类型（wildcard，如 ? super XXX）。

检测没有监听者的事件：

- 传统实现：在每个事件分发方法中添加逻辑代码（也可能适用AOP）；
- EventBus实现：监听[DeadEvent](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/eventbus/DeadEvent.html)；EventBus会把所有发布后没有监听者处理的事件包装为DeadEvent（对调试很便利）。

### 事件生产者[Producers]

管理和追踪监听者：

- 传统实现：用列表管理监听者，还要考虑线程同步；或者使用工具类，如EventListenerList；
- EventBus实现：EventBus内部已经实现了监听者管理。

向监听者分发事件：

- 传统实现：开发者自己写代码，包括事件类型匹配、异常处理、异步分发；
- EventBus实现：把事件传递给 [EventBus.post(Object)](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/eventbus/EventBus.html#post(java.lang.Object))方法。异步分发可以直接用EventBus的子类[AsyncEventBus](http://docs.guava-libraries.googlecode.com/git-history/release/javadoc/com/google/common/eventbus/AsyncEventBus.html)。

## 术语表

事件总线系统使用以下术语描述事件分发：

| 事件     | 可以向事件总线发布的对象                                     |
| -------- | ------------------------------------------------------------ |
| 订阅     | 向事件总线注册*监听者*以接受事件的行为                       |
| 监听者   | 提供一个*处理方法*，希望接受和处理事件的对象                 |
| 处理方法 | 监听者提供的公共方法，事件总线使用该方法向监听者发送事件；该方法应该用Subscribe注解 |
| 发布消息 | 通过事件总线向所有匹配的监听者提供事件                       |

## 常见问题解答[FAQ]

##### **为什么一定要创建****EventBus****实例，而不是使用单例模式？**

EventBus不想给定开发者怎么使用；你可以在应用程序中按照不同的组件、上下文或业务主题分别使用不同的事件总线。这样的话，在测试过程中开启和关闭某个部分的事件总线，也会变得更简单，影响范围更小。

当然，如果你想在进程范围内使用唯一的事件总线，你也可以自己这么做。比如在容器中声明EventBus为全局单例，或者用一个静态字段存放EventBus，如果你喜欢的话。

简而言之，EventBus不是单例模式，是因为我们不想为你做这个决定。你喜欢怎么用就怎么用吧。

**可以从事件总线中注销监听者吗？**  

当然可以，使用EventBus.unregister(Object)方法，但我们发现这种需求很少：

- 大多数监听者都是在启动或者模块懒加载时注册的，并且在应用程序的整个生命周期都存在；
- 可以使用特定作用域的事件总线来处理临时事件，而不是注册/注销监听者；比如在请求作用域[request-scoped]的对象间分发消息，就可以同样适用请求作用域的事件总线；
- 销毁和重建事件总线的成本很低，有时候可以通过销毁和重建事件总线来更改分发规则。

**为什么使用注解标记处理方法，而不是要求监听者实现接口？**

我们觉得注解和实现接口一样传达了明确的语义，甚至可能更好。同时，使用注解也允许你把处理方法放到任何地方，和使用业务意图清晰的方法命名。

传统的Java实现中，监听者使用方法很少的接口——通常只有一个方法。这样做有一些缺点:

- 监听者类对给定事件类型，只能有单一处理逻辑；
- 监听者接口方法可能冲突；
- 方法命名只和事件相关（handleChangeEvent），不能表达意图（recordChangeInJournal）；
- 事件通常有自己的接口，而没有按类型定义的公共父接口（如所有的UI事件接口）。

接口实现监听者的方式很难做到简洁，这甚至引出了一个模式，尤其是在Swing应用中，那就是用匿名类实现事件监听者的接口。比较以下两种实现：第二种实现的业务意图明显更加清晰：没有多余的代码，并且处理方法的名字是清晰和有意义的。

**通用的监听者接口****Handler<T>****怎么样？**

有些人已经建议过用泛型定义一个通用的监听者接口Handler<T>。这有点牵扯到Java类型擦除的问题，假设我们有如下这个接口：

因为类型擦除，Java禁止一个类使用不同的类型参数多次实现同一个泛型接口（即不可能出现MultiHandler implements Handler<Type1>, Handler<Type2>）。这比起传统的Java事件机制也是巨大的退步，至少传统的Java Swing监听者接口使用了不同的方法把不同的事件区分开。

**EventBus****不是破坏了静态类型，排斥了自动重构支持吗？**

有些人被EventBus的register(Object) 和post(Object)方法直接使用Object做参数吓坏了。

这里使用Object参数有一个很好的理由：EventBus对事件监听者类型和事件本身的类型都不作任何限制。

另一方面，处理方法必须要明确地声明参数类型——期望的事件类型（或事件的父类型）。因此，搜索一个事件的类型引用，可以马上找到针对该事件的处理方法，对事件类型的重命名也会在IDE中自动更新所有的处理方法。

在EventBus的架构下，你可以任意重命名@Subscribe注解的处理方法，并且这类重命名不会被传播（即不会引起其他类的修改），因为对EventBus来说，处理方法的名字是无关紧要的。如果测试代码中直接调用了处理方法，那么当然，重命名处理方法会引起测试代码的变动，但使用EventBus触发处理方法的代码就不会发生变更。我们认为这是EventBus的特性，而不是漏洞：能够任意重命名处理方法，可以让你的处理方法命名更清晰。

**如果我注册了一个没有任何处理方法的监听者，会发生什么？**

什么也不会发生。

EventBus旨在与容器和模块系统整合，Guice就是个典型的例子。在这种情况下，可以方便地让容器/工厂/运行环境传递任意创建好的对象给EventBus的register(Object)方法。

这样，任何容器/工厂/运行环境创建的对象都可以简便地通过暴露处理方法挂载到系统的事件模块。

**编译时能检测到****EventBus****的哪些问题？**

Java类型系统可以明白地检测到的任何问题。比如，为一个不存在的事件类型定义处理方法。

**运行时往****EventBus****注册监听者，可以立即检测到哪些问题？**

一旦调用了register(Object) 方法，EventBus就会检查监听者中的处理方法是否结构正确的[well-formedness]。具体来说，就是每个用@Subscribe注解的方法都只能有一个参数。

违反这条规则将引起IllegalArgumentException（这条规则检测也可以用APT在编译时完成，不过我们还在研究中）。

**哪些问题只能在之后事件传播的运行时才会被检测到？**

如果组件传播了一个事件，但找不到相应的处理方法，EventBus*可能*会指出一个错误（通常是指出@Subscribe注解的缺失，或没有加载监听者组件）。

*请注意这个指示并不一定表示应用有问题。一个应用中可能有好多场景会故意忽略某个事件，尤其当事件来源于不可控代码时*

你可以注册一个处理方法专门处理DeadEvent类型的事件。每当EventBus收到没有对应处理方法的事件，它都会将其转化为DeadEvent，并且传递给你注册的DeadEvent处理方法——你可以选择记录或修复该事件。

**怎么测试监听者和它们的处理方法？**

因为监听者的处理方法都是普通方法，你可以简便地在测试代码中模拟EventBus调用这些方法。

**为什么我不能在****EventBus****上使用****<**泛型**魔法****>****？**

EventBus旨在很好地处理一大类用例。我们更喜欢针对大多数用例直击要害，而不是在所有用例上都保持体面。

此外，泛型也让EventBus的可扩展性——让它有益、高效地扩展，同时我们对EventBus的增补不会和你们的扩展相冲突——成为一个非常棘手的问题。

如果你真的很想用泛型，EventBus目前还不能提供，你可以提交一个问题并且设计自己的替代方案。

