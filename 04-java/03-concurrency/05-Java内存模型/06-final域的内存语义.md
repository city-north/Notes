# final 域的内存语义

写 final 域的重排序规则可以确保: **在引用变量为任意变量可见之前,该引用变量执行的对象的 final 域已经在构造函数中正确初始化过了**

## final 域的重排序规则

- 在构造函数内对一个 final 域的写入,与随后把这个被构造对象的引用复制给一个引用变量,这两个操作之间不能重排序
- 初次读一个包含 final 域的对象的引用,与随后初次读这个 final 域,这两个操作之间不能重排序

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

## 写 final 域的重排序

- JMM禁止编译器吧 final 域的写重排序到构造函数之外
- 编译器会在 final 域的写之后, 构造函数的 return 之前,插入一个 StoreStore 屏障,禁止吹起把 final 域写重排序到构造函数之外

## 读final 域的重排序

编译器会在 final域的前面插入一个 LoadLoad 屏障