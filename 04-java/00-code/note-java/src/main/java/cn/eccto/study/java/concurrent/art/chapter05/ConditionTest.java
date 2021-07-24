package cn.eccto.study.java.concurrent.art.chapter05;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * <p>
 * description
 * </p>
 *
 * @author Jonathan 2020/04/04 22:37
 */
public class ConditionTest {
    public static void main(String[] args) throws Exception {
        Lock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        new Thread(new ConditionWait(lock, condition)).start();
        new Thread(new ConditionNotify(lock, condition)).start();
        Thread.sleep(10000);
    }
}
