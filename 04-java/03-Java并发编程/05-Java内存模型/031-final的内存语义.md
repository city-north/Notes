# 06-final域的内存语义

[TOC]



## final语义总结

final关键字确保我们我

- 获取到对象的引用的时候,final域就已经初始化好了

## final的内存语义

编译器对final域要遵守的两个重排序规则:

- 在构造函数内对一个final域的写入,与随后把这个被构造对象的引用赋值给一个引用变量,这两个操作之 间不能重排序 
- 初次读一个对象(包含final域的)的引用,与随后初次读这个final域,这两个操作之间不能重排序

## final内存语义的实例

```java
public class FinalExample {
    int                 i;  //普通变量
    final int           j;  //final变量
    static FinalExample obj;

    public FinalExample() { //构造函数
        i = 1; //写普通域
        j = 2; //写final域
    }

    public static void writer() { //写线程A执行
        obj = new FinalExample();
    }

    public static void reader() { //读线程B执行
        FinalExample object = obj; //读对象引用
        int a = object.i; //读普通域
        int b = object.j; //读final域
    }
}
```

假设线程A执行了writer()方法,随后另一个线程B执行了reader()方法

### 写final 域的重排序

- JMM禁止编译器把 final 域的写重排序到构造函数之外
- 编译器会在 final 域的写之后, 构造函数的 return 之前,插入一个 StoreStore 屏障,禁止处理器把 final 域写重排序到构造函数之外

我们可以先看 write()方法,这个方法只包含一行代码

```java
obj = new FinalExample();
```

这行代码分为两个步骤:

- 构造一个 FinalExample 类型的对象
- 把这个对象的引用赋值给引用变量 obj

**假设线程 B 读对象引用与读对象的成员域之间没有重排序**,那么下图 是一种可能的执行时序:

- 写普通域的操作被编译器重排序到了构造方法之外
- 读线程 B 错误地读取了普通变量 i 初始化之前的值
- 写 final 域的重排序规则被"**限定**"在了构造方法中
- 读线程 B 正确地读取到了 final变量初始化之后的值



<img src="../../../assets/image-20200324211043013.png" alt="image-20200324211043013" style="zoom:33%;" />



写 final 的重排序规则可以确保:

**在对象引用为任意线程可见之前,对象的 final 域已经被正确地初始化了,而普通域不会有这个保障**

### 读final 域的重排序规则

读 final 域的重排序规则是,在一个线程中,**初次读对象引用与初次读该对象包含的 final 域**,JMM 禁止处理器重排序这两个操作

编译器会在 final域的前面插入一个 LoadLoad 屏障

**初次读对象引用与初次读该对象包含的 final 域.这两个操作之间存在间接依赖关系**. 由于编辑器遵守间接依赖关系,因此编译器不会重排序这两个操作

reader()方法:

```java
public static void reader() { //读线程B执行
  FinalExample object = obj; //读对象引用
  int a = object.i; //读普通域
  int b = object.j; //读final域
}
```

- 初次读引用变量 obj
- 初次读引用变量 obj 指向对象的普通域 j
- 初次读对象引用变量 obj 指向的对象的 final 域

假设写线程 A 没有发生任何重排序,同时程序在不遵守间接依赖的处理器上执行,那么可能出现:

<img src="../../../assets/image-20200324212406017.png" alt="image-20200324212406017" style="zoom:50%;" />

- 读对象的普通域的操作被处理器重排序到读对象引用之前

读普通域时,这个域还没有被线程 A写入,这是一个错误的读取操作

- 读 final 域的重排序规则会把读对象的 final 域的操作限定在读对象引用之后,

此时 final 域已经被线程 A 初始化过了,所以读取正确

读 final 域的重排序规则可以确保:

**在读一个对象的 final域之前,一定会先读包含这个 final 域的对象的引用**

在这个实例程序中,如果该引用不为null,那么 final 域一定已经被线程 A初始化过了

## final语义在处理器中的实现

以 X86 处理器为例:

上面提到:

- **写 final域的重排序规则会要求编译器在 final 域的写之后,构造函数 return 之前插入一个StoreStore屏障**

- **读 final 域的重排序规则要求编译器在读 final 域的操作前面插入一个 LoadLoad 屏障**

但是 x86 处理器中,不会对写-写操作做重排序,所以忽略了 StoreStore 屏障,也不会对存在间接依赖关系的操作做重排序,所以在 X86 处理器中,读 final 域需要的 LoadLoad 屏障也会被省略掉,所以不会写任何屏障

