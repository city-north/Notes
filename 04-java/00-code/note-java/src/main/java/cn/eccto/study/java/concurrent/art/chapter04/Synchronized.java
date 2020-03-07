package cn.eccto.study.java.concurrent.art.chapter04;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/03/07 16:58
 */
public class Synchronized {
    public static void main(String[] args) {
        // 对Synchronized Class对象进行加锁
        synchronized (Synchronized.class) {

        }
        // 静态同步方法，对Synchronized Class对象进行加锁
        m();
    }

    public static synchronized void m() {
    }
}
