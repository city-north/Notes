# 算法

这里描述的多态算法是Java平台提供的可重用功能。它们都来自`Collections`类，并且都采用静态方法的形式，静态方法的第一个参数是要对其执行操作的集合。Java平台提供的绝大多数算法都是对列表实例进行操作的，但也有少数算法是对任意集合实例进行操作的。本节简要介绍以下算法:

- [Sorting](https://docs.oracle.com/javase/tutorial/collections/algorithms/index.html#sorting) ，排序
- [Shuffling](https://docs.oracle.com/javase/tutorial/collections/algorithms/index.html#shuffling)，打乱
- [Routine Data Manipulation](https://docs.oracle.com/javase/tutorial/collections/algorithms/index.html#rdm)，常规数据操作
- [Searching](https://docs.oracle.com/javase/tutorial/collections/algorithms/index.html#searching)，搜索
- [Composition](https://docs.oracle.com/javase/tutorial/collections/algorithms/index.html#composition)
- [Finding Extreme Values](https://docs.oracle.com/javase/tutorial/collections/algorithms/index.html#fev)

## Sorting

归并排序，特点：快速、稳定

- 快速：保证`log(n)`的时间复杂度，在大体排序好的`list`里会更快，根据测试，比快速排序更快，而且快排不保证`nlog(n)`的时间复杂度
- 稳定：它不会对相等的元素重新排序。如果您需要在不同的属性上重复排序相同的列表，那么这一点非常重要。如果邮件程序的用户按邮件日期对收件箱进行排序，然后按发件人对其进行排序，那么用户自然希望来自给定发件人的连续消息列表(仍然)按邮件日期进行排序。只有当第二种排序是稳定的，这才有保证。

```
import java.util.*;

public class Sort {
    public static void main(String[] args) {
        List<String> list = Arrays.asList(args);
        Collections.sort(list);
        System.out.println(list);
    }
}
```

运行：

```
% java Sort i walk the line
```

输出：

```
[i, line, the, walk]
```

我们可以指定`Comparator`

```
// Make a List of all anagram groups above size threshold.
List<List<String>> winners = new ArrayList<List<String>>();
for (List<String> l : m.values())
    if (l.size() >= minGroupSize)
        winners.add(l);

// Sort anagram groups according to size
Collections.sort(winners, new Comparator<List<String>>() {
    public int compare(List<String> o1, List<String> o2) {
        return o2.size() - o1.size();
    }});

// Print anagram groups.
for (List<String> l : winners)
    System.out.println(l.size() + ": " + l);
```

## Shuffling

与`sorting`相反，销毁`List`中可能存在的任何顺序痕迹。输入一个随机源，打乱`List`中的数据。

## Routine Data Manipulation

常规数据访问

The `Collections` class provides five algorithms for doing routine data manipulation on `List` objects, all of which are pretty straightforward:

- `reverse` — reverses the order of the elements in a `List`.反转
- `fill` — overwrites every element in a `List` with the specified value. This operation is useful for reinitializing a `List`.将Collection中的所有元素填写为指定的值，用于重新初始化List
- `copy` — takes two arguments, a destination `List` and a source `List`, and copies the elements of the source into the destination, overwriting its contents. The destination `List` must be at least as long as the source. If it is longer, the remaining elements in the destination `List` are unaffected.拷贝`List`，两个形参。目标`List`和源`List`
- `swap` — swaps the elements at the specified positions in a `List`.切换指定位置的元素
- `addAll` — adds all the specified elements to a `Collection`. The elements to be added may be specified individually or as an array.添加指定元素到一个`Collection`

## Searching

搜索，使用二叉搜索指定一个排序好的`list`里的指定元素：

下面的惯用法：搜索是否有元素，没有的话就添加：

```
int pos = Collections.binarySearch(list, key);
if (pos < 0)
   l.add(-pos-1, key);
```

## Finding Extreme Values

`min`和`max`算法返回指定的最大值和最小值，