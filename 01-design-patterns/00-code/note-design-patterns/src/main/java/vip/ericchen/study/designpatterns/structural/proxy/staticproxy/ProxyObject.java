package vip.ericchen.study.designpatterns.structural.proxy.staticproxy;

/**
 * 代理对象
 *
 * @author EricChen 2020/01/01 11:58
 */
public class ProxyObject implements Action {
    private RealObject realObject;

    public ProxyObject(RealObject realObject) {
        this.realObject = realObject;
    }

    @Override
    public void doSomething() {
        enhance();
        realObject.doSomething();
    }

    private void enhance() {
        System.out.println("Proxy object is enhancing ");
    }

}
