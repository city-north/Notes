---
title:  EffectiveJava第11条:重写 equals 方法时要覆盖 hashCode 方法
date:  2019-03-08 21:28:17
tags: effective-java
---

在每个覆盖了equals方法的类里，我们也必须覆盖hashCode方法
不这么做的后果是
- 这个类将会违反hashCode的通用约定
- 会影响 Collections 中的功能,如 HashMap 或者 HashSet

通用协议包括:
<!-- more -->
- 只要equals方法里用到的信息没有被更改，那么在一个应用的执行期间，不管调用hashCode方法多少次，hashCode方法都必须持续返回相同的值。但在一个应用程序的多次执行过程中，这个值可以不一致。
- 若两个对象根据equals(Object)方法比较是相等的，那么分别调用这两个对象的hashCode方法时必须产生相同的整型数值。
- 若两个对象根据equals(Object)方法比较是不等的，那么分别调用这两个对象的hashCode方法时不一定要产生不同的数值。只是程序员应该知道，对于不等的对象若能产生不同的哈希值，有助于提高哈希表的性能。


##两个相应的对象总会有相同的 HashCode

```java
Map<PhoneNumber, String> m = new HashMap<>();
m.put(new PhoneNumber(707, 867, 5309), "Jenny");
```
如果 PhoneNumber 没有覆盖 `HashCode` ,那么调用 get 方法获取时会获取不到

相同的对象的 HashCode 应该相同,最简单的方式就是:
```
@Override 
public int hashCode() { 
    return 42; 
}
```

一个好的哈希函数会为不相等的实例生成不相等的哈希码。这也正是hashCode约定的第三部分的内容。理想的情况下，哈希函数应该在所有的int值上均匀分布任意不等实例的集合。要达到这种理想状态比较困难。但幸运的是，若我们想近似地达到这种理想状态倒不难。下面给出一种简单的办法：
- 声明一个叫result的int类型变量，然后将它初始化成对象里第一个重要属性的哈希码c，如步骤2.a里面计算的那样。（回顾条目10，重要属性是指影响equals进行比较的属性）

- 对于对象中剩下的每一个重要属性f，完成以下步骤： a. 为这个属性计算一个int型的哈希码c： i. 如果这个属性是基础类型的，计算Type.hashCode(f)，Type是f的类型对应的封箱基础类型。 ii. 如果这个属性是个对象引用，而且这个对象所属类的equals方法通过递归调用equals方法来比较这个属性，则递归调用hashCode方法。如果需要更复杂的比较，则为这个属性计算一个“范式（canonical representation）”，然后针对这个范式调用hashCode方法。如果这个属性的值为null，则返回0（或者其它常量，但一般都是用0）。 iii. 如果这个属性是个数组，则将它的每一个元素都当成重要属性来处理。也就是说，通过递归的应用这些规则来给每个元素计算出一个哈希码，然后按照步骤2.b将这些值合并起来。如果数组没有重要元素，则返回一个常量，最好不要用0。如果所有的元素都是重要的，则使用Arrays.hashCode。 b. 将步骤2.a计算出来的哈希码c通过接下来的公式合并到result里去：`result = 31*result + c`
- 返回result。


例如:
```
// Typical hashCode method
@Override 
public int hashCode() {
    int result = Short.hashCode(areaCode);
    result = 31 * result + Short.hashCode(prefix);
    result = 31 * result + Short.hashCode(lineNum);
    return result;
}
```


如果一个类是不可变的，而且计算哈希码的代价很重要，我们也许应该考虑将哈希码缓存在对象里，而不是每次都去计算。如果你觉得某个类型的大多数对象将会被当做键来使用，那么你必须在实例被创建出来时就计算出哈希码。或者，你也可以选择延迟初始化（lazyily initialize）哈希码，即在hashCode方法第一次被调用时才进行生成动作。需要注意的是，在使用延迟初始化时，要确保类是线程安全的（条目83）。我们还不知道PhoneNumber类是否值得这么处理，但用来展示该如何实现。注意，hashCode属性的初始值（在这里是0）不应该是创建的实例的哈希码：


```
// hashCode method with lazily initialized cached hash code
private int hashCode; // Automatically initialized to 0
@Override 
public int hashCode() {
    int result = hashCode;
    if (result == 0) {
        result = Short.hashCode(areaCode);
        result = 31 * result + Short.hashCode(prefix);
        result = 31 * result + Short.hashCode(lineNum);
        hashCode = result;
    } 
    return result;
}

```

## 不要为了提高性能而试图在哈希码的计算过程中将重要的属性排除掉。
这样做虽然会让哈希函数运行得更快，但其随之变差的质量可能会导致哈希表慢到无法用。特别是，哈希函数可能会面临大量的实例，而这些实例主要的区别在于你选择忽视了的那些属性上。如果这种情况发生了，哈希函数将会把这些实例映射到少数的几个哈希码上，程序也将以线性时间运行，而不是平方时间。

这不只是一个理论问题。在Java 2之前，String的哈希函数最多使用16个字符，从第一个字符开始，在整个字符串均匀地获取。对于层次状名字的大型集合，如URL，这个函数就产生了前面提到的病态行为。


## 总结
每次我们覆盖equals方法时都必须覆盖hashCode方法，不然我们的程序将不会正常运作。我们的hashCode方法必须遵守Object里说明的通用约定，合理地为不相等的实例指派不相等的哈希码。我们按照前面说的方法就可以很容实现，虽然有点繁琐。就像条目10里提到的，AutoValue框架为手动编写equals方法和hashCode方法提供了一个不错的替换方式，多数IDE也提供了这个功能。