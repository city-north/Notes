package vip.ericchen.study.designpatterns.creational.singletonPattern.hungry;

import vip.ericchen.study.designpatterns.commons.MyThreadPoolExecutor;

/**
 * description
 *
 * @author EricChen 2019/12/31 23:25
 */
public class HungryInitSingletonExample {

    public static void main(String[] args) {
        tryHungrySingleton();//线程安全
//        tryHungryStaticSingleton();//线程安全
    }

    private static void tryHungrySingleton() {
        MyThreadPoolExecutor.execute(() -> {
            System.out.println(Thread.currentThread().getName() + "-->" + HungrySingleton.getInstance());
        });
    }

    private static void tryHungryStaticSingleton() {
        MyThreadPoolExecutor.execute(() -> {
            System.out.println(Thread.currentThread().getName() + "-->" + HungryStaticSingleton.getInstance());
        });
    }
}
