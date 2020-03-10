package cn.eccto.study.java.concurrent.condition;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * {@link Condition} 实例,任务队列
 * </p>
 *
 * @author EricChen 2020/03/10 15:48
 */
public class TaskQueue {
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();

    private Queue<String> taskQueue = new LinkedList<>();


    public String getTask() {
        lock.lock();
        try {
            while (taskQueue.isEmpty()) {
                condition.await();
            }
            return taskQueue.remove();
        } catch (InterruptedException e) {
            return null;
        } finally {
            lock.unlock();
        }
    }

    public void addTask(String task) {
        lock.lock();
        try {
            taskQueue.add(task);
            condition.signalAll();
        } finally {
            lock.lock();
        }
    }
}
