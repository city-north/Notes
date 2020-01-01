package vip.ericchen.study.designpatterns.structural.proxy.demo;

import vip.ericchen.study.designpatterns.commons.MyThreadPoolExecutor;
import vip.ericchen.study.designpatterns.structural.proxy.demo.cglib.CglibOrderServiceProxy;
import vip.ericchen.study.designpatterns.structural.proxy.demo.common.IOrderService;
import vip.ericchen.study.designpatterns.structural.proxy.demo.common.OrderServiceImpl;
import vip.ericchen.study.designpatterns.structural.proxy.demo.jdkdynamic.JdkOrderServiceProxy;
import vip.ericchen.study.designpatterns.structural.proxy.demo.staticproxy.OrderServiceStaticProxy;

/**
 * description
 *
 * @author EricChen 2020/01/01 15:18
 */
public class OrderServiceExample {

    public static void main(String[] args) {
//        doWithStaticProxy();
//        doWithJdkDynamicProxy();//jdk 动态代理
        doWithCglibProxy();//cglib 代理
    }


    private static void doWithStaticProxy() {
        OrderServiceStaticProxy orderServiceStaticProxy = new OrderServiceStaticProxy(new OrderServiceImpl());
        MyThreadPoolExecutor.execute(orderServiceStaticProxy::createOrder);
    }

    private static void doWithJdkDynamicProxy() {
        JdkOrderServiceProxy jdkOrderServiceProxy = new JdkOrderServiceProxy(new OrderServiceImpl());
        IOrderService proxy = jdkOrderServiceProxy.getProxy();
        MyThreadPoolExecutor.execute(proxy::createOrder);
//        MyThreadPoolExecutor.execute(()->proxy.setDataSource("123"));
    }


    private static void doWithCglibProxy() {
        OrderServiceImpl instance = new CglibOrderServiceProxy().getInstance(OrderServiceImpl.class);
        MyThreadPoolExecutor.execute(instance::createOrder);
    }

}
