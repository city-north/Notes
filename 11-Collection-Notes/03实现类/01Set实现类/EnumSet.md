# EnumSet

[参考文章](https://blog.csdn.net/tugangkai/article/details/79509067)

`EnumSet`的实现是用极为精简和高效的位向量实现的，位向量是计算机程序中解决问题的一种常用方式，与`EnumMap`没有任何关系。

## 创建

`EnumSet`提供了若干工厂方法创建，例如

```
public static <E extends Enum<E>> EnumSet<E> noneOf(Class<E> elementType)
```

noneOf方法会创建一个指定枚举类型的EnumSet，不含任何元素。创建的EnumSet对象的实际类型是EnumSet的子类

## 例子

```
enum Day {
    MONDAY, TUESDAY, WEDNESDAY,
    THURSDAY, FRIDAY, SATURDAY, SUNDAY
}
```

```
Set<Day> weekend = EnumSet.noneOf(Day.class);
weekend.add(Day.SATURDAY);
weekend.add(Day.SUNDAY);
System.out.println(weekend);
```

输出：

```
[SATURDAY, SUNDAY]
```

```
// 初始集合包括指定枚举类型的所有枚举值
<E extends Enum<E>> EnumSet<E> allOf(Class<E> elementType)
// 初始集合包括枚举值中指定范围的元素
<E extends Enum<E>> EnumSet<E> range(E from, E to)
// 初始集合包括指定集合的补集
<E extends Enum<E>> EnumSet<E> complementOf(EnumSet<E> s)
// 初始集合包括参数中的所有元素
<E extends Enum<E>> EnumSet<E> of(E e)
<E extends Enum<E>> EnumSet<E> of(E e1, E e2)
<E extends Enum<E>> EnumSet<E> of(E e1, E e2, E e3)
<E extends Enum<E>> EnumSet<E> of(E e1, E e2, E e3, E e4)
<E extends Enum<E>> EnumSet<E> of(E e1, E e2, E e3, E e4, E e5)
<E extends Enum<E>> EnumSet<E> of(E first, E... rest)
// 初始集合包括参数容器中的所有元素
<E extends Enum<E>> EnumSet<E> copyOf(EnumSet<E> s)
<E extends Enum<E>> EnumSet<E> copyOf(Collection<E> c)
```