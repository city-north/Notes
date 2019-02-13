# SortedSet 接口

先看定义：

 [`SortedSet`](https://docs.oracle.com/javase/8/docs/api/java/util/SortedSet.html)是 [`Set`](https://docs.oracle.com/javase/8/docs/api/java/util/Set.html)按升序维护其元素的元素，根据元素的自然顺序或根据创建时`Comparator`提供的元素进行排序`SortedSet`。除正常`Set`操作外，该`SortedSet`接口还提供以下操作：

- `Range view` — 允许允许对已排序集进行任意范围操作
- `Endpoints` — 返回第一个或者是最后一个元素
- `Comparator access` — 如果存在比较器，返回该 `Comparator`

实现类有：

`ConcurrentSkipListSet` 、`TreeSet`



```
public interface SortedSet<E> extends Set<E> {
    // Range-view
    SortedSet<E> subSet(E fromElement, E toElement);//e1和e2之间的元素
    SortedSet<E> headSet(E toElement);//e之前的元素，不包括e
    SortedSet<E> tailSet(E fromElement);//e之后的元素，包括e

    // Endpoints
    E first(); //第一个元素
    E last();//最后一个元素

    // Comparator access
    Comparator<? super E> comparator();//自己定义比较器，对内部元素排序
}
```

## Set Operations

`SortedSet`的大部分操作继承与`Set`，除了两个不同外，其他和普通`Set`无区别。

- 在`Iterator`方法中，返回`iterator`以便在操作遍历有序集合
- 返回的数组`toArray`按顺序包含有序集的元素。

虽然接口无法控制实现类的实现，但是Java平台的所有`SortedSet`实现类的`toString`都返回一个排序好的、所有的元素的`Set`的`String`



## 标准构造器

`SortedSet`的实现类提供一个接受`Collection`  参数的构造器，比如，`TreeSet`的构造器根据自然顺序排列传入的`Collection`。这里需要注意，最好在进行构造前，检查一下参数是否时`SortedSet`，如果是，`TreeSet`根据相同的标准（比较器或自然排序）对新的排序进行排序。

`SortedSet`的实现类提供一个参数为`Comparator`的构造器，并根据`Comparator`的排序方式，返回一个空的`Set`，如`TreeSet`



```

```