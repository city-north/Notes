# String-StringBuffer和StringBuilder的区别

String是只读字符串，它并不是基本数据类型，而是一个对象。从底层源码来看是一个final类型的字符数组，所引用的字符串不能被改变，一经定义，无法再增删改。每次对String的操作都会生成新的 String对象。

```
private final char value[];
```

```
String name = "Eric" + "CHEN";
//隐式地再堆上new了一个跟原字符串相同的StringBuilder 对象,在调用append方法加上后面的字符
```

他们的底层都是可变的字符数组，所以在进行频繁的字符串操作时，建议使用StringBuffer和 StringBuilder来进行操作。 另外StringBuffer 对方法加了同步锁或者同步方法，所以是 线程安全的。StringBuilder 并没有对方法进行加同步锁，所以是非线程安全的。