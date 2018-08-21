package chapter02.proxy;

import chapter02.proxy.jdkProxy.JDKProxy;
import chapter02.proxy.staticProxy.Action;
import chapter02.proxy.staticProxy.RealObject;


/**
 * 代理模式
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class Question {

    public static void main(String[] args) {
        //首先是静态代理
        //静态代理是三要素：共同接口，真实对象，代理对象
        //分别：共同接口：Action,真实对象RealObject，代理对象ProxyObject
        RealObject realObject = new RealObject();
//        ProxyObject proxyObject = new ProxyObject(realObject);
//        proxyObject.doSomething();
        JDKProxy jdkProxy = new JDKProxy();
        Action action = (Action) jdkProxy.newProxy(realObject);
        action.doSomething();

    }
}
