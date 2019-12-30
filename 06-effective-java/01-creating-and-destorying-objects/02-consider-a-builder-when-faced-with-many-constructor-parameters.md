---
title:  EffectiveJava第2条:静态工厂和构造器的局限性
date: 2019-02-27 09:45:49
tags: effective-java
---

- 不能很好地拓展到大量的可选参数

如果类的构造器或者静态工厂中具有多个参数，设计这个类是，Builder模式就是不错的选择，特别是当大多数参数都是可选的时候。
举个栗子：

<!-- more -->

```java
public class Person{
	private int id;   //必须
	private String name;//必须
	private int age;
	private int gender;
	private String address;
}
```
当我需要构建Person类时，`id`字段和`name`字段是必须的，其他可选。如何构造该对象呢？我们一般会选择**重载构造器**的方式

```java
public class Person{
	private int id;   //必须
	private String name;//必须
	private int age;
	private int gender;//0女;1男,-1未知
	private String address;
	
	public Person(int id ,String name){
		this(id,name,0);
	}
	
	public Person(int id ,String name,int age){
		this(id,name,age,-1);
	}
	
	public Person(int id ,String name,int age, int gender){
		this(id,name,age,-1,0);
	}
	
	public Person(int id ,String name, int age ,int gender , String address){
		this.id = id;
		this.age = age;
		this.gender = gander;
		this.address = address;
	}
}

```

可以看出重载构造器具有以下缺点：
- 拓展性插，如果参数数目发生变化，那么它很快就会失去控制。
- 重载构造器可行，但是当有许多参数的时候，客户端代码会很难编写，并仍然较难以阅读。

为了解决这个问题，JavaBeans模式应运而生，这种方法先用无参创建对象，然后都要用Setter来设置每个必要的参数，以及每个相关可选的参数：
## JavaBeans模式
```java
public class Person {
    private int id;   //必须
    private String name;//必须
    private int age;
    private int gender;//0女;1男,-1未知
    private String address;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}

```
但是JavaBeans模式也有缺点：
- 该类的构造过程被到了几个调用中，在构造过程中可能处于不一致状态。
- 类无法通过检验构造器参数的有效性来保证一致性
- JavaBean模式阻止了类做成不可变的可能，头疼的线程安全问题

## Builder 建造者模式
[传送门到设计模式的笔记](设计模式笔记 - 建造者设计模式
https://www.showdoc.cc/eccto002?page_id=1595532059546050 "传送门到设计模式的笔记")

优势：
- 能像重叠构造器一样保证安全性
- 能像JavaBeans模式一样具有可读性

其核心思想是：不直接生成想要的对象，而是让客户端利用所有必要的参数调用构造器，得到一个Builder对象。通过Builder对象调用类似于setter的方法，来设置相关的可选参数。

```java
public class Person {
    private int id;    //必须
    private String name; //必须
    private int age;
    private int gender; //0女;1男,-1未知
    private String address;

    public static class Builder{
        private final int id;    //必须
        private final String name; //必须
        private int age;
        private int gender; //0女;1男,-1未知
        private String address;

        public Builder(int id, String name) {
            this.id = id;
            this.name = name;
        }
        public Builder age(int val){
            age = val;
            return this;
        }
        public Builder gender(int val){
            gender = val;
            return this;
        }
        public Builder address(String val){
            address = val;
            return this;
        }
    }
    private Person(Builder builder){
        id = builder.id;
        name = builder.name;
        age = builder.age;
        gender = builder.gender;
        address = builder.address;
    }
}
```

注意点：
- Persion 是不可变的，所有的默认值都单独存放在一个地方
- builder 的setter方法返回builder本身

```java
Person person = new Person.Builder(1,"eric")
	.address("123")
	.age(1)
	.build();
```

##总结：
- builder可以对其参数加强约束条件，builder方法可以检验这些约束条件。
- 将参数从builder拷贝到对象中之后，并在对象域而不是builder域进行检验（39条）。
- 违反条件的参数builder方法应该抛出illegalArgumentException

##优势
- builder可以有多个可变参数，随着参数的增加，只需要加对应的setter方法
- 灵活，可以使用builder构建多个对象，builder参数可以在创建期间进行调整，也可以随着不同的对象而改变
- builder可以自动填充某些域。

##缺点
- 为了创建对象，先创建其构造器，性能会有影响，但是不大
- Builder模式比重载构造器方法更加冗长，因此只有在很多参数的时候才能使用，比如4个或者更多

## 集成到项目中
可以使用jdk的`Builder<T>`

```java
@FunctionalInterface
public interface Builder<T> {
    /**
     * Builds and returns the object.
     */
    public T build();
}

```