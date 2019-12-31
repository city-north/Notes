package vip.ericchen.study.designpatterns.creational.singletonPattern.lazy;


import vip.ericchen.study.designpatterns.commons.MyThreadPoolExecutor;


/**
 * description
 *
 * @author EricChen 2019/12/31 22:59
 */
public class LazyInitSingletonExample {

    public static void main(String[] args) {
        tryNotSafeLazyInitSingleton(); //线程不安全
        System.out.println("----");
        tryLazyInitThreadNotSafeSingleton();//线程安全
    }

    private static void tryNotSafeLazyInitSingleton() {
        MyThreadPoolExecutor.execute(() -> {
            System.out.println(Thread.currentThread().getName() + "-->" + LazyInitThreadNotSafeSingleton.getInstance());
        });
    }

    private static void tryLazyInitThreadNotSafeSingleton() {
        MyThreadPoolExecutor.execute(() -> {
            System.out.println(Thread.currentThread().getName() + "-->" + LazyInitThreadSafeSingleton.getInstance());
        });
    }

}
