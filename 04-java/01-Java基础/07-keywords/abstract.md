# Java abstract keyword - abstract classes and methods 

Abstract keywords can be used with :

- classes 类
- methods 方法

we can not use it with variables . abstract is a non-access modifier which helps in achieving abstarction in object oriented designs 

> abstract 是一个非访问修饰符,有助于在面向对象的设计中实现抽象设计

# Java abstract class

Abstract classes cannot be instantiated due to their partial implementation,but they can be inherited just like a normal class.

When an abstract class is inherited , the subclass usually provides implementations for all of the abstract methods in its parents class . However , if it does not ,then the subclass must also be declared abstract .

```java
public abstract class DemoClass 
{
    //declare other methods and fields
}
```

## Java abstract method 

An abstract method is a method that is decleard without an implementation i.e. without curly braces ,and followed by a semincolon 

