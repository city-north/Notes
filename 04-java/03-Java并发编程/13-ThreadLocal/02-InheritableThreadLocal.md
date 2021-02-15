# InheritableThreadLocal

---

[TOC]

## 简介

**InheritableThreadLocal** 继承了 ThreadLocal , 并重写了三个方法

- childValue(T parentValue);
- ThreadLocalMap getMap(Thread t);
- createMap(Thread t, T firstValue)

在 **InheritableThreadLocal** 的世界里 , 变量 **inheritableThreadLocals** 代替了 threadLocals

实际上还是 Thread类里的变量

![image-20200720080520791](../../../assets/image-20200720080520791.png)

```java
private void init(ThreadGroup g, Runnable target, String name,
                  long stackSize, AccessControlContext acc,
                  boolean inheritThreadLocals) {
    ...
        //获取当前线程
        // ④
        Thread parent = currentThread();
    SecurityManager security = System.getSecurityManager();

    // 如果父线程的 inheritableThreadLocals 变量不为 null
    if (inheritThreadLocals && parent.inheritableThreadLocals != null)
        this.inheritableThreadLocals =
        // ⑥ 设置子线程中的 inheritableThreadLocals 变量
        ThreadLocal.createInheritedMap(parent.inheritableThreadLocals);
    ...

}
```

在创建线程时, 在构造函数里面调用 init 方法

- 代码 ④  获取了当前线程 , 经常是 maIn 线程去创建
- 代码 ⑤ 判断 main 函数所在的线程里面的 **inheritableThreadLocals** 是否为空, 前面我们讲了 **inheritableThreadLocals** 类的 get 和 set 方法操作的是 inheritableThreadLocals 变量,所以这里的不为空
- 代码 ⑥ 设置子线程中的 **inheritableThreadLocals** 变量

```java
static ThreadLocalMap createInheritedMap (ThreadLocalMap parentMap) {
    return new ThreadLocalMap(parentMap);
}
```

使用父类的 inheritableThreadLocals 的变量去创建一个新的 ThreadLocalMap 并给子类

```java
private ThreadLocalMap(ThreadLocalMap parentMap) {
  Entry[] parentTable = parentMap.table;
  int len = parentTable.length;
  setThreshold(len);
  table = new Entry[len];

  for (int j = 0; j < len; j++) {
    Entry e = parentTable[j];
    if (e != null) {
      @SuppressWarnings("unchecked")
      ThreadLocal<Object> key = (ThreadLocal<Object>) e.get();
      if (key != null) {
        // ⑦ 实际上这里是调用了重写的方法
        Object value = key.childValue(e.value);
        Entry c = new Entry(key, value);
        int h = key.threadLocalHashCode & (len - 1);
        while (table[h] != null)
          h = nextIndex(h, len);
        table[h] = c;
        size++;
      }
    }
  }
}
```

在构造函数内部把父线程的 **inheritableThreadLocals**  成员变量复制给了新的 ThreadLocalMap .

其中代码 ⑦ 调用了 **inheritableThreadLocals** 类重写的代码 ①

## 结束

**InheritableThreadLocal** 类通过重写  getMap 和  createMap 方法, 让本地变量保存到了具体线程的 **inheritableThreadLocals** 变量里面, 那么线程在通过 **inheritableThreadLocals** 类示例的 set 或者 get 方法设置变量时, 就会创建当前线程的 **inheritableThreadLocals** ,

当父线程创建子线程时, 构造函数会把父线程中的 **inheritableThreadLocals** 变量的的本地变量复制一份到 子线程的 **inheritableThreadLocals** 变量里面去

子线程可以访问在父线程设置的本地变量

```java
public class InheritableThreadLocal<T> extends ThreadLocal<T> {

  //获取
  protected T childValue(T parentValue) {
    return parentValue;
  }

  //② 获取 Map
  ThreadLocalMap getMap(Thread t) {
    return t.inheritableThreadLocals;
  }

  //③ 创建 Map
  void createMap(Thread t, T firstValue) {
    t.inheritableThreadLocals = new ThreadLocalMap(this, firstValue);
  }
}
```

