package vip.ericchen.study.designpatterns.creational.singletonPattern.lazy;

import vip.ericchen.study.designpatterns.commons.Singleton;

/**
 * 懒加载单例模式(线程安全)
 *
 * @author EricChen 2019/12/31 22:39
 */
public class LazyInitThreadSafeSyncSingleton {
    private static Singleton INSTANCE = null;

    private LazyInitThreadSafeSyncSingleton() {
        //防止反射攻击
        throw new IllegalArgumentException("HungrySingleton not allow be constructed");
    }

    /**
     * 使用 synchronized 关键字,效率低,锁掉整个类
     */
    public static synchronized Singleton getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Singleton();
        }
        return INSTANCE;
    }

    /**
     * 重写 readResolve 方法`java.io.ObjectStreamClass` 类在反序列初始化对象时会去判断这个方法
     * 还是会创建两次,但是发生在 JVM 层面,相对安全,之前反序列化的对象也会被 GC 回收
     *
     * @see java.io.ObjectStreamClass#invokeReadResolve
     */
    private Object readResolve() {
        return INSTANCE;
    }

}
