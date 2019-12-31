package vip.ericchen.study.designpatterns.creational.factoryMethod;

import vip.ericchen.study.designpatterns.commons.Product;
import vip.ericchen.study.designpatterns.creational.simpleFactory.SimpleFactoryExample;

/**
 * 工厂方法实例
 *
 * @author EricChen 2019/12/31 21:09
 */
public class FactoryMethodExample {


    public static void main(String[] args) {
        SimpleFactoryExample.doWithoutPatterns();//不使用
        doWithFactoryMethod();
    }

    private static void doWithFactoryMethod() {
        IphoneFactory iphoneFactory = new IphoneFactory();
        Product product = iphoneFactory.create();
        System.out.println("使用工厂方法创建一个对象" + product);
    }

}
