# 建造者设计模式（Builder Pattern）

## 定义

>  构造者模式：将一个复杂对象的构建与它的表示分离，使得同样的构建过程可以创建不同的表示

## 

[笔记参考于](https://blog.csdn.net/carson_ho/article/details/54910597)

## UML

![](assets/20160325111451193.png)

如果你需要将一个复杂对象的构建与它的表示分离，使得同样的构建过程可以创建不同的表示的意图时，使用构造者模式

## 角色

- Builder（抽象建造者）：
- ConcreteBuilder（具体建造者）：
- Product（产品）：要创建的复杂对象
- Director（指挥者）:调用创建者，来创建复杂对象的各个部分，在指挥者中不涉及具体产品的信息，之只负责保证对象各部分完整创建或按照某种顺序创建。

模式讲解：

1. 指挥者（Director）直接和客户（Client）进行需求沟通； 
2. 沟通后指挥者将客户创建产品的需求划分为各个部件的建造请求（Builder）；
3. 



## 使用Effective Java中的例子：

### 重叠构造器模式

我们先来看看程序员一向习惯使用的重叠构造器模式，在这种模式下，你提供第一个只有必要参数的构造器，第二个构造器有一个可选参数，第三个有两个可选参数，依此类推，最后一个构造器包含所有的可选参数。下面看看其编程实现：

```java
/**
 * 使用重叠构造器模式
 */
public class Person {
    //必要参数
    private final int id;
    private final String name;
    //可选参数
    private final int age;
    private final String sex;
    private final String phone;
    private final String address;
    private final String desc;

    public Person(int id, String name) {
        this(id, name, 0);
    }

    public Person(int id, String name, int age) {
        this(id, name, age, "");
    }

    public Person(int id, String name, int age, String sex) {
        this(id, name, age, sex, "");
    }

    public Person(int id, String name, int age, String sex, String phone) {
        this(id, name, age, sex, phone, "");
    }

    public Person(int id, String name, int age, String sex, String phone, String address) {
        this(id, name, age, sex, phone, address, "");
    }

    public Person(int id, String name, int age, String sex, String phone, String address, String desc) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
        this.phone = phone;
        this.address = address;
        this.desc = desc;
    }
}
```

从上面的代码中，当你想要创建实例的时候，就利用参数列表最短的构造器，但该列表中包含了要设置的所有参数：

```java
Person person = new Persion(1, "李四", 20, "男", "18800000000", "China", "测试使用重叠构造器模式");1
```

这个构造器调用通常需要许多你本不想设置的参数，但还是不得不为它们传递值。 
一句话：重叠构造器可行，但是当有许多参数的时候，创建使用代码会很难写，并且较难以阅读。

### JavaBeans模式

遇到许多构造器参数的时候，还有第二种代替办法，即JavaBeans模式。在这种模式下，调用一个无参构造器来创建对象，然后调用setter办法来设置每个必要的参数，以及每个相关的可选参数： 

```
/**
 * 使用JavaBeans模式
 */
public class Person {
    //必要参数
    private int id;
    private String name;
    //可选参数
    private int age;
    private String sex;
    private String phone;
    private String address;
    private String desc;

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
```

这种模式弥补了重叠构造器模式的不足。创建实例很容易，这样产生的代码读起来也很容易：

```java
Person person = new Person();
person.setId(1);
person.setName("李四");
person.setAge(20);
person.setSex("男");
person.setPhone("18800000000");
person.setAddress("China");
person.setDesc("测试使用JavaBeans模式");

```

遗憾的是，JavaBeans模式自身有着很重要的缺点。因为构造过程被分到了几个调用中，在构造过程中JavaBean可能处于不一致的状态。类无法仅仅通过检验构造器参数的有效性来保证一致性。

## Builder模式

幸运的是，还有第三种替代方法，既能保证像重叠构造器模式那样的安全性，也能保证像JavaBeans模式那么好的可读性。这就是Builder模式的一种形式，不直接生成想要的对象，而是让客户端利用所有必要的参数调用构造器（或者静态工厂），得到一个builder对象。然后客户端在builder对象上调用类似于setter的方法，来设置每个相关的可选参数。最后，客户端调用无参的builder方法来生成不可变的对象。这个builder是它构建类的静态成员类。下面就是它的示例：

```java
package cn.eccto.study.designpattens.creationalpatterns.builderpattern;

/**
 * <p>
 *      Builder 模式
 * </p>
 */
public class Person {
    private int id;
    private String name;
    private int age;
    private String sex;
    private String phone;
    private String address;
    private String desc;


    public static class PersonBuilder {
        private Person person;

        public PersonBuilder(Person person) {
            this.person = person;
        }

        public PersonBuilder(int id, String name) {
            person.setId(id);
            person.setName(name);
        }

        public PersonBuilder age(int val) {
            person.setAge(val);
            return this;
        }

        public PersonBuilder sex(String val) {
            person.setSex(val);
            return this;
        }

        public PersonBuilder phone(String val) {
            person.setPhone(val);
            return this;
        }

        public PersonBuilder address(String val) {
            person.setAddress(val);
            return this;
        }

        public PersonBuilder desc(String val) {
            person.setDesc(val);
            return this;
        }

        public Person build() {
            return person;
        }
    }


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

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    private static PersonBuilder builder() {
        return new PersonBuilder(new Person());
    }


}
```

注意Person是不可变得，所有的默认参数值都单独放在一个地方。builder的setter方法返回builder本身。以便可以把连接起来。下面是客户端使用代码： 

```java
/**
 * 测试使用
 */
public class Test {

    public static void main(String[] args) {
        PersonBuilder builder = Person.builder();
        Person build = builder.address("123")
                .age(12)
                .desc("!23")
                .build();
    }
}
```

