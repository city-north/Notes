# Java create Class 

##  Class vs Object

In Java, **objects** are containers like data structure which have **state and behavior**. Objects represent the actors in the system or the application. For example, in an HR application, actors are employee, manager, department or reports. An object is an instance of a class.

The **classes** are the template that describes the state and behavior of its objects.

> - 对象是有状态和行为的数据结构的容器
> - class 是描述对应对象的状态和行为的模板

## How to decleare a Class 

The general syntax for declearing a class is 

```java
<<modifiers>> class <<class name>>{
  // fields and members of the class
}
```

- a class decleration may have zero or more modifiers  类声明可以有零个或者多个修饰符
- The keywords `class`  is used to declear a class  关键字 class 用以声明一个类
- The `class name` is a user-defined name of the class , which should be a valid identifier `class name`是一个用户定义的类名,应该是一个有效的标识符
- Each class has a body , which is specified inside a pair of braces `{}` 每个类都有一个
- The body of a class contains its different components , for example , fields , methods .etc

## Ingredients of Java Class 

In Java, classes are used as a templates to create objects . A class in Java may consist of five primary components :

- Fields 字段
- Methods 方法
- Constructors 构造器
- Static initializers 静态代码块
- Instance initializers  构造代码块

Fields and methods are also known as class members .Constructors and both initializers are used to during initialization of class i.e creating objects using class template .

> 字段和方法都是类成员,构造器和代码块在构造一个类的对象时使用

Constructors are used to create objects of a class . We must have at least one constructors for a class(if we do not decleare explicitly then JVM inject default constructor for us )

> 构造器用来创建一个类的对象,每个类至少要拥有一个构造器, 如果我们没有显式地声明一个构造器的话,JVM 会给我们默认注入一个

initializers are used to initialize fields of a class , we can have zero or more initializers of static or instance types 

构造代码块用于初始化一个 class 的 属性,我们可以0 个或者多个静态代码块或者实例类型

## Fields 

Fields of a class represent properties (also called state attribute) of objects of that class . That fields are decleard inside the body of the class.

> 字段代表着一个类的属性,当然也可以叫做状态属性.这些字段在类体内声明

```java
public class Main 
 
        // A field declaration
        <<modifiers>> <<data type>> <<field name>> = <<initial value>>;
}
```

Suppose every object of ‘Human’ class has two properties: a name and a gender. The human class should include declarations of two fields: one to represent name and one to represent gender.

```java
public class Human {
 
        String name;
        String gender;
}
```

Here the Human class declares two fields: name and gender. Both fields are of the `String` type. Every instance (or object) of the Human class will have a copy of these two fields.

## 3.2. Methods

A Java method is a collection of statements that are grouped together to perform an operation. Methods are generally used to modify the state of class fields. Methods also can be used to delegate the tasks by calling methods in other objects.

In Java, methods may –

- accept zero or more arguments
- return void or a single value
- be overloaded – means we can define more than one method with same name but different syntax
- be overrided – means we can define methods with same syntax in parent and child classes

```java
public class Human {
 
        String name;
        String gender;
 
        public void eat() {
 
            System.out.println("I am eating");
        }
}
```

## 3.3. Constructors

A constructor is a named block of code that is used to initialize an object of a class immediately after the object is created. The general syntax for a constructor declaration is:

> 构造器用于在类的实例被创建后立刻进行初始化的代码

```java
<<Modifiers>> <<Constructor Name>>(<<parameters list>>) throws <<Exceptions list>> {
 
        // Body of constructor goes here
}
```

- A constructor can have its access modifier as public, private, protected, or package-level (no modifier).

- The constructor name is the same as the simple name of the class.
- The constructor name is followed by a pair of opening and closing parentheses, which may include parameters.
- Optionally, the closing parenthesis may be followed by the keyword throws, which in turn is followed by a comma-separated list of exceptions.
- Unlike a method, a constructor does not have a return type.
- We cannot even specify void as a return type for a constructor. If there is any return type then it is method.
- Remember that if the name of a construct is the same as the simple name of the class, it could be a method or a constructor. If it specifies a return type, it is a method. If it does not specify a return type, it is a constructor.

## 3.4. Instance Initialization Block

We saw that a constructor is used to initialize an instance of a class. An instance initialization block, also called **instance initializer**, is also used to initialize objects of a class.

An instance initializer is simply a block of code inside the body of a class, but outside of any methods or constructors. An instance initializer does not have a name. Its code is simply placed inside an opening brace and a closing brace.

Note that an instance initializer is executed in instance context and the keyword `this` is available inside the instance initializer.

```java
public class Main 
{
    {
        //instance initializer block
    }
}
```

- we can have multiple instance initializers for a class.
- All initializers are executed automatically in textual order for every object we create.
- Code for all **instance initializers are executed before any constructor**.
- An **instance initializer cannot have a return statement**.
- It cannot throw checked exceptions unless all declared constructors list those checked exceptions in their throws clause.

```
public class Main {
     
    //instance initializer
    {
        System.out.println("Inside instance initializer");
    }
     
    //constructor
    public Main()       
    {
        System.out.println("Inside constructor");
    }
     
    public static void main(String[] args) {
        new Main();
    }
}
 
Output:
 
Inside instance initializer
Inside constructor
```

## 3.5. Static Initialization Block

- A [static](https://howtodoinjava.com/oops/java-static-keyword/) initialization block is also known as a **static initializer**.

> 静态初始化块又称静态初始化器

- It is similar to an instance initialization block except it is used to initialize a class.

> 它类似于实例初始化块，只是用于初始化类。

- An instance initializer is executed once per object whereas a static initializer is executed only once for a class when the class definition is loaded into JVM.

> 每个对象执行一次实例初始化器，而类定义加载到JVM中时，静态初始化器只执行一次。

- To differentiate it from an instance initializer, we need to use the `static` keyword in the beginning of its declaration.

> 为了将它与实例初始化器区别开来，我们需要在它的声明开头使用' static '关键字。

- we can have multiple static initializers in a class.

> 一个类中可以有多个静态代码块

- All static initializers are executed in textual order in which they appear, and execute before any instance initializers.

> 所有静态初始化器都是按照它们出现的文本顺序执行的，并在任何实例初始化器之前执行。

A static initializer cannot throw checked exceptions. It cannot have a return statement.

```java
public class Main {
     
    //static initializer
    static {
        System.out.println("Inside static initializer");
    }
     
    //constructor
    public Main()       
    {
        System.out.println("Inside constructor");
    }
     
    public static void main(String[] args) {
        new Main();
    }
}
 
Output:
 
Inside static initializer
Inside constructor
```

## 4. How to create objects?

In Java, to create an object from a class, use `new` keyword along with one of it’s constructors.

```java
<<Class>> <<variable>> = new <<Call to Class Constructor>>;
 
//e.g.
 
Human human = new Human();
```

Remember, when we do not add a constructor to a class, the Java compiler adds one for us. The constructor that is added by the Java compiler is called a **default constructor**. The default constructor accepts no arguments. The name of the constructor of a class is the same as the class name.

The new operator is followed by a call to the constructor of the class whose instance is being created. The new operator creates an instance of a class by allocating the memory in heap.

## 5. The ‘null’ reference type

Java has a special reference type called `null` type. It has no name. Therefore, we cannot define a variable of the null reference type. The null reference type has only one value defined by Java, which is the **null literal**. It is simply null.

The null reference type is assignment compatible with any other reference type. That is, we can assign a null value to a variable of any reference type. Practically, a null value stored in a [reference type](https://howtodoinjava.com/java/basics/data-types-in-java/) variable means that the reference variable is referring to no object.

```java
// Assign null value to john
 
Human john = null;      // john is not referring to any object
john = new Human();     // Now, john is referring to a valid Human object
```

Note that null is a literal of null type. We cannot assign null to a [primitive type](https://howtodoinjava.com/java/basics/primitive-data-types-in-java/) variable, and that’s why Java compiler does not allow us to compare a primitive value to a null value.

That’s all for this very basic tutorial about creating classes in java.

