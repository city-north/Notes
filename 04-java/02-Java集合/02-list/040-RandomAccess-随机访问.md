# 040-RandomAccess-随机访问

[TOC]

## 什么是随机访问

- 一个标记接口
- 表明 **支持快速(通常是固定时间)随机访问**
- 用于 在迭代时判断是否实现该接口而使用相应的算法

-  `RandomAccess` 这个标记接口就是标记能够随机访问元素的集合， 简单来说就是底层是数组实现的集合。

为了提升性能，在遍历集合前，我们便可以通过 `instanceof` 做判断， 选择合适的集合遍历方式，当数据量很大时， 就能大大提升性能。

这个接口的主要目的是允许通用算法在应用于随机或顺序访问列表时改变它们的行为，以提供良好的性能。

### 简而言之 

```
for (int i=0, n=list.size(); i < n; i++){
	list.get(i);
}
```

```
for (Iterator i=list.iterator(); i.hasNext(); ) {
	i.next(); 
}
```

如果使用第一种 for 循环的方式遍历速度比第二种使用迭代器的方式快,那么就使用这个接口

```java
package cn.eccto.study.java.collections.list;

import java.util.*;

/**
 * usage Example of RanddmAccess
 *
 * @author EricChen 2020/01/24 20:32
 * @see RandomAccess
 */
public class RandomAccessExample {


    public static void main(String[] args) {
        List<String> testArrayList = new ArrayList();
        List<String> testLinkedList = new LinkedList<>();
        for (int i = 0; i < 80000; i++) {
            testArrayList.add("" + i);
            testLinkedList.add("" + i);
        }
        System.out.println("LinkedList , 不支持 RandomAccess");
        System.out.println("for 循环遍历时间:" + traverseByNormalForLoop(testLinkedList));
        System.out.println("iterator 循环遍历时间:" + traverseByIterator(testLinkedList));

        System.out.println("ArrayList , 支持 RandomAccess");

        System.out.println("for 循环遍历时间:" + traverseByNormalForLoop(testArrayList));
        System.out.println("iterator 循环遍历时间:" + traverseByIterator(testArrayList));

        System.out.println("using random access");
        System.out.println("arrayList:" + useRandomAccess(testArrayList));
        System.out.println("LinkedList" + useRandomAccess(testLinkedList));

    }

    /**
     * 使用 for循环来迭代
     */
    private static long traverseByNormalForLoop(List<String> testList) {
        long before = System.currentTimeMillis();
        for (int i = 0; i < testList.size(); i++) {
            testList.get(i);
        }
        long after = System.currentTimeMillis();
        return after - before;
    }


    /**
     * 使用迭代器遍历
     */
    public static long traverseByIterator(List arrayList) {
        Iterator iterator = arrayList.iterator();
        long before = System.currentTimeMillis();
        while (iterator.hasNext()) {
            iterator.next();
        }
        long after = System.currentTimeMillis();
        return after - before;
    }

    /**
     * {@link RandomAccess} usage Example of RanddmAccess
     */
    public static long useRandomAccess(List arrayList) {
        if (arrayList instanceof RandomAccess) {
            //use for
            return traverseByNormalForLoop(arrayList);
        } else {
            // use an iterator
            return traverseByIterator(arrayList);
        }
    }


}

```

```
LinkedList , 不支持 RandomAccess
for 循环遍历时间:18112
iterator 循环遍历时间:5
ArrayList , 支持 RandomAccess
for 循环遍历时间:1
iterator 循环遍历时间:3
using random access
arrayList:1
LinkedList4
```

## 结论

- 不支持 RandomAccess 就意味着最好不要使用 for 循环去遍历