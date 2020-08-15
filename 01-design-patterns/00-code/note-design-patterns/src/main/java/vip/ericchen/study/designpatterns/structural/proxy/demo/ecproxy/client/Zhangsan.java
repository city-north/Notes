package vip.ericchen.study.designpatterns.structural.proxy.demo.ecproxy.client;

/**
 * Created by Tom.
 */
public class Zhangsan implements IPerson {

    @Override
    public void findLove() {
        System.out.println("张三要求：肤白貌美大长腿");
    }
    @Override
    public void buyInsure() {
        System.out.println("30万");
    }

}
