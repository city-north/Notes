# Java command line arguments

The program arguments passed at launching the Java program are called **command line arguments**.

A Java program can be launched either from [console](https://howtodoinjava.com/java/io/java-io-how-to-read-input-from-console/) or from an editor. To launch a program we have to use `"java className"` command from command prompt or system console. While launching the program we can pass additional arguments (no limit of numbers of arguments) in below syntax.

## 1. Command line arguments syntax

In given below syntax, we are passing 5 arguments to the Main class `MyClass`. MyClass has the `main()` method which accepts these arguments in form of an **[String](https://howtodoinjava.com/java-string/) array**.

```
$ java MyClass arg1 arg2 arg3 arg4 arg5
```

## 2. Java command line arguments example

Let’s create an example to understand how command line program arguments work in Java. This class simple accepts the arguments and print them in console.

As a programmer, we can use these arguments as startup parameters to customize the behavior of application in runtime.

```java
package app;
 
public class Main 
{
  public static void main(String[] args) 
  {
    for(int i = 0; i< args.length; i++) 
    {
      System.out.println( args[i] );
    }
  }
}
```

Now run this class from console.

```java

$ java Main 1 2 3 4
 
#prints
 
1
2
3
4
```

## 3. Summary

- **Command line args** can be used to specify configuration information while launching the application.
- There is no restriction on the maximum number of arguments. We can specify any number of arguments.
- Arguments are passed as strings.
- Passed arguments are retrieved as the string [array](https://howtodoinjava.com/java-array/) in the main() method argument.