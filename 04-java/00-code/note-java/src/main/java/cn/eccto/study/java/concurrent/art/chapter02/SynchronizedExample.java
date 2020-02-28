package cn.eccto.study.java.concurrent.art.chapter02;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/02/27 19:17
 */
public class SynchronizedExample {


    /**
     * 普通同步方法,锁是当前实例对象
     */
    public synchronized void test1() {

    }

    /**
     * 静态同步方法,锁是当前类的 Class对象
     */
    public static synchronized void test2() {

    }

    /**
     * 对于同步代码块,锁是 synchronized 括号内配置的对象
     */
    public void test3() {
        synchronized (this) {

        }
    }


}
