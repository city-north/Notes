# Main 方法

## 1. Java main method syntax

Start with reminding the **syntax of main method in Java**.

```
public class Main 
{
    public static void main(String[] args) 
    {
        System.out.println("Hello World !!");
    }
}
```

## Why Java main method is public?

The big question and perhaps most difficult too. I tried hard to find a good reason for this question in all good learning material in my reach, but nothing proved enough. So, my analysis says (like many others): **main method is public so that it can be accessible everywhere and to every object which may desire to use it for launching the application**. Here, I am not saying that JDK/JRE had similar reasons because `java.exe` or `javaw.exe` (for windows) use **Java Native Interface** (JNI) calls to `invoke` method, so they can have invoked it either way irrespective of any access modifier.

> main 方法是 public 所以在任何地方的任何对象访问,也许会用于启动整个应用

A second way to answer this is another question, why not public? All methods and constructors in java have some access modifier. `main()` method also need one. There is no reason why it should not be `public`, and be any other modifier(default/protected/private).

Notice that if you do not make `main()` method `public`, there is no compilation error. You will **runtime error** because matching `main()` method is not present. Remember that whole syntax should match to execute `main()` method.

> 如果你的 main 方法不是 public ,编译的时候不会有异常,但是在运行是,main 方法不存在

## Why Java main method is static?

Another big question. To understand this, let suppose we do not have the main method as `static`. Now, to invoke any method you need an instance of it. Right?

> 如果我们不使用 static 状态的 main 方法,那么,我们在调用main 的时候就必须要这个类的实例

**Java can have overloaded constructors, we all know. Now, which one should be used and from where the parameters for overloaded constructors will come**. These are just more difficult questions, which helped Java designers to make up their mind and to have the main method as `static`.

> 我们都知道，Java可能有重载的构造函数。现在，应该使用哪一个以及重载构造函数的参数将来自何处

Notice that if you do not make `main()` method `static`, there is no compilation error. You will **runtime error**.

## Why Java main method is void?

Why should it not be void? Have you called this method from your code? NO. **Then there is no use of returning any value to JVM, who actually invokes this method**. It simply doesn’t need any returning value.

> 通常 main 方法的调用者是 JVM, 基本上返回给 JVM 的值是没有用到的

The only thing application would like to communicate to the invoking process is normal or abnormal termination. This is already possible using `*System.exit(int)*`. A non-zero value means abnormal termination otherwise everything was fine.

Basically, `java.exe` is a super simple C application that **parses the command line**, **creates a new String array** in the JVM to hold those arguments, **parses out the class name** that you specified as containing `main()`, **uses JNI calls to find the main()** method itself, then **invokes the main()** method, passing in the newly created string array as a parameter.

This is very, very much like what you do when you use reflection from Java, it just uses confusingly named native function calls instead.

## 7. main() method native code in java.c

Download and extract the source jar and check out `../launcher/java.c`. It is something like this:

```
*
* Get the application's main class.
*/
if (jarfile != 0) {
mainClassName = GetMainClassName(env, jarfile);
... ...
 
mainClass = LoadClass(env, classname);
if(mainClass == NULL) { /* exception occured */
... ...
 
/* Get the application's main method */
mainID = (*env)->GetStaticMethodID(env, mainClass, "main", "([Ljava/lang/String;)V");
... ...
 
{/* Make sure the main method is public */
jint mods;
jmethodID mid;
jobject obj = (*env)->ToReflectedMethod(env, mainClass, mainID, JNI_TRUE);
... ...
 
/* Build argument array */
mainArgs = NewPlatformStringArray(env, argv, argc);
if (mainArgs == NULL) {
ReportExceptionDescription(env);
goto leave;
}
 
/* Invoke main method. */
(*env)->CallStaticVoidMethod(env, mainClass, mainID, mainArgs);
```

## 8. Do we always need main method to run java program?

I believe not. We do have applets, where we do not write the main method. I still need to check how they are executed. If you already knew, please share with me.

## 9. Summary

Java’s `main` method is used by all developers and everybody know the basic syntax to write it. Yet, very few completely understand the correct reasoning and the way it works. I am still trying to figure out more understanding and will update this post if found more interesting facts.

If you have something to share please add in the comments section or send me a mail. I will get included in your knowledge in this post.