# 单例模式(Singleton Pattern)

> 确保一个类在任何情况下只有一个实例，并提供一个全局访问点来访问这个唯一实例

## 特点

- 隐藏起所有的构造方法(私有构造方法)
- 属于创建型模式
- 注意线程安全问题
- 注意延迟加载
- 注意防止序列化和反序列化破坏单例
- 注意防止反射攻击

## 缺点

- 没有接口,拓展困难,只能修改代码

## 常见的单例

- ServletContext
- ServletCOnfig
- ApplicationContext
- DBPool

## 实现方式

- **饿汉单例**：提前实例化，没有延迟加载，不管是否使用都会有一个已经初始化的实例在内存中，线程安全
- **懒汉单例**：只要到要getInstance的时候才创建实例，分为线程安全和线程不安全两种。
- **双检锁/双重校验锁**:使用两个线程锁确保线程安全
- **登记式/内部类**：使用内部类的形式，确保线程安全性。
- **枚举式**：使用枚举类完成线程的安全性
- **ThreadLocal 单例**
- 容器单例 : Ioc 容器中类似的单例

## 单例模式的结构

![](assets/singleton_pattern_uml_diagram.jpg)



## 饿汉单例

在单例类首次加载时就创建实例

实例 1:

```java
public class Singleton{
    private static final Singleton instance= new Singleton();
    private Singleton(){}
    pulic static Singleton getInstance(){
        return instance;
    }
  
  //重写 readResolve 方法`java.io.ObjectStreamClass` 类在反序列初始化对象时会去判断这个方法
  //还是会创建两次,但是发生在 JVM 层面,相对安全,之前反序列化的对象也会被 GC 回收
  private Object readResolve(){
    return instance;
  }
}
```

实例 2:

```java
public class Singleton{
    private static final Singleton instance;
  
  static{
    instance= new Singleton();
  }
    private Singleton(){}
    pulic static Singleton getInstance(){
        return instance;
    }
}
```

为什么使用 final 修饰符:确保不能通过反射攻击破坏单例性

- 优点：没有加锁，执行效率会提高。 线程安全。

- 缺点：类加载时就初始化，浪费内存。 它基于 classloader 机制避免了多线程的同步问题，不过，instance 在类装载时就实例化，虽然导致类装载的原因有很多种，在单例模式中大多数都是调用 getInstance 方法， 但是也不能确定有其他的方式（或者其他的静态方法）导致类装载，这时候初始化 instance 显然没有达到 lazy loading 的效果。

## 懒汉单例

### 1.线程不安全的懒汉

```java
/**
 * 懒加载单例模式(线程不安全)
 *
 * @author EricChen 2019/12/31 22:39
 */
public class LazyInitThreadNotSafeSingleton {
    private static Singleton INSTANCE = null;

    private LazyInitThreadNotSafeSingleton() {
        //防止反射攻击
        throw new IllegalArgumentException("HungrySingleton not allow be constructed");
    }

    public static Singleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Singleton();
        }
        return INSTANCE;
    }

    /**
     * 重写 readResolve 方法`java.io.ObjectStreamClass` 类在反序列初始化对象时会去判断这个方法
     * 还是会创建两次,但是发生在 JVM 层面,相对安全,之前反序列化的对象也会被 GC 回收
     *
     * @see java.io.ObjectStreamClass#invokeReadResolve
     */
    private Object readResolve() {
        return INSTANCE;
    }

}
```



这种方式是最基本的实现方式，这种实现最大的问题就是不支持多线程。因为没有加锁 synchronized，所以严格意义上它并不算单例模式。 这种方式 lazy loading 很明显，不要求线程安全，在多线程不能正常工作。 

### 2.线程安全的懒汉

```java
/**
 * 懒加载单例模式(线程安全)
 *
 * @author EricChen 2019/12/31 22:39
 */
public class LazyInitThreadSafeSyncSingleton {
    private static Singleton INSTANCE = null;

    private LazyInitThreadSafeSyncSingleton() {
        //防止反射攻击
        throw new IllegalArgumentException("HungrySingleton not allow be constructed");
    }

    /**
     * 使用 synchronized 关键字,效率低,锁掉整个类
     */
    public static synchronized Singleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Singleton();
        }
        return INSTANCE;
    }

    /**
     * 重写 readResolve 方法`java.io.ObjectStreamClass` 类在反序列初始化对象时会去判断这个方法
     * 还是会创建两次,但是发生在 JVM 层面,相对安全,之前反序列化的对象也会被 GC 回收
     *
     * @see java.io.ObjectStreamClass#invokeReadResolve
     */
    private Object readResolve() {
        return INSTANCE;
    }

}

```

 这种方式具备很好的 lazy loading，能够在多线程中很好的工作，但是，效率很低，99% 情况下不需要同步。 

- 优点：第一次调用才初始化，避免内存浪费。 

- 缺点：必须加锁 synchronized 才能保证单例，但加锁会影响效率。 getInstance() 的性能对应用程序不是很关键（该方法使用不太频繁） 

## 双检锁/双重校验锁

```java
/**
 * 懒加载单例模式(线程安全) 双检锁
 *
 * @author EricChen 2019/12/31 22:39
 */
public class LazyInitThreadSafeSingleton {
    /**
     * CPU执行的时候会转换成 JVM指令执行,防止指令重排序 volatile
     */
    private static volatile Singleton INSTANCE = null;

    private LazyInitThreadSafeSingleton() {
        //防止反射攻击
        throw new IllegalArgumentException("HungrySingleton not allow be constructed");
    }

    /**
     * 双检锁方式单例
     */
    public static Singleton getInstance() {
        if (INSTANCE == null) {
            synchronized (LazyInitThreadNotSafeSingleton.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Singleton();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 重写 readResolve 方法`java.io.ObjectStreamClass` 类在反序列初始化对象时会去判断这个方法
     * 还是会创建两次,但是发生在 JVM 层面,相对安全,之前反序列化的对象也会被 GC 回收
     *
     * @see java.io.ObjectStreamClass#invokeReadResolve
     */
    private Object readResolve() {
        return INSTANCE;
    }

}

```

**描述**：这种方式采用双锁机制，安全且在多线程情况下能保持高性能。 getInstance() 的性能对应用程序很关键。 

### 注册(登记式)/静态内部类

```java
/**
 * 注册内部类方式的单例模式,线程安全
 *
 * @author EricChen 2019/12/31 23:48
 */
public class InnerClassSingleton {
    private InnerClassSingleton() {
        //防止反射攻击
        throw new IllegalArgumentException("HungrySingleton not allow be constructed");
    }

    public static Singleton getInstance() {
        return LazyHolder.LAZY;
    }

    private static class LazyHolder {
        private static final Singleton LAZY = new Singleton();
    }
    /**
     * 重写 readResolve 方法`java.io.ObjectStreamClass` 类在反序列初始化对象时会去判断这个方法
     * 还是会创建两次,但是发生在 JVM 层面,相对安全,之前反序列化的对象也会被 GC 回收
     *
     * @see java.io.ObjectStreamClass#invokeReadResolve
     */
    private Object readResolve() {
        return LazyHolder.LAZY;
    }
}

```

这种方式能达到双检锁方式一样的功效，但实现更简单。对静态域使用延迟初始化，应使用这种方式而不是双检锁方式。这种方式只适用于静态域的情况，双检锁方式可在实例域需要延迟初始化时使用。

这种方式同样利用了 classloader 机制来保证初始化 instance 时只有一个线程，它跟饿汉单例方式不同的是：

- 饿汉单例只要 Singleton 类被装载了，那么 instance 就会被实例化（没有达到 lazy loading 效果），而这种方式是 Singleton 类被装载了，instance 不一定被初始化。

因为 SingletonHolder 类没有被主动使用，只有通过显式调用 getInstance 方法时，才会显式装载 SingletonHolder 类，从而实例化 instance。想象一下，如果实例化 instance 很消耗资源，所以想让它延迟加载，另外一方面，又不希望在 Singleton 类加载时就实例化，因为不能确保 Singleton 类还可能在其他的地方被主动使用从而被加载，那么这个时候实例化 instance 显然是不合适的。这个时候，这种方式相比饿汉单例就显得很合理。

####  缺点

- 反射还是可以访问私有的构造方法,有可会被反射攻击

### 枚举单例

JDK 层面就不允许枚举通过反射调用构造器,所以就没有反射攻击风险

这种实现方式还没有被广泛采用，但这是实现单例模式的最佳方法。它更简洁，自动支持序列化机制，绝对防止多次实例化。 这种方式是 Effective Java 作者 Josh Bloch 提倡的方式，它不仅能避免多线程同步问题，而且还自动支持序列化机制，防止反序列化重新创建新的对象，绝对防止多次实例化。不过，由于 JDK1.5 之后才加入 enum 特性，用这种方式写不免让人感觉生疏，在实际工作中，也很少用。 不能通过 reflection attack 来调用私有构造方法。 

```java
public enum Singleton {  
    INSTANCE;  
    private Singleton() {}
}
```

[例子参考](https://blog.csdn.net/gavin_dyson/article/details/70832185)

```java
public enum DataSourceEnum {
    DATASOURCE;
    private DBConnection connection = null;
    private DataSourceEnum() {
        connection = new DBConnection();
    }
    public DBConnection getConnection() {
        return connection;
    }
}  
```

```java
public class Main {
    public static void main(String[] args) {
        DBConnection con1 = DataSourceEnum.DATASOURCE.getConnection();
        DBConnection con2 = DataSourceEnum.DATASOURCE.getConnection();
        System.out.println(con1 == con2);
    }
}
```

```java
/**
 * 枚举方式单例
 *
 * @author EricChen 2019/12/31 23:58
 */
public enum EnumSingleton {
    INSTANCE;
    private Singleton SINGLETON_INSTANCE = new Singleton();

    public Singleton getInstance() {
        return SINGLETON_INSTANCE;
    }
}

```



输出结果为：**true**  结果表明两次获取返回了相同的实例。 

## 容器单例



## 总结

一般情况下，**不建议**使用

- 线程安全的懒汉
- 线程不安全的懒汉

需要明确实现懒加载（lazy loading）时，使用**登记式/静态内部类方式**

需要涉及到序列化创建对象时，使用**枚举式单例**。

需要有其他特殊的需求，可以考虑使用**双检锁式单例**。