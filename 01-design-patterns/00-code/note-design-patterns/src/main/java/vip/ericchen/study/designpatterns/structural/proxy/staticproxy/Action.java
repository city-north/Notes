package vip.ericchen.study.designpatterns.structural.proxy.staticproxy;

/**
 * 代理对象和真实对象都实现的共同接口
 *
 * @author EricChen 2020/01/01 11:56
 */
public interface Action {
    /**
     * 对象的行为
     */
    void doSomething();
}
