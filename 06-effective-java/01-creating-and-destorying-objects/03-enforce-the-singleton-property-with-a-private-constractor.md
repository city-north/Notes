---
title:  EffectiveJava第3条:用私有构造器或者枚举类型强化Singleton属性
date:  2019-02-28 21:27:58
tags: effective-java
---
## Singleton的类
Singleton 指仅仅被实例化一次的类，通常被用来代表那些本质上唯一的系统组件。

本文介绍三种实现方式
- final 修饰公有静态成员
- 静态工厂方法实现Singleton
- 使用枚举实现Singleton

<!-- more -->

### final 修饰公有静态成员
私有构造器仅仅只能被调用一次，用来实例化公有的静态final域Elvis.INSTANCE。因为没有其他构造器，所以保证了Elvis的全局唯一性。
```java
public class Elvis{
	public static final Elvis INSTANCE = new Elvis();
	private Elvis(){
		...
	}
	publid void method(){
		...
	}
}
```
客户端使用：
```java
Elvis.INSTANCE.method();
```

但是，享有特权的客户端可以使用`AccessibleObject.setAccessible()`,通过反射机制调用私有的构造器。

### 公有的成员是一个静态工厂
```java
public class Elvis{
	private static final Elvis INSTANCE = new Elvis();
	//私有的构造器
	private Elvis(){}
	public static Elvis getInstance(){
		return INSTANCE;
	}
	
	public void method(){
		...
	}
}
```
对于静态方法`Elvis.getinstance()`的所有调用，都会返回同一个对象引用，所以，永远不会创建其他的Elvis实例。

公有域方法的好处:
- 可以在类的成员的声明很清楚地表明这类睡个Singleton,公有的静态域是final的，所以该与弄湿包含相同的对象引用。
- 灵活性：在不改变API的前提下，我们可以改变该类的单例想法。

### 注意：
想让该类编程可序列化的。仅仅在类声明上添加`implements Serializable`是不够的，为了维护并保证Singleton ，必须声明所有的实例域都是瞬时（transient）的。

## 枚举实现
这种实现方式还没有被广泛采用，但这是实现单例模式的最佳方法。它更简洁，自动支持序列化机制，绝对防止多次实例化。 这种方式是 Effective Java 作者 Josh Bloch 提倡的方式，它不仅能避免多线程同步问题，而且还自动支持序列化机制，防止反序列化重新创建新的对象，绝对防止多次实例化。不过，由于 JDK1.5 之后才加入 enum 特性，用这种方式写不免让人感觉生疏，在实际工作中，也很少用。 不能通过 reflection attack 来调用私有构造方法。
```java
public enum Singleton {  
    INSTANCE;  
    private Singleton() {}
}
```
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
使用
```java
public class Main {
    public static void main(String[] args) {
        DBConnection con1 = DataSourceEnum.DATASOURCE.getConnection();
        DBConnection con2 = DataSourceEnum.DATASOURCE.getConnection();
        System.out.println(con1 == con2);
    }
}
```
输出结果为：true 结果表明两次获取返回了相同的实例。