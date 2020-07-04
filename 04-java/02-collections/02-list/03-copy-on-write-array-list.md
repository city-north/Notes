# Java CopyOnWriteArrayList class

> https://www.cnblogs.com/myseries/p/10877420.html
>
> https://howtodoinjava.com/java/collections/java-copyonwritearraylist/

**Java CopyOnWriteArrayList** is a [thread-safe](https://howtodoinjava.com/java/multi-threading/what-is-thread-safety/) variant of **ArrayList** in which all mutative operations (add, set, and so on) are implemented by making a fresh copy of the underlying [array](https://howtodoinjava.com/java-array/).

It’s **[immutable](https://howtodoinjava.com/java/basics/how-to-make-a-java-class-immutable/) snapshot** style iterator method uses a reference to the state of the array at the point that the [iterator](https://howtodoinjava.com/java/collections/java-iterator/) was created. This helps in usecases when traversal operations vastly outnumber list update operations and we do not want to synchronize the traversals and still want thread safety while updating the list.

> 线程安全版本的ArrayList

 [01-COW-CopyOnWrite写时复制机制.md](../../../99-unclassified/01-COW-CopyOnWrite写时复制机制.md) 

> 写入时复制（CopyOnWrite，简称COW）思想是计算机程序设计领域中的一种优化策略。其核心思想是，如果有多个调用者（Callers）同时要求相同的资源（如内存或者是磁盘上的数据存储），他们会共同获取相同的指针指向相同的资源，直到某个调用者视图修改资源内容时，系统才会真正复制一份专用副本（private copy）给该调用者，而其他调用者所见到的最初的资源仍然保持不变。这过程对其他的调用者都是透明的（transparently）。此做法主要的优点是如果调用者没有修改资源，就不会有副本（private copy）被创建，因此多个调用者只是读取操作时可以共享同一份资源。



## CopyOnWriteArrayList Featrue

- CopyOnWriteArrayList 类 实现了`List` 和`RandomAccess` ,Cloneable 和 Serializable
- 支持随机访问意味着 for 循环遍历比迭代器速度快
- 使用更新操作时消耗比较大,因为会`ReentrantLock`加锁
- 因为迭代器创建的时候是用的快照,所以不会抛出**ConcurrentModificationException**
- 迭代器中修改操作不支持,会直接抛出`UnsupportedOperationException`

- 多个线程写入时,复制一份并加锁
- 因为迭代器创建的时候会拷贝一份快照,所以性能比 ArrayList 慢
- 支持所有元素,包括 null

## 4. CopyOnWriteArrayList Constructors

- **CopyOnWriteArrayList()** : Creates an empty list.
- **CopyOnWriteArrayList(Collection c)** : Creates a list containing the elements of the specified collection, in the order they are returned by the collection’s iterator.
- **CopyOnWriteArrayList(object[] array)** : Creates a list holding a copy of the given array.

## 5. CopyOnWriteArrayList Methods

CopyOnWriteArrayList class all the methods which are supported in ArrayList class. The behavior is different only in case of iterators (**snapshot iterator**) AND new backing array created during mutations in the list.

Additionally it provides few methods which are additional to this class.

- **boolean addIfAbsent(object o)** : Append the element if not present.
- **int addAllAbsent(Collection c)** : Appends all of the elements in the specified collection that are not already contained in this list, to the end of this list, in the order that they are returned by the specified collection’s iterator.

For all other methods supported, visit [ArrayList](https://howtodoinjava.com/java-arraylist/) methods section.

## 6. Java CopyOnWriteArrayList Usecases

> 使用场景

We can prefer to use CopyOnWriteArrayList over normal ArrayList in following cases:

1. When list is to be used in concurrent environemnt.

   > 多线程环境

2. Iterations outnumber the mutation operations.

   > 迭代操作超过更新操作

3. Iterators must have snapshot version of list at the time when they were created.

   > 迭代器在创建的时候,必须有 list 的快照

4. We don’t want to [synchronize the thread access](https://howtodoinjava.com/java/multi-threading/wait-notify-and-notifyall-methods/) programatically.

## 7. Java CopyOnWriteArrayList Performance

Due to added step of creating a new backing array everytime the list is updated, it performs worse than ArrayList.
There is no performance overhead on read operations and both classes perform same.

## 8. Conclusion

In this Java Collection tutorial, we learned to use **CopyOnWriteArrayList** class, it’s [constructors](https://howtodoinjava.com/oops/java-constructors/), methods and usecases. We learned the **CopyOnWriteArrayList internal working in java** as well as **CopyOnWriteArrayList vs synchronized arraylist**.

We gone through **Java CopyOnWriteArrayList example program** to demo how snapshot iterators works.

## 代码 

Beggar version of CopyOnWriteArrayList

```java
package cn.eccto.study.java.collections.list;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.RandomAccess;
import java.util.concurrent.locks.ReentrantLock;

/**
 * a beggar version of {@link java.util.concurrent.CopyOnWriteArrayList}
 *
 * @author EricChen 2020/01/30 18:23
 */
public class MyCopyOnWriteList<E> implements MyList, RandomAccess, Cloneable, Serializable {

    private transient volatile Object[] array;

    final transient ReentrantLock lock = new ReentrantLock();

    /**
     * Gets the array , No-private so as to also be accessible from CopyOnWriteSet class.
     */
    final Object[] getArray() {
        return array;
    }

    final void setArray(Object[] array) {
        this.array = array;
    }

    public MyCopyOnWriteList() {
        setArray(new Object[0]);
    }

    public MyCopyOnWriteList(Collection<? extends E> collection) {
        setArray(collection.toArray());
    }


    @Override
    public int size() {
        return getArray().length;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * 获取 指定 index 的值
     */
    public E get(int index) {
        return get(getArray(), index);
    }

    @SuppressWarnings("unchecked")
    private E get(Object[] a, int index) {
        return (E) a[index];
    }

    /**
     * appends the specified elements to the end of this list
     */
    public boolean add(E e) {
        //加锁
        final ReentrantLock lock = new ReentrantLock();
        lock.lock();
        //追加
        try {
            Object[] array = getArray();
            //复制一份
            int length = array.length;
            Object[] newElements = Arrays.copyOf(array, length + 1);
            newElements[length] = e;
            setArray(array);
            return true;
        } finally {
            //释放锁
            lock.unlock();
        }
    }

    /**
     * Removes the element at the specified position in this list , Shifts any subsequent elements to the
     * left ( subtracts one from their indices)
     *
     * @param index index of the specified position
     * @return the element that was removed from the list
     */
    public E remove(int index) {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            Object[] array = getArray();
            int length = array.length;
            E oldValue = get(index);
            int numMoved = length - index - 1;
            if (numMoved == 0) {
                setArray(Arrays.copyOf(array, length - 1));
            } else {
                Object[] newElements = new Object[length - 1];
                System.arraycopy(array, 0, newElements, 0, index);
                System.arraycopy(array, index + 1, newElements, index, numMoved);
                setArray(newElements);
            }
            return oldValue;
        } finally {
            lock.unlock();
        }

    }

}

```

## 迭代器方法

```java
    static final class COWIterator<E> implements ListIterator<E> {
        /** Snapshot of the array */
 
        private final Object[] snapshot;
        /** Index of element to be returned by subsequent call to next.  */
        private int cursor;

        private COWIterator(Object[] elements, int initialCursor) {
            cursor = initialCursor;
            snapshot = elements;
        }

        public boolean hasNext() {
            return cursor < snapshot.length;
        }

        public boolean hasPrevious() {
            return cursor > 0;
        }

        @SuppressWarnings("unchecked")
        public E next() {
            if (! hasNext())
                throw new NoSuchElementException();
            return (E) snapshot[cursor++];
        }

        @SuppressWarnings("unchecked")
        public E previous() {
            if (! hasPrevious())
                throw new NoSuchElementException();
            return (E) snapshot[--cursor];
        }

        public int nextIndex() {
            return cursor;
        }

        public int previousIndex() {
            return cursor-1;
        }

        /**
         * Not supported. Always throws UnsupportedOperationException.
         * @throws UnsupportedOperationException always; {@code remove}
         *         is not supported by this iterator.
         */
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /**
         * Not supported. Always throws UnsupportedOperationException.
         * @throws UnsupportedOperationException always; {@code set}
         *         is not supported by this iterator.
         */
        public void set(E e) {
            throw new UnsupportedOperationException();
        }

        /**
         * Not supported. Always throws UnsupportedOperationException.
         * @throws UnsupportedOperationException always; {@code add}
         *         is not supported by this iterator.
         */
        public void add(E e) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void forEachRemaining(Consumer<? super E> action) {
            Objects.requireNonNull(action);
            Object[] elements = snapshot;
            final int size = elements.length;
            for (int i = cursor; i < size; i++) {
                @SuppressWarnings("unchecked") E e = (E) elements[i];
                action.accept(e);
            }
            cursor = size;
        }
    }

```

- 可以看出在创建迭代器的时候,创建的是内部类
- 拷贝出来一份作为`snapshot`
- 迭代时不允许更新操作

