[返回目录](/README.md)

# 类型推断

Java会从上下文（目标类型）推断用什么函数式接口来配合Lambda表达式。它也可以推断出适合Lambda的签名，因为函数描述符可以通过目标类型来得到。这样做的好处在于，编译器可以了解Lambda表达式的参数类型，这样可以在Lambda语法中省去标注参数类型。

Java编译器会自动判断Lambda的参数类型：

```
List<Apple> greenApples =
filter(inventory, a -> "green".equals(a.getColor()));
```

可以看到，Lambda的参数a,并没有显式类型

Lambda表达式有多个参数，代码可读性的好处就更为明显。例如，你可以这样来创建一个

Comparator 对象：

```
Comparator<Apple> c =
(Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight());
Comparator<Apple> c =
(a1, a2) -> a1.getWeight().compareTo(a2.getWeight());
```

有时候显式写出类型更易读，有时候去掉它们更易读，对于如何让代码更易读，程序员必须做出自己的选择

[返回目录](/README.md)

