---
title:  EffectiveJava第6条:避免创建不必要的对象
date:  2019-03-03 21:28:17
tags: effective-java
---

## 拒绝重复创建不可变对象
一般来说，最好能重用对象而不是每次需要的时候就创建一个功能相同的对象：

例如：
`String s = new String ("hello");`

该语句每次执行都会创建个新的String实例，但没有必要创建这些对象

改为:
`String s = "hello";`
对于所有在同一台虚拟机中运行的代码，只要他们包含相同的字符串字面常量，该对象就会被重用。

<!-- more --> 

## 尽量使用静态工厂而不是构造器
如
`Boolean.valueOf(String);`总能够由于构造器`Boolean(Sytring);`。

## 重用哪些已知的不会被修改的可变对象

```java
public class Person {
    private final Date birthDate;
	
	...

    public isBabyBommer(){
        Calendar gmtCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        gmtCal.set(1946,Calendar.JANUARY,1,0,0,0);
        Date boomStart = gmtCal.getTime();
        gmtCal.set(1965,Calendar.JANUARY,1,0,0,0);
        Date boomEnd = gmtCal.getTime();
        return birthDate.compareTo(boomStart) >=0 && birthDate.compareTo(boomEnd) <0;
    }

}
```
`isBabyBommer()`方法每一次被调用时，就会新建一个`Calendar`对象，一个`TimeZone`和两个`Date`实例，这些很明显是不需要的。
**用一个静态的初始化容器（initalizer）**来优化。

```java
public class Person2 {
    private final Date birthDate;
    private static final Date BOOM_START;
    private static final Date BOOM_END;
    
    static {
        Calendar gmtCal = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        gmtCal.set(1946,Calendar.JANUARY,1,0,0,0);
        BOOM_START = gmtCal.getTime();
        gmtCal.set(1965,Calendar.JANUARY,1,0,0,0);
        BOOM_END = gmtCal.getTime();
    }
    public Person2(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public boolean isBabyBommer(){
        return birthDate.compareTo(BOOM_START) >=0 && birthDate.compareTo(BOOM_END) <0;
    }

}

```
改进之后，具有以下特征：
- `Calendar`、`TimeZone`、`Date`只初始化一次。
- `boomStart` 和`boomEnd`从局部变量改为final静态域，作为常量处理
没执行一千万次，原来的版本是32000ms,而改进后的只需要130ms,大约提高了250倍。


