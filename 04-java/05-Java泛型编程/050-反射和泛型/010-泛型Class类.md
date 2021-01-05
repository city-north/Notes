# 010-泛型Class类

[TOC]

## 简介

反射允许你在运行时分析任意的对象。如果对象是泛型类的实例，关于泛型类型参数则得不到太多信息，因为它们会被擦除。在下面的小节中，可以了解利用反射可以获得泛型类的什么信息。

## 泛型Class类

**现在，Class类是泛型的。例如，String.class实际上是一个Class＜String＞类的对象(事实上，是唯一的对象)。**

类型参数十分有用，这是因为它允许Class＜T＞方法的返回类型更加具有针对性。



下面Class＜T＞中的方法就使用了类型参数：

```java
public final class Class<T> implements java.io.Serializable,
                              GenericDeclaration,
                              Type,
                              AnnotatedElement {
                              
//返回无参数构造器构造的一个新实例。
T newInstance(Object ... initargs); 

//如果obj为null或有可能转换成类型T，则返回obj；否则抛出BadCastException异常。
public T cast(Object obj);  

//如果T是枚举类型，则返回所有值组成的数组，否则返回null。
public T[] getEnumConstants();                      

//返回这个类的超类。如果T不是一个类或Object类，则返回null。
native Class<? super T> getSuperclass();

//获得公有的构造器，或带有给定参数类型的构造器。
public Constructor<T> getConstructor(Class<?>... parameterTypes);

public Constructor<T> getDeclaredConstructor(Class<?>... parameterTypes)
}
```

- newInstance方法返回一个实例，这个实例所属的类由默认的构造器获得。它的返回类型目前被声明为T，其类型与Class＜T＞描述的类相同，这样就免除了类型转换。

- 如果给定的类型确实是T的一个子类型，cast方法就会返回一个现在声明为类型T的对象，否则，抛出一个BadCastException异常。

- getEnumConstants- 如果这个类不是enum类或类型T的枚举值的数组，getEnumConstants方法将返回null。

- getConstructor与getdeclaredConstructor方法返回一个Constructor＜T＞对象。Constructor类也已经变成泛型，以便newInstance方法有一个正确的返回类型。

  