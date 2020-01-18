# Immutable class

An [immutable class](https://en.wikipedia.org/wiki/Immutable_object) is one whose state can not be changed once created. There are certain guidelines to **create a class immutable in Java**.

## 1. Rules to create immutable classes

Java documentation itself has some guidelines identified to [write immutable classes](https://docs.oracle.com/javase/tutorial/essential/concurrency/imstrat.html) in this link. We will understand what these guidelines mean actually by creating an immutable class with mutable object with `Date` field.

#### Don’t provide “setter” methods — methods that modify fields or objects referred to by fields.

This principle says that for all mutable properties in your class, do not provide setter methods. Setter methods are meant to change the state of an object and this is what we want to prevent here.

#### Make all fields final and private

This is another way to increase *immutability*. Fields declared **private** will not be accessible outside the class and making them final will ensure the even accidentally you can not change them.

#### Don’t allow subclasses to override methods

The simplest way to do this is to declare the class as ***final\***. Final classes in java can not be overridden.

#### Special attention when having mutable instance variables

Always remember that your instance variables will be either **mutable** or **immutable**. Identify them and return new objects with copied content for all mutable objects. Immutable variables can be returned safely without extra effort.

> 始终记住，您的实例变量要么是**可变的**，要么是**不可变的**。标识它们并为所有可变对象返回带有复制内容的新对象。不需要额外的努力就可以安全地返回不可变变量。

A more sophisticated approach is to make the constructor ***private\*** and construct instances in **factory methods**.

## 2. Java immutable class example

Lets apply all above rules for immutable classes and make a concrete class implementation for **immutable class in Java**.

```java
import java.util.Date;
 
/**
* Always remember that your instance variables will be either mutable or immutable.
* Identify them and return new objects with copied content for all mutable objects.
* Immutable variables can be returned safely without extra effort.
* */
public final class ImmutableClass
{
 
    /**
    * Integer class is immutable as it does not provide any setter to change its content
    * */
    private final Integer immutableField1;
 
    /**
    * String class is immutable as it also does not provide setter to change its content
    * */
    private final String immutableField2;
 
    /**
    * Date class is mutable as it provide setters to change various date/time parts
    * */
    private final Date mutableField;
 
    //Default private constructor will ensure no unplanned construction of class
    private ImmutableClass(Integer fld1, String fld2, Date date)
    {
        this.immutableField1 = fld1;
        this.immutableField2 = fld2;
        this.mutableField = new Date(date.getTime());
    }
 
    //Factory method to store object creation logic in single place
    public static ImmutableClass createNewInstance(Integer fld1, String fld2, Date date)
    {
        return new ImmutableClass(fld1, fld2, date);
    }
 
    //Provide no setter methods
 
    /**
    * Integer class is immutable so we can return the instance variable as it is
    * */
    public Integer getImmutableField1() {
        return immutableField1;
    }
 
    /**
    * String class is also immutable so we can return the instance variable as it is
    * */
    public String getImmutableField2() {
        return immutableField2;
    }
 
    /**
    * Date class is mutable so we need a little care here.
    * We should not return the reference of original instance variable.
    * Instead a new Date object, with content copied to it, should be returned.
    * */
    public Date getMutableField() {
        return new Date(mutableField.getTime());
    }
 
    @Override
    public String toString() {
        return immutableField1 +" - "+ immutableField2 +" - "+ mutableField;
    }
}
```

```java
class TestMain
{
    public static void main(String[] args)
    {
        ImmutableClass im = ImmutableClass.createNewInstance(100,"test", new Date());
        System.out.println(im);
        tryModification(im.getImmutableField1(),im.getImmutableField2(),im.getMutableField());
        System.out.println(im);
    }
 
    private static void tryModification(Integer immutableField1, String immutableField2, Date mutableField)
    {
        immutableField1 = 10000;
        immutableField2 = "test changed";
        mutableField.setDate(10);
    }
}
```

As it can be seen that even changing the instance variables using their references does not change their value, so the class is immutable.

#### Immutable classes in JDK

Apart from your written classes, JDK itself has lots of immutable classes. Given is such a list of immutable classes in Java.

1. `String`
2. Wrapper classes such as `Integer`, `Long`, `Double` etc.
3. Immutable collection classes such as `Collections.singletonMap()` etc.
4. `java.lang.StackTraceElement`
5. Java enums (ideally they should be)
6. `java.util.Locale`
7. `java.util.UUID`

## 3. Benefits of making a class immutable

Lets first identify **advantages of immutable class**. In Java, immutable classes are:

1. are simple to construct, test, and use
2. are automatically thread-safe and have no synchronization issues
3. do not need a copy constructor
4. do not need an implementation of clone
5. allow [`hashCode()`](https://howtodoinjava.com/java/related-concepts/working-with-hashcode-and-equals-methods-in-java/) to use lazy initialization, and to cache its return value
6. do not need to be copied defensively when used as a field
7. make good [Map keys and Set elements](https://howtodoinjava.com/java/collections/how-hashmap-works-in-java/) (these objects must not change state while in the collection)
8. have their class invariant established once upon construction, and it never needs to be checked again
9. always have “**failure atomicity**” (a term used by Joshua Bloch) : if an immutable object throws an exception, it’s never left in an undesirable or indeterminate state

## 不可变类好处

- 容易构造,测试和使用
- 自动线程安全，没有同步问题
- 不需要复制构造函数
- 不需要实现克隆
- 允许[' hashCode() '](https://howtodoinjava.com/java/relatconcepts/working - hashCode -and-equals-methods-in-java/)使用延迟初始化，并缓存其返回值
- 当用作字段时，不需要防御性地复制
-  [Map keys and Set elements](https://howtodoinjava.com/java/collections/how-hashmap-works-in-java/) (这些类必须在 collections 中不能被修改状态)
- 它们的类不变量是否在构建时就已经建立，并且永远不需要再次检查
- 始终保持“**失败原子性**”(Joshua Bloch使用的术语):如果一个不可变对象抛出一个异常，那么它永远不会处于不希望的或不确定的状态