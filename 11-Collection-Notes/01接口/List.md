# List

`List`是有序的`Collection`：包含以下操作：

- `Positional access` 位置访问 — manipulates elements based on their numerical position in the list. This includes methods such as `get`, `set`, `add`, `addAll`, and `remove`.
- `Search`检索— searches for a specified object in the list and returns its numerical position. Search methods include `indexOf` and `lastIndexOf`.判断第一次出现的位置与最后出现位置
- `Iteration`迭代 — extends `Iterator` semantics to take advantage of the list's sequential nature. The `listIterator` methods provide this behavior.
- `Range-view`距离视图 — The `sublist` method performs arbitrary *range operations* on the list.

## 集合操作

JDK8以后使用聚合一些指定的属性

```java
List<String> list = people.stream()
.map(Person::getName)
.collect(Collectors.toList());
```

## 位置访问以及检索

基本的位置方位方法有 `get`, `set`, `add` and `remove`. (The `set` and `remove` 操作会返回操作过的值) ，其他操作 (`indexOf` and `lastIndexOf`)第一次出现以及最后一次出现指定节点的索引。

调换指定位置的元素：

```java
public static <E> void swap(List<E> a, int i, int j) {
    E tmp = a.get(i);
    a.set(i, a.get(j));
    a.set(j, tmp);
}
```

随机打乱`List`的顺序可以使用`shuffle`方法

```java
import java.util.*;

public class Shuffle {
    public static void main(String[] args) {
        List<String> list = Arrays.asList(args);
        Collections.shuffle(list);
        System.out.println(list);
    }
}
```

## Iterators



```java
for (ListIterator<Type> it = list.listIterator(list.size()); it.hasPrevious(); ) {
    Type t = it.previous();
    ...
}
```

`ListIterator`和`Iterator`的区别：简单说就是`添加/逆序/定位`

- `ListIterator`有`add()`方法，可以向`List`中添加对象，而`Iterator`不能
- `ListIterator`和`Iterator`都有`hasNext()`和`next()`方法，可以实现顺序向后遍历，但是`ListIterator`有`hasPrevious()`和`previous()`方法，可以实现逆向（顺序向前）遍历。`Iterator`就不可以。
- `ListIterator`可以定位当前的索引位置，`nextIndex()`和`previousIndex()`可以实现。`Iterator`没有此功能。
-  都可实现删除对象，但是ListIterator可以实现对象的修改，set()方法可以实现。Iierator仅能遍历，不能修改。

## List算法

- `sort` — sorts a `List` using a merge sort algorithm, which provides a fast, stable sort. (A *stable sort* is one that does not reorder equal elements.)归并排序
- `shuffle` — randomly permutes the elements in a `List`.随机打乱List
- `reverse` — reverses the order of the elements in a `List`.翻转List
- `rotate` — rotates all the elements in a `List` by a specified distance.旋转
- `swap` — swaps the elements at specified positions in a `List`.
- `replaceAll` — replaces all occurrences of one specified value with another.
- `fill` — overwrites every element in a `List` with the specified value.
- `copy` — copies the source `List` into the destination `List`.
- `binarySearch` — searches for an element in an ordered `List` using the binary search algorithm.
- `indexOfSubList` — returns the index of the first sublist of one `List` that is equal to another.
- `lastIndexOfSubList` — returns the index of the last sublist of one `List` that is equal to another.

### Collections 打乱List顺序

`Collections`类中存在一个方法，根据指定的随机源来排列指定的列表，其实现有点微妙：它是从`List`的底部向上运行列表，重复的将随机选择的元素交换到当前位置，不像大部分幼稚的洗牌方式：它是公平的（假设随机性的无偏源，所有的排列都以相同的可能性发生，）和快速的（需要精确的list.size -1 交换）

```java
import java.util.*;

public class Shuffle {
    public static void main(String[] args) {
        List<String> list = new ArrayList<String>();
        for (String a : args)
            list.add(a);
        Collections.shuffle(list, new Random());
        System.out.println(list);
    }
}
```



### Range-View操作

`subList(int fromIndex , int toIndex)` 返回一个List视图，从`fromIndex`起到`toIndex`，这个半开范围反映了for循环的典型情况。

```java
for (int i = fromIndex; i < toIndex; i++) {
    ...
}
```

返回的`List`在原`List`基础之上的，所以前者因后者变化而变化。比如，下面的例子删除了`List`指定范围内的元素：

```
list.subList(fromIndex, toIndex).clear();
```

在指定范围内查询元素：

```java
int i = list.subList(fromIndex, toIndex).indexOf(o);
int j = list.subList(fromIndex, toIndex).lastIndexOf(o);
```





### 对不在使用的List进行回收

在对使用完毕的`list`，如一些中间变量的`list`，可以调用`clear()`方法通知垃圾回收站回收，`list`中的对象会被垃圾回收站回收，但是List对象依然存在。如果直到程序结束都不需要再使用的到该对象，可以置为`null`，通知垃圾回收站回收。