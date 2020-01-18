# Correct way to compare floats or doubles in Java

> 比较 floats 和 doubles 

Correctly **compare float** or **compare double** is not only Java specific problem. It can be observed in almost all the programming languages today. In computer memory, floats and doubles are stored using [IEEE 754](https://en.wikipedia.org/wiki/IEEE_754) standard format. How the actual storage and conversion works, it is out of scope of this article.

For now, just understand that during computations and conversions, *minor rounding errors* can be introduced in these numbers. That’s why it is not advisable to simply rely on the *equality operators (==)* to **compare floating-point numbers**.

> 现在，只需要理解在计算和转换期间，可以在这些数字中引入*较小的舍入错误*。这就是为什么不建议简单地依赖*相等运算符(==)*来比较浮点数**

## 1. Compare double – Simple comparison [Not recommended]

First look at the simple comparison to understand what exactly is wrong with *comparing double with == operator*. In given program, I am creating same floating point number (i.e. `1.1`) using two methods:

1. Add `.1`, 11 times.
2. Multiply `.1` to 11.

In theory, both operations should produce the number `1.1`. And when we compare the results of both methods, it should match.

```java
private static void simpleFloatsComparison() 
{
    //Method 1
    double f1 = .0;
    for (int i = 1; i <= 11; i++) {
        f1 += .1;
    }
 
    //Method 2
    double f2 = .1 * 11;
 
    System.out.println("f1 = " + f1);
    System.out.println("f2 = " + f2);
 
    if (f1 == f2)
        System.out.println("f1 and f2 are equal\n");
    else
        System.out.println("f1 and f2 are not equal\n");
}
```

Program Output.

```
f1 = 1.0999999999999999
f2 = 1.1
f1 and f2 are not equal
```

Look at the both values printed in console. `f1` is computed to `1.0999999999999999`. Its exactly the problem which rounding off causes internally. That’s why, **floating point comparison with `'=='` operator is not recommended**.



## 2. Compare double – Threshold based comparison [Recommended]

推荐写法

Now when we know the problem with equality operator, lets solve it. Using programming, we cannot change the way these floating point numbers are stored or computed. So we have to adapt a solution where we agree that a determine the differences in both values which we can tolerate and still consider the numbers equal. This agreed upon difference in values is called the **threshold** or **epsilon**.

So now to use ‘**threshold based floating point comparison**‘, we can use the `Math.abs()` method to compute a difference between the two numbers and compare the difference to a threshold value.

```java
private static void thresholdBasedFloatsComparison() 
{
    final double THRESHOLD = .0001;
 
    //Method 1
    double f1 = .0;
    for (int i = 1; i <= 11; i++) {
        f1 += .1;
    }
 
    //Method 2
    double f2 = .1 * 11;
 
    System.out.println("f1 = " + f1);
    System.out.println("f2 = " + f2);
 
    if (Math.abs(f1 - f2) < THRESHOLD)
        System.out.println("f1 and f2 are equal using threshold\n");
    else
        System.out.println("f1 and f2 are not equal using threshold\n");
}
```

```
f1 = ``1.0999999999999999
f2 = ``1.1
f1 and f2 are equal using threshold
```



## 3. Compare double – Compare with BigDecimal [Recommended]

In `BigDecimal` class, you can specify the **rounding mode** and **exact precision** which you want to use. Using the exact precision limit, rounding errors are mostly solved.

Best part is that `BigDecimal` numbers are [immutable](https://howtodoinjava.com/java/basics/how-to-make-a-java-class-immutable/) i.e. if you create a `BigDecimal` BD with value `"1.23"`, that object will remain `"1.23"` and can never be changed. This class provide many methods which can be used to do numerical operations on it’s value.

You can use it’s `compareTo()` method to compare to `BigDecimal` numbers. It ignore the scale while comparing.

> **a.compareTo(b);**
>
> Method returns:
>
> -1 – if a < b)
>
> 0 – if a == b
>
> 1 – if a > b

Never use the `equals()` method to compare `BigDecimal` instances. That is because this equals function will compare the scale. If the scale is different, `equals()` will return false, even if they are the same number mathematically

Java program to compare double with `BigDecimal` class.

```java
private static void testBdEquality() 
{
     BigDecimal a = new BigDecimal("2.00");
     BigDecimal b = new BigDecimal("2.0");
 
     System.out.println(a.equals(b));           // false
 
     System.out.println(a.compareTo(b) == 0);   // true
}
```

Now just to verify, let’s solve out original problem using `BigDecimal` class.

```java
private static void bigDecimalComparison() 
{
    //Method 1
    BigDecimal f1 = new BigDecimal("0.0");
    BigDecimal pointOne = new BigDecimal("0.1");
    for (int i = 1; i <= 11; i++) {
        f1 = f1.add(pointOne);
    }
 
    //Method 2
    BigDecimal f2 = new BigDecimal("0.1");
    BigDecimal eleven = new BigDecimal("11");
    f2 = f2.multiply(eleven);
 
    System.out.println("f1 = " + f1);
    System.out.println("f2 = " + f2);
 
    if (f1.compareTo(f2) == 0)
        System.out.println("f1 and f2 are equal using BigDecimal\n");
    else
        System.out.println("f1 and f2 are not equal using BigDecimal\n");
}
```

```
f1 = 1.1
f2 = 1.1
f1 and f2 are equal using BigDecimal
```

That’s all about **comparing floating point numbers in java**. Share your thoughts in comments section.