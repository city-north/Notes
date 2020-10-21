# Java自省机制简介

https://www.cs.auckland.ac.nz/references/java/java1.5/tutorial/javabeans/introspection/index.html

https://xiaomi-info.github.io/2020/03/16/java-beans-introspection/

## 目录

- 什么是Java自省

> *Introspection* is the automatic process of analyzing a bean's design patterns to reveal the bean's properties, events, and methods. This process controls the publishing and discovery of bean operations and properties

自省是自动分析一个bean的流程的设计模式, 用来发现 bean 的属性, 事件, 方法 , 这个过程控制了 publish 和 decovery 一个bean的操作和属性

在计算机科学中，内省是指计算机程序在运行时（Run time）检查对象（Object）类型的一种能力，通常也可以称作运行时类型检查。

### 自省的目的





> A growing number of Java object repository sites exist on the Internet in answer to the demand for centralized deployment of applets, classes, and source code in general. Any developer who has spent time hunting through these sites for licensable Java code to incorporate into a program has undoubtedly struggled with issues of how to quickly and cleanly integrate code from one particular source into an application.
>
> The way in which introspection is implemented provides great advantages, including:
>
> 1. *Portability* - Everything is done in the Java platform, so you can write components once, reuse them everywhere. There are no extra specification files that need to be maintained independently from your component code. There are no platform-specific issues to contend with. Your component is not tied to one component model or one proprietary platform. You get all the advantages of the evolving Java APIs, while maintaining the portability of your components.
> 2. *Reuse* - By following the JavaBeans design conventions, implementing the appropriate interfaces, and extending the appropriate classes, you provide your component with reuse potential that possibly exceeds your expectations.

### Introspection API

> The JavaBeans API architecture supplies a set of classes and interfaces to provide introspection.
>
> The [`BeanInfo` ](http://java.sun.com/javase/6/docs/api/java/beans/BeanInfo.html)(in the API reference documentation) interface of the `java.beans` package defines a set of methods that allow bean implementors to provide explicit information about their beans. By specifying BeanInfo for a bean component, a developer can hide methods, specify an icon for the toolbox, provide descriptive names for properties, define which properties are bound properties, and much more.
>
> The [`getBeanInfo(beanName)` ](http://java.sun.com/javase/6/docs/api/java/beans/Introspector.html#getBeanInfo(java.lang.Class))(in the API reference documentation) of the [`Introspector` ](http://java.sun.com/javase/6/docs/api/java/beans/Introspector.html)(in the API reference documentation) class can be used by builder tools and other automated environments to provide detailed information about a bean. The `getBeanInfo` method relies on the naming conventions for the bean's properties, events, and methods. A call to `getBeanInfo` results in the introspection process analyzing the bean�s classes and superclasses.
>
> The `Introspector` class provides descriptor classes with information about properties, events, and methods of a bean. Methods of this class locate any descriptor information that has been explicitly supplied by the developer through `BeanInfo` classes. Then the `Introspector` class applies the naming conventions to determine what properties the bean has, the events to which it can listen, and those which it can send.
>
> The following figure represents a hierarchy of the `FeatureDescriptor` classes:

![image-20201021130040895](../../assets/image-20201021130040895.png)

> Each class represented in this group describes a particular attribute of the bean. For example, the `isBound` method of the [`PropertyDescriptor` ](http://java.sun.com/javase/6/docs/api/java/beans/PropertyDescriptor.html)class indicates whether a `PropertyChangeEvent` event is fired when the value of this property changes.

### Editing Bean Info with the NetBeans BeanInfo Editor

> To open the BeanInfo dialog box, expand the appropriate class hierarchy to the bean Patterns node. Right-click the bean Patterns node and choose BeanInfo Editor from the pop-up menu. All elements of the selected class that match bean-naming conventions will be displayed at the left in the BeanInfo Editor dialog box as shown in the following figure:

![image-20201021130138942](../../assets/image-20201021130138942.png)

> Select one of the following nodes to view and edit its properties at the right of the dialog box:
>
> - BeanInfo
> - Bean
> - Properties
> - Methods
> - Event Sources
>
> Special symbols (green and red) appear next to the subnode to indicate whether an element will be included or excluded from the `BeanInfo` class.
>
> If the Get From Introspection option is not selected, the node's subnodes are available for inclusion in the `BeanInfo` class. To include all subnodes, right-click a node and choose Include All. You can also include each element individually by selecting its subnode and setting the Include in BeanInfo property. If the Get From Introspection option is selected, the setting the properties of subnodes has no effect in the generated `BeanInfo` code.
>
> The following attributes are available for the nodes for each bean, property, event sources, and method:
>
> - Name - A name of the selected element as it appears in code.
> - Preferred - An attribute to specify where this property appears in the Inspector window under the Properties node.
> - Expert - An attribute to specify where this property appears in the Inspector window under the Other Properties node.
> - Hidden - An attribute to mark an element for tool use only.
> - Display Name Code - A display name of the property.
> - Short Description Code - A short description of the property.
> - Include in BeanInfo - An attribute to include the selected element in the `BeanInfo` class.
> - Bound - An attribute to make the bean property bound.
> - Constrained - An attribute to make the bean property constrained.
> - Mode - An attribute to set the property's mode and generate getter and setter methods.
> - Property Editor Class - An attribute to specify a custom class to act as a property editor for the property.
>
> For Event Source nodes, the following Expert properties are available:
>
> - Unicast (read-only)
> - In Default Event Set

### Introspection Sample

> The following example represents code to perform introspection:
>
> ```
> import java.beans.BeanInfo;
> import java.beans.Introspector;
> import java.beans.IntrospectionException;
> import java.beans.PropertyDescriptor;
> 
> public class SimpleBean
> {
>     private final String name = "SimpleBean";
>     private int size;
> 
>     public String getName()
>     {
>         return this.name;
>     }
> 
>     public int getSize()
>     {
>         return this.size;
>     }
> 
>     public void setSize( int size )
>     {
>         this.size = size;
>     }
> 
>     public static void main( String[] args )
>             throws IntrospectionException
>     {
>         BeanInfo info = Introspector.getBeanInfo( SimpleBean.class );
>         for ( PropertyDescriptor pd : info.getPropertyDescriptors() )
>             System.out.println( pd.getName() );
>     }
> }
> ```
>
> This example creates a non-visual bean and displays the following properties derived from the `BeanInfo` object:
>
> - `class`
> - `name`
> - `size`
>
> Note that a `class` property was not defined in the `SimpleBean` class. This property was inherited from the `Object` class. To get properties defined only in the `SimpleBean` class, use the following form of the `getBeanInfo` method:
>
> ```
> 	Introspector.getBeanInfo( SimpleBean.class, Object.class );
> ```