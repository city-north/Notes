# Diference between ArrayList and Vector

**ArrayList** and **Vector**, both implements `java.util.List` interface and provide capability to store and get objects within using simple API methods. Still they are different in many aspects and we need to understand both classes in detail to make a wise decision when to use which class.

## 1. ArrayList vs Vector – Thread safety

- Vector : 所有的方法都是`**synchronized** 线程安全
- ArrayList : 线程不安全

## 2. ArrayList vs Vector – Capacity increment

- Vector : 拓展100% ,也就是从 1 拓展到 2
- ArrayList : 拓展50% 也就是 从 1 拓展到1.5

## 4. ArrayList vs Vector – ConcurrentModificationException

There is one difference on how these colelction handle the iteration while the collection is still modifying by program.

`ArrayList` provide iterators, which are **fail-fast**. As soon as we modify the arraylist structure (add or remove elements), the iterator will throw **ConcurrentModificationException** error.

`Vector` provide **iterator** as well as **enumeration**. Iterators are fail-fast by enumerations are not. If we modify the vector during iteration over enumeration, it does not fail.

- `ArrayList` 快速失败
- `Vector`提供**迭代器**以及**枚举**。通过迭代器是快速失效的，枚举不是。如果我们在枚举的迭代过程中修改向量，它不会失败。

```kava
    /**
     * Vector is thread safe because all its methods are synchronized 
     */
    private static void fastFailVector() {
        Vector<String> vector = new Vector<>();
        MyThreadPoolExecutor.execute(() -> {
            vector.add("to");
            vector.forEach(System.out::println);
            vector.add("do");
            vector.add("in");
            vector.add("java");
        });
    }

    /**
     * ArrayList is not thread safe , it is fast-fail 
     */
    private static void fastFailArrayList() {
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("how");
        List<String> synchronizedList = Collections.synchronizedList(arrayList);
        MyThreadPoolExecutor.execute(() -> {
            synchronizedList.add("to");
            synchronizedList.forEach(System.out::println);
            synchronizedList.add("do");
            synchronizedList.add("in");
            synchronizedList.add("java");

        });

    }
```

