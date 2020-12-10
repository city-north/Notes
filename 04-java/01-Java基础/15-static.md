# Static 

- static variable
- static method 
- static block 
- static Class
- static import statement

# Static Variable

To declare a variable static , use `static` in variable declaration . static variable syntax is 

```
ACCESS_MODIFER static DATA_TYPE VARNAME;

```

for example , a public static variable of `Inteager` type is decleard in this way

```java
public static Integer staticVar;
```

The most important thing about static variables is they belong to class level .What it means is that there can be only one copy of variable in runtime;



> Static 变量是类级别的,这意味着这个变量在运行时只能有一个拷贝

When you define a static variable in class definition , each instance of Class will have access to that single copy , Separate instances of class will not have their own local copy , like they have for non-static variables .

> 当你在一个类定义里声明一个静态变量时,这个类的所有实例都共享这个静态变量.每个实例自己不拥有一个本地拷贝



```java
/**
 * Example of Static identifier in Java
 *
 * @author EricChen 2020/01/18 15:35
 */
public class JavaStaticExample {

    public static void main(String[] args) {
        DataObject objOne = new DataObject();
        objOne.staticVar = 10;
        objOne.nonStaticVar = 20;

        DataObject objTwo = new DataObject();

        System.out.println(objTwo.staticVar);       //10
        System.out.println(objTwo.nonStaticVar);    //null

        DataObject.staticVar = 30;  //Direct Access

        System.out.println(objOne.staticVar);       //30
        System.out.println(objTwo.staticVar);       //30
    }
}
class DataObject {
    public static Integer staticVar;
    public Integer nonStaticVar;
}
```

Notice how we changed the value to 30 , and both objects (objOne and objTwo) now see the updated value which is 30;

We don't need to create any instance to acces `static` variables , It clearly shows that static valuables belong to class scope

- Static 作用域是 Class ,我们不需要创建任何实例来访问他,直接用类名访问
- 如果有数据需要被共享给所有类的实例时,我们就用 static 

#### 生命周期

- 静态变量:随着类的加载而存在,随着类的消失而消失
- 非静态变量:随着对象创建而创建,随着被垃圾回收期回收而消失

## Static Method

To declare a static method, use `static` keyword in method declaration. Static method syntax is:

```java
ACCESS_MODIFER static RETURN_TYPE METHOD_NAME;
```

For Example , a public static variable of `Inteager` type is decleard in this way 

```java
public static Integer staticVar;
 
public static Integer getStaticVar(){
 
    return staticVar;
}
```

Few things to remember.

1. You can *access only static variables inside static methods*. If you try to access any non-static variable, the compiler error will be generated with message “*Cannot make a static reference to the non-static field nonStaticVar*“.
2. Static methods can be accessed via it’s class reference, and there is no need to create an instance of class. Though you can access using instance reference as well but it will have not any difference in comparison to access via class reference.
3. Static methods also belong to class level scope. 

> - 静态函数可以直接访问静态变量,但是不能直接访问普通变量
> - 非静态函数可以直接访问静态变量和普通变量

## Static Import Statement

The normal import declaration imports classes from packages, so that they can be used without package reference. Similarly the **static import** declaration *imports static members from classes* and allowing them to be used without class reference.

A static import statement also comes in two flavors: **single-static import** and **static-import-on-demand**. A single-static import declaration imports one static member from a type. A static-import-on-demand declaration imports all static members of a type.

```java
//Single-static-import declaration:
  
import static <<package name>>.<<type name>>.<<static member name>>;
  
//Static-import-on-demand declaration:
  
import static <<package name>>.<<type name>>.*;
```

For example, `System.out` is

```java
//Static import statement
import static java.lang.System.out;
 
public class JavaStaticExample 
{
    public static void main(String[] args) 
    {
        DataObject.staticVar = 30;  
 
        out.println(DataObject.staticVar);  //Static import statement example
    }
}
class DataObject 
{
    public static Integer staticVar;    //static variable
}
 
Output:
 
30
```

## 4. Static Block

Static blocks are portion of [class initialization](https://howtodoinjava.com/java/basics/how-to-create-a-class-in-java/) code, which are wrapped with `static` keyword.

```java
public class Main {
      
    //static initializer
    static {
        System.out.println("Inside static initializer");
    }   
}
```

Static blocks are executed when the class is loaded in the memory. A class can have multiple static blocks and these will be executed in the same sequence in which they appear in class definition.

```java
import static java.lang.System.out;
 
class DataObject 
{
    public Integer nonStaticVar;
    public static Integer staticVar;    //static variable
     
    //It will be executed first
    static {
        staticVar = 40;
        //nonStaticVar = 20;    //Not possible to access non-static members
    }
     
    //It will be executed second
    static {
        out.println(staticVar);
    }
}
 
Output:
 
40
```

## 5. Static Class

In Java, you can have a **static class** as **inner class**. Just like other static members, nested classed belong with class scope so the inner static class can be accessed without having an object of outer class.

```java
public class JavaStaticExample 
{
    public static void main(String[] args) 
    {
        //Static inner class example
        System.out.println( DataObject.StaticInnerClas.innerStaticVar );
    }
}
class DataObject 
{
    public Integer nonStaticVar;
    public static Integer staticVar;    //static variable
     
    static class StaticInnerClas {
        Integer innerNonStaticVar = 60; 
        static Integer innerStaticVar = 70;     //static variable inside inner class
    }
}
```

Please note that an *static inner class* cannot access the non-static members of outer class. It can access only static members from outer class.

> 静态内部类只能访问外部类的的静态成员

```java
public class JavaStaticExample 
{
    public static void main(String[] args) 
    {
        //Static inner class example
        DataObject.StaticInnerClas.accessOuterClass();
    }
}
class DataObject 
{
    public Integer nonStaticVar;
    public static Integer staticVar;    //static variable
         
    static {
        staticVar = 40;
        //nonStaticVar = 20;    //Not possible to access non-static members
    }
 
    public static Integer getStaticVar(){
        return staticVar;
    }
     
    static class StaticInnerClas 
    {   
        public static void accessOuterClass()
        {
            System.out.println(DataObject.staticVar);       //static variable of outer class
            System.out.println(DataObject.getStaticVar());  //static method of outer class
        }
    }
}
 
Output:
 
40
```

## 6. Summary

Let’s summarize everything about `static keyword` usage in Java.

1. Static members belong to class. No need to create class instance to access static members.
2. Static members (variables and methods) can be accessed inside static methods and static blocks only.
3. Non-static members cannot be accessed inside static methods, blocks and inner classes.
4. A class can have multiple static blocks and they will be executed in order they appear in class definition.
5. A class can be static only if its declared as inner class inside outer class.
6. Static imports can be used to import all static members from a class. These members can be referred without any class reference.

> - 静态成员属于类,你不需要创建一个类的实例就可以访问静态成员
> - 静态成员(静态方法和静态变量) 只能通过静态方法和静态代码块访问
> - 静态方法和静态代码块,和静态内部类不能访问非静态变量
> - 一个类可以拥有多个静态代码块,他们执行的顺序是声明顺序
> - 一个类如果声明成 static, 那么肯定是一个内部类
> - 通过 static imports  导入静态变量或者静态方法,你就不需要使用类名来调用