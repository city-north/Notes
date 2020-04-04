package cn.eccto.study.java.concurrent.art.chapter05;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/04/04 22:33
 */
public class ConditionNotify extends Thread {

    private Lock lock;
    private Condition condition;

    public ConditionNotify(Lock lock, Condition condition) {
        this.lock = lock;
        this.condition = condition;
    }

    @Override
    public void run() {
        lock.lock();
        try {
            System.out.println("notify-start");
            condition.signal();
            System.out.println("notify-end");
        } catch (Exception e) {
        } finally {
            lock.unlock();
        }

    }
}
