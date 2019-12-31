package vip.ericchen.study.designpatterns.creational.singletonPattern.enu;

import vip.ericchen.study.designpatterns.commons.Singleton;

/**
 * 枚举方式单例
 *
 * @author EricChen 2019/12/31 23:58
 */
public enum EnumSingleton {
    INSTANCE;
    private Singleton SINGLETON_INSTANCE = new Singleton();

    public Singleton getInstance() {
        return SINGLETON_INSTANCE;
    }
}
