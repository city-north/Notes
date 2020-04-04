package cn.eccto.study.java.concurrent.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * 有界队列,当队列为空时,队列的获取操作会被阻塞,直到队列中有新的元素
 * 当队列满的时候 ,队列的操作将会阻塞插入线程,直到队列出现空位
 * </p>
 *
 * @author EricChen 2020/04/04 21:36
 */
public class BoundedQueue<T> {

    private T[] items;

    private Lock lock = new ReentrantLock();
    private Condition waiting = lock.newCondition();
    private Condition available = lock.newCondition();


    //插入序列, 删除序列
    private int addIndex, removeIndex, count;

    public BoundedQueue(int size) {
        items = (T[]) new Object[size];
    }

    /**
     * 添加一个元素，如果数组满，则添加线程进入等待状态，直到有“空位”
     */
    public void add(T item) throws InterruptedException {
        lock.lock();
        try {
            while (addIndex == items.length) {
                //满了,阻塞
                waiting.await();//将当前线程加入队列
            }
            items[addIndex] = item;
            ++count;//计数加一
            available.signal();//获取列表可以拿
        } finally {
            lock.unlock();
        }
    }

    /**
     * 由头部删除一个元素，如果数组空，则删除线程进入等待状态，直到有新添加元素
     */
    public T remove() throws InterruptedException {
        lock.lock();
        try {
            while (count == 0) {
                //如果没队列中没有数据,将线程阻塞并加入队列
                available.await();
            }
            T item = items[removeIndex];
            if (++removeIndex == items.length) {
                removeIndex = 0;
            }
            --count;
            waiting.signal();
            return item;
        } finally {
            lock.unlock();
        }
    }


}
