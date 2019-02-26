# 自定义实现类

例如你要自定义一个`List`



```
public static <T> List<T> asList(T[] a) {
    return new MyArrayList<T>(a);
}

private static class MyArrayList<T> extends AbstractList<T> {

    private final T[] a;

    MyArrayList(T[] array) {
        a = array;
    }

    public T get(int index) {
        return a[index];
    }

    public T set(int index, T element) {
        T oldValue = a[index];
        a[index] = element;
        return oldValue;
    }

    public int size() {
        return a.length;
    }
}
```

好处：自定义了`get``set``size`方法，又可以获得`AbstractList`中定义好的通用方法，例如`ListIterator`、体积操作、搜索操作，Hash Code计算，比较器，String展示方式。

## 其他一些内置拓展抽象类

- [`AbstractCollection`](https://docs.oracle.com/javase/8/docs/api/java/util/AbstractCollection.html) — a `Collection` that is neither a `Set` nor a `List`. At a minimum, you must provide the `iterator` and the `size`methods.
- [`AbstractSet`](https://docs.oracle.com/javase/8/docs/api/java/util/AbstractSet.html) — a `Set`; use is identical to `AbstractCollection`.
- [`AbstractList`](https://docs.oracle.com/javase/8/docs/api/java/util/AbstractList.html) — a `List` backed up by a random-access data store, such as an array. At a minimum, you must provide the `positional access` methods (`get` and, optionally, `set`, `remove`, and `add`) and the `size` method. The abstract class takes care of `listIterator` (and `iterator`).
- [`AbstractSequentialList`](https://docs.oracle.com/javase/8/docs/api/java/util/AbstractSequentialList.html) — a `List` backed up by a sequential-access data store, such as a linked list. At a minimum, you must provide the `listIterator` and `size` methods. The abstract class takes care of the positional access methods. (This is the opposite of `AbstractList`.)
- [`AbstractQueue`](https://docs.oracle.com/javase/8/docs/api/java/util/AbstractQueue.html) — at a minimum, you must provide the `offer`, `peek`, `poll`, and `size` methods and an `iterator` supporting `remove`.
- [`AbstractMap`](https://docs.oracle.com/javase/8/docs/api/java/util/AbstractMap.html) — a `Map`. At a minimum you must provide the `entrySet`view. This is typically implemented with the `AbstractSet` class. If the `Map` is modifiable, you must also provide the `put` method.