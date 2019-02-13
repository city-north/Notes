# TreeMap

好文章https://www.jianshu.com/p/69f11fc9ea38

底层采用红黑树进行实现，元素是有序的，每次添加元素，都会根据红黑树的算法进行自平衡，查找的时间复杂度为O(log(n))

```
红黑树的定义:
（1）根节点是黑色。
（2）每个叶节点（NIL节点，空节点）是黑色的。
（3）节点是红色或黑色。
（4）如果一个节点是红色，那么他的两个子节点都是黑色
（5）从任一节点到其每个叶子的所有路径都包含相同数目的黑色节点。
```

红黑树参照数据结构的笔记。

| Data Structure                               | Add      | Find     | Delete   | GetByIndex |
| -------------------------------------------- | -------- | -------- | -------- | ---------- |
| Array (T[])                                  | O(n)     | O(n)     | O(n)     | O(1)       |
| Linked list (LinkedList<T>)                  | O(1)     | O(n)     | O(n)     | O(n)       |
| Resizable array list (List<T>)               | O(1)     | O(n)     | O(n)     | O(1)       |
| Stack (Stack<T>)                             | O(1)     | -        | O(1)     | -          |
| Queue (Queue<T>)                             | O(1)     | -        | O(1)     | -          |
| Hash table (Dictionary<K,T>)                 | O(1)     | O(1)     | O(1)     | -          |
| Tree-based dictionary(SortedDictionary<K,T>) | O(log n) | O(log n) | O(log n) | -          |
| Hash table based set (HashSet<T>)            | O(1)     | O(1)     | O(1)     | -          |
| Tree based set (SortedSet<T>)                | O(log n) | O(log n) | O(log n) | -          |