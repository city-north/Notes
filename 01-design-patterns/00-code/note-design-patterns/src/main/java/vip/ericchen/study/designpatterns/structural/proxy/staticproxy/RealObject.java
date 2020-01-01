package vip.ericchen.study.designpatterns.structural.proxy.staticproxy;

/**
 * 真实对象,也就是被代理的对象
 *
 * @author EricChen 2020/01/01 11:57
 */
public class RealObject implements Action {

    @Override
    public void doSomething() {
        System.out.println("The real object is doing");
    }
}
