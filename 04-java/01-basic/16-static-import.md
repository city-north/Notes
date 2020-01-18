# Static Import Declearations in Java

The normal import declaration imports classes from packages, so that they can be used without package reference. Similarly the static import declaration imports static members from classes and allowing them to be used without class reference.

A static import declaration also comes in two flavors: single-static import and static-import-on-demand. A single-static import declaration imports one static member from a type. A static-import-on-demand declaration imports all static members of a type. The general syntax of static import declaration is as follows:

```java
//Single-static-import declaration:
 
import static <<package name>>.<<type name>>.<<static member name>>;
 
//Static-import-on-demand declaration:
 
import static <<package name>>.<<type name>>.*;
```

## Static import example

For example, You remember printing messages in the standard output using the `System.out.println()` method. `System` is a class in **java.lang** package that has a static variable named `out`. When you use `System.out`, you are referring to that static variable out of the `System` class. You can use a static import declaration to import the `out` static variable from the `System` class as follows:

```
import static java.lang.System.out;
```

You code can now use the name **out** to mean `System.out` in your program. The compiler will use the static import declaration to resolve the name `out` to `System.out`.

```
public class StaticImportTest {
        public static void main(String[] args) {
                out.println("Hello static import!");
        }
}
```

## Static import rules

The following are some important rules about static import declaration.

1) If two static members with the same simple name are imported, one using single-static import declaration and other using static-import-on-demand declaration, the one imported using single-static import declaration takes precedence.

> 1）如果导入两个具有相同简单名称的静态成员，一个使用单静态导入声明，另一个使用静态导入按需声明，则使用单静态导入声明导入的静态成员优先。

Suppose there are two classes, `package1.Class1` and `package2.Class2`. Both classes have a static method called `methodA`. The following code will use `package1.Class1.methodA()` method because it is imported using the single-static import declaration:

> 假设有两个类，`package1.Class1`和`package2.Class2`。这两个类都有一个名为的静态方法`methodA`。以下代码将使用`package1.Class1.methodA()`方法，因为它是使用单静态导入声明导入的：

```java
import static package1.Class1.methodA; // Imports Class1.methodA() method
import static package2.Class2.*;  // Imports Class2.methodA() method too
  
public class Test {
        public static void main(String[] args) {
                methodA();   // Class1.methodA() will be called
        }
}
```

2) Using single-static-import declaration to import two static members with the same simple name is not allowed. The following static import declarations generate an error because both of them import the static member with the same simple name of `methodA`:

```
import static package1.Class1.methodA;
import static package1.Class2.methodA; // An error
```

3) If a static member is imported using a single-static import declaration and there exists a static member in the same class with the same name, the static member in the class is used.

```java
// A.java
package package1;
  
public class A {
        public static void test() {
                System.out.println("package1.A.test()");
        }
}
  
// Test.java
package package2;
  
import static package1.A.test;
  
public class Test {
        public static void main(String[] args) {
                test(); // Will use package2.Test.test() method, not package1.A.test() method
        }
  
        public static void test() {
                System.out.println("package2.Test.test()");
        }
}
 
Output:
 
package2.Test.test()
```

It may seem that static imports help you use simple names of static members to make the program simpler to write and read. Sometimes static imports may introduce subtle bugs in your program, which may be hard to debug. You are advised not use static imports at all, or only in very rare circumstances.