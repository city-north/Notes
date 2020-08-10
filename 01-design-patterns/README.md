# 设计模式笔记

设计模式学习笔记

> 参考文章: [什么是设计模式](https://refactoringguru.cn/design-patterns/what-is-pattern)

## 什么是设计模式

**设计模式**是软件设计中常见问题的典型解决方案。 它们就像能根据需求进行调整的预制蓝图， 可用于解决代码中反复出现的设计问题。同样的解决方案在各种项目中得到了反复使用， 所以最终有人给它们起了名字， 并对其进行了详细描述

设计模式与方法或库的使用方式不同， 你很难直接在自己的程序中套用某个设计模式。 模式并不是一段特定的代码， 而是解决特定问题的一般性概念。 你可以根据模式来实现符合自己程序实际所需的解决方案。

人们常常会混淆模式和算法， 因为两者在概念上都是已知特定问题的典型解决方案。 但算法总是明确定义达成特定目标所需的一系列步骤， 而模式则是对解决方案的更高层次描述。 同一模式在两个不同程序中的实现代码可能会不一样。

算法更像是菜谱： 提供达成目标的明确步骤。 而模式更像是蓝图： 你可以看到最终的结果和模式的功能， 但需要自己确定实现步骤。

## 模式包含哪些内容？

大部分模式都有正规的描述方式， 以便在不同情况下使用。 模式的描述通常会包括以下部分：

- **意图**部分简单描述问题和解决方案。
- **动机**部分将进一步解释问题并说明模式会如何提供解决方案。
- **结构**部分展示模式的每个部分和它们之间的关系。
- **在不同语言中的实现**提供流行编程语言的代码， 让读者更好地理解模式背后的思想。

部分模式介绍中还列出其他的一些实用细节， 例如模式的适用性、 实现步骤以及与其他模式的关系。

## 模式的分类

不同设计模式在其复杂程度、 细节层次以及在整个系统中的应用范围等方面各不相同。 我喜欢将其类比于道路的建造： 如果你希望让十字路口更加安全， 那么可以安装一些交通信号灯， 或者修建包含行人地下通道在内的多层互通式立交桥。

最基础的、 底层的模式通常被称为*惯用技巧* (*idioms*)。 这类模式一般只能在一种编程语言中使用。

最通用的、 高层的模式是*构架模式*。 开发者可以在任何编程语言中使用这类模式。 与其他模式不同， 它们可用于整个应用程序的架构设计。

此外， 所有模式可以根据其*意图*或目的来分类。 本书覆盖了三种主要的模式类别：

- **创建型模式**提供创建对象的机制， 增加已有代码的灵活性和可复用性。
- **结构型模式**介绍如何将对象和类组装成较大的结构， 并同时保持结构的灵活和高效。
- **行为模式**负责对象间的高效沟通和职责委派。

## 目录

### [设计模式设计原则](01-design-principles/README.md)

| 序号 | 设计模式                                                     |
| ---- | ------------------------------------------------------------ |
| 1    | [单一职责原则（Single Responsibility Principle,SRP）](01-design-principles/01-single-responsibility-principle.md) |
| 2    | [开闭原则(Open-Closed Principle,OPC)](01-design-principles/02-open-closed-principle.md) |
| 3    | [里氏代换原则 (Liskov Substitution Principle,LSP](01-design-principles/03-Liskov-substitution-principle.md) |
| 4    | [依赖倒转原则(Dependancy Inversion Principle,DIP)](01-design-principles/04-dependence-inversion-principle.md) |
| 5    | [接口隔离原则(Interface Segregation Principle,ISP)](01-design-principles/05-interface-segregation-principle.md) |
| 6    | [合成复用原则(Composite Reuse Principle,CRP)](01-design-principles/06-composite-reuse-principle.md) |
| 7    | [迪米特法则((Law of Demeter)](01-design-principles/07-law-of-demeter.md) |

### [创建型模式（Creational Patterns）](02-creational-patterns/README.md)

| 设计模式                                                     | 简介               | 举例                        |
| ------------------------------------------------------------ | ------------------ | --------------------------- |
| [ 简单工厂模式(Simple Factory Pattern)](02-creational-patterns/01-simple-factory-pattern.md) | 封装创建过程       | BeanFactory/ Calender       |
| [工厂方法模式(Factory Method Pattern)](02-creational-patterns/02-factory-method-pattern.md) | 封装创建过程       | BeanFactory/ Calender       |
| [抽象工厂模式(Abastact Factory Pattern)](02-creational-patterns/03-abstract-factory-pattern.md) | 封装创建过程       | BeanFactory/ Calender       |
| [单例模式(Singleton Pattern)](02-creational-patterns/04-singleton-pattern.md) | 保证独一无二       | ApplicationContext/Calender |
| [建造者模式(Builder Pattern)](02-creational-patterns/05-builder-pattern.md) | 多构造参数解决方案 |                             |
| [原型模式(Prototype Pattern)](02-creational-patterns/06-prototype-pattern.md) | 以一变百           | ArrayList/PrototypeBean     |

### [结构型模式（Structural Patterns）](03-structural-patterns/README.md)

| 设计模式                                                     | 简介              | 举例                                              |
| ------------------------------------------------------------ | ----------------- | ------------------------------------------------- |
| [适配器模式（Adapter Pattern）](03-structural-patterns/01-adapter-pattern.md) | 兼容转接头        | AdvisorAdaptor/HandlerAdaptor                     |
| [桥接模式（Bridge Pattern）](03-structural-patterns/02-bridge-pattern.md) |                   |                                                   |
| [过滤器模式（ Filter /Criteria Pattern）](03-structural-patterns/08-filter-pattern.md) |                   |                                                   |
| [组合模式（Composite Pattern）](03-structural-patterns/03-composite-pattern.md) |                   |                                                   |
| [装饰器模式（Decorator Pattern）](03-structural-patterns/04-decorator-pattern.md) |                   |                                                   |
| [外观模式（Facade Pattern）](03-structural-patterns/05-facade-pattern.md) |                   |                                                   |
| [享元模式（Flyweight Pattern）](03-structural-patterns/06-flyweight-pattern.md) |                   |                                                   |
| [代理模式（Proxy Pattern）](03-structural-patterns/07-proxy-pattern.md) | 找人办事,增强职责 | ProxyFactoryBean/JDKDynamicAopProxy/CglibAopProxy |

### [行为型模式（Behavioral Patterns）](04-behavioral-patterns/README.md)

| 设计模式                                                     | 简介                      | 举例                                           |
| ------------------------------------------------------------ | ------------------------- | ---------------------------------------------- |
| [责任链模式（Chain of Respinsibility Pattern）](04-behavioral-patterns/01-chain-of-responsibility-pattern.md) |                           |                                                |
| [命令模式（Command Pattern）](04-behavioral-patterns/02-command-pattern.md) |                           |                                                |
| [解释器模式（Interpreter Pattern）](04-behavioral-patterns/03-interpreter-pattern.md) |                           |                                                |
| [迭代器模式（Iterator Pattern）](04-behavioral-patterns/04-iterator-pattern.md) |                           |                                                |
| [中介者模式（Mediator Pattern）](04-behavioral-patterns/05-mediator-pattern.md) |                           |                                                |
| [备忘录模式（Memento Pattern）](04-behavioral-patterns/06-memento-pattern.md) |                           |                                                |
| [观察者模式（Observer Pattern）](04-behavioral-patterns/07-observer-pattern.md) | 任务完成通知              | ContextLoaderListener                          |
| [状态模式（State Pattern）](04-behavioral-patterns/08-state-pattern.md) |                           |                                                |
| [空对象模式（Null Object Pattern）](04-behavioral-patterns/12-null-object-pattern.md) |                           |                                                |
| [策略模式（Strategy Pattern）](04-behavioral-patterns/09-strategy-pattern.md) | 策略可替换                | InstantiationStrategy                          |
| [模板方法模式（Template Method Pattern）](04-behavioral-patterns/10-template-method-pattern.md) | 流程标准化,自己实现定制   | JdbcTempalte/HttpServlet                       |
| [访问者模式（Vistor Pattern）](04-behavioral-patterns/11-vistor-pattern.md) |                           |                                                |
| [委派模式(Delegate Pattern)](04-behavioral-patterns/13-delegate-pattern.md) | leader 委派 worker,分配活 | DispatcherServlet/BeanDefinitionParserDelegate |

