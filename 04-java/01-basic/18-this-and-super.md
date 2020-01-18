# This and Super

`this` and **super** are reserved [keywords](https://howtodoinjava.com/java-keywords/) in Java. `this` refer to **current instance** of a class while `super` refer to the **parent class** of that class where `super` keyword is used.

## 1. Java this keyword

`this` keyword automatically holds the reference to current instance of a class. It is very useful in scenarios where we are inheriting a method from parent class into child class, and want to invoke method from child class specifically.

We can use this keyword to access [static](https://howtodoinjava.com/java/basics/java-static-keyword/) fields in the class as well, but recommended approach to access static fields using class reference e.g. **MyClass.STATIC_FIELD**.

- 

## 2. Java super keyword

Similar to `this` keyword, `super` also is a reserved keyword in Java. It always hold the reference to parent class of any given class.

Using `super` keyword, we can access the fields and methods of parent class in any child class.

- super 关键字标识对父类空间的引用
  - 指定访问父类的成员(当父类的成员与子类的成员相同时)
    - 创建子类对象时,默认会调用父类无参的构造函数,可以通过 super 指定调用父类的构造函数
- 如果子类的构造函数中没有指定调用父类的构造函数,那么 Java 编译器会在子类的构造方法上加上 super 语句
- super 关键字调用父类的构造函数时,该语句必须是子类构造函数的第一句
- super 关键字与 this 关键字不能同时出现在同一个构造函数中调用其他的构造函数,因为两个语句都必须产生第一句

## 3. Java this and super keyword example

In this example, we have two classes `ParentClass` and `ChildClass` where **ChildClass extends ParentClass**. I have created a method `showMyName()` in parent class and override it child class.

Now when we try to invoke `showMyName()` method inside child class using this and super keywords, it invokes the methods from current class and parent class, respectively.

```java
public class ParentClass 
{   
    public void showMyName() 
    {
        System.out.println("In ParentClass");
    }
}
```

```java
public class ChildClass extends ParentClass 
{
    public void showMyName() 
    {
        System.out.println("In ChildClass");
    }
     
    public void test() 
    {
        this.showMyName();
         
        super.showMyName();
    }
}
```

```java
public class Main 
{
    public static void main(String[] args) 
    {
        ChildClass childObj = new ChildClass();
         
        childObj.test();
    }
}
```

```java
In ChildClass
In ParentClass
```

