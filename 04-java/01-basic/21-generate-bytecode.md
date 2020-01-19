# Java View / Generate Bytecode of Class file

Many times, we need to understand what a compiler is doing under the hood. How java statements we are writing, will be reordered and executed. Also, we need to see the byte code for learning purpose also, I do it seldom. In this tutorial, I am giving an example of how to generate the byte code for a class file in java.

```java
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
public class ResourceManagementInJava7 
{
    public static void main(String[] args) 
    {
        try (BufferedReader br = new BufferedReader(new FileReader("C:/temp/test.txt")))
        {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) 
            {
                System.out.println(sCurrentLine);
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
}
```



## **Step 1) Compile the file ResourceManagementInJava7.java using command javac (optional)**

```
$ javac ResourceManagementInJava7.java
```

```
-rw-r--r--  1 ec  staff   1.0K Jan 18 21:27 ResourceManagementInJava7.class
-rw-r--r--  1 ec  staff   553B Jan 18 21:27 ResourceManagementInJava7.java
```

This will generate the .class file ResourceManagementInJava7.class.

## **Step 2) Execute javap command and redirect the output to .bc file**

```
$ javap -c ResourceManagementInJava7  > tempjavatestbytecode.bc 
```

> javap是jdk自带的反解析工具。它的作用就是根据class字节码文件，反解析出当前类对应的code区（汇编指令）、本地变量表、异常表和代码行偏移量映射表、常量池等等信息。

```
$ cat tempjavatestbytecode.bc 
Compiled from "ResourceManagementInJava7.java"
public class ResourceManagementInJava7 {
  public ResourceManagementInJava7();
    Code:
       0: aload_0
       1: invokespecial #1                  // Method java/lang/Object."<init>":()V
       4: return

  public static void main(java.lang.String[]);
    Code:
       0: new           #2                  // class java/io/BufferedReader
       3: dup
       4: new           #3                  // class java/io/FileReader
       7: dup
       8: ldc           #4                  // String C:/temp/test.txt
      10: invokespecial #5                  // Method java/io/FileReader."<init>":(Ljava/lang/String;)V
      13: invokespecial #6                  // Method java/io/BufferedReader."<init>":(Ljava/io/Reader;)V
      16: astore_1
      17: aconst_null
      18: astore_2
      19: aload_1
      20: invokevirtual #7                  // Method java/io/BufferedReader.readLine:()Ljava/lang/String;
      23: dup
      24: astore_3
      25: ifnull        38
      28: getstatic     #8                  // Field java/lang/System.out:Ljava/io/PrintStream;
      31: aload_3
      32: invokevirtual #9                  // Method java/io/PrintStream.println:(Ljava/lang/String;)V
      35: goto          19
      38: aload_1
      39: ifnull        109
      42: aload_2
      43: ifnull        62
      46: aload_1
      47: invokevirtual #10                 // Method java/io/BufferedReader.close:()V
      50: goto          109
      53: astore_3
      54: aload_2
      55: aload_3
      56: invokevirtual #12                 // Method java/lang/Throwable.addSuppressed:(Ljava/lang/Throwable;)V
      59: goto          109
      62: aload_1
      63: invokevirtual #10                 // Method java/io/BufferedReader.close:()V
      66: goto          109
      69: astore_3
      70: aload_3
      71: astore_2
      72: aload_3
      73: athrow
      74: astore        4
      76: aload_1
      77: ifnull        106
      80: aload_2
      81: ifnull        102
      84: aload_1
      85: invokevirtual #10                 // Method java/io/BufferedReader.close:()V
      88: goto          106
      91: astore        5
      93: aload_2
      94: aload         5
      96: invokevirtual #12                 // Method java/lang/Throwable.addSuppressed:(Ljava/lang/Throwable;)V
      99: goto          106
     102: aload_1
     103: invokevirtual #10                 // Method java/io/BufferedReader.close:()V
     106: aload         4
     108: athrow
     109: goto          117
     112: astore_1
     113: aload_1
     114: invokevirtual #14                 // Method java/io/IOException.printStackTrace:()V
     117: return
    Exception table:
       from    to  target type
          46    50    53   Class java/lang/Throwable
          19    38    69   Class java/lang/Throwable
          19    38    74   any
          84    88    91   Class java/lang/Throwable
          69    76    74   any
           0   109   112   Class java/io/IOException
}
```

