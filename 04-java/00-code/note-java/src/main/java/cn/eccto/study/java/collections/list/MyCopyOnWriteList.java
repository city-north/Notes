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
