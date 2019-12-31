package vip.ericchen.study.designpatterns.creational.singletonPattern.enu;

import vip.ericchen.study.designpatterns.commons.MyThreadPoolExecutor;

/**
 * description
 *
 * @author EricChen 2019/12/31 23:58
 */
public class EnumSingletonTest {

    public static void main(String[] args) {
        MyThreadPoolExecutor.execute(() -> {
            System.out.println(Thread.currentThread().getName() + "-->" + EnumSingleton.INSTANCE.getInstance());
        });
    }
}
