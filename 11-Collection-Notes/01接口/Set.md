## Set

Set是一个不能包含重复元素的Collection

- HashSet：使用HashTable存储，效率高，无序存放
- TreeSet：使用红黑树存储，效率比HashSet低，有序存储
- LinkedHashSet：由链表和哈希表组成，它根据元素的插入顺序对其元素进行排序

## 常见用法

如果你有一个Collection c,你想创建另一个`Collection`包含相同的节点：

```java
Collection<Type> noDups = new HashSet<Type>(c);
```

或者在Java8里：

```java
c.stream()
.collect(Collectors.toSet()); // no duplicates 没有复制，直接转换
```

收集 `Collection`中的某个属性到`TreeSet`:

```java
Set<String> set = people.stream()
.map(Person::getName)
.collect(Collectors.toCollection(TreeSet::new));
```

下面是一个第一种常见用法的变种。它在移出重复元素的同时，保留了原`Collection`中的顺序。

```java
Collection<Type> noDups = new LinkedHashSet<Type>(c);
```

下面是一个工具类的通用方法，封装了之前习惯用法，将传入的`Collection`转化为`Set`

```java
public static <E> Set<E> removeDups(Collection<E> c) {
    return new LinkedHashSet<E>(c);
}
```

## 小例子

找到所有的独立单词，第一个使用的JDK8的聚合函数：

因为Set不允许有重复的元素，所以可以直接将Array转化为Set完成独立单词的筛选。

```java
import java.util.*;
import java.util.stream.*;

public class FindDups {
    public static void main(String[] args) {
        Set<String> distinctWords = Arrays.asList(args).stream()
		.collect(Collectors.toSet()); 
        System.out.println(distinctWords.size()+ 
                           " distinct words: " + 
                           distinctWords);
    }
}
```

使用`for-each` 结构:

```java
import java.util.*;

public class FindDups {
    public static void main(String[] args) {
        Set<String> s = new HashSet<String>();
        for (String a : args)
               s.add(a);
               System.out.println(s.size() + " distinct words: " + s);
    }
}
```

输入：`java FindDups i came i saw i left`

输出：`4 distinct words: [left, came, saw, i]`

## 批量操作

- `s1.containsAll(s2)` — returns `true` if `s2` is a **subset** of `s1`. (`s2` is a subset of `s1` if set `s1` contains all of the elements in `s2`.)
- `s1.addAll(s2)` — transforms `s1` into the **union** of `s1` and `s2`. (The union of two sets is the set containing all of the elements contained in either set.)
- `s1.retainAll(s2)` — transforms `s1` into the intersection of `s1` and `s2`. (The intersection of two sets is the set containing only the elements common to both sets.)，将S1转化为`s1`和`s2`的交集。
- `s1.removeAll(s2)` — transforms `s1` into the (asymmetric) set difference of `s1` and `s2`. (For example, the set difference of `s1` minus `s2` is the set containing all of the elements found in `s1` but not in `s2`.)

打印只出现一次和出现多次的单词：

```java
import java.util.*;

public class FindDups2 {
    public static void main(String[] args) {
        Set<String> uniques = new HashSet<String>();
        Set<String> dups    = new HashSet<String>();

        for (String a : args)
            if (!uniques.add(a))
                dups.add(a);

        // Destructive set-difference
        uniques.removeAll(dups);

        System.out.println("Unique words:    " + uniques);
        System.out.println("Duplicate words: " + dups);
    }
}
```

输入： (`i came i saw i left`)

输出：

```java
//只出现一次的单词：
Unique words:    [left, saw, came]
//出现多于一次的单词：
Duplicate words: [i]
```

包含在两个指定集合中的任意一个中的元素集合，但不包含在两个集合中。

```java
Set<Type> symmetricDiff = new HashSet<Type>(s1);
symmetricDiff.addAll(s2);
Set<Type> tmp = new HashSet<Type>(s1);
tmp.retainAll(s2);
symmetricDiff.removeAll(tmp);
```