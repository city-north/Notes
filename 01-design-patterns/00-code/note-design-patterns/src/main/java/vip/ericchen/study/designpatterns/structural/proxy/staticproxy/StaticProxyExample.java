package vip.ericchen.study.designpatterns.structural.proxy.staticproxy;

/**
 * 静态代理测试
 *
 * @author EricChen 2020/01/01 12:01
 */
public class StaticProxyExample {

    public static void main(String[] args) {
        doWithStaticProxy();
    }


    private static void doWithStaticProxy() {
        ProxyObject proxyObject = new ProxyObject(new RealObject());
        proxyObject.doSomething();
    }
}
