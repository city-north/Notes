package vip.ericchen.study.designpatterns.creational.singletonPattern.inner;

import vip.ericchen.study.designpatterns.commons.MyThreadPoolExecutor;

/**
 * description
 *
 * @author EricChen 2019/12/31 23:48
 */
public class InnerClassSingletonExample {
    public static void main(String[] args) {
        tryHungrySingleton();//线程安全
    }

    private static void tryHungrySingleton() {
        MyThreadPoolExecutor.execute(() -> {
            System.out.println(Thread.currentThread().getName() + "-->" + InnerClassSingleton.getInstance());
        });
    }
}
