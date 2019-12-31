package vip.ericchen.study.designpatterns.creational.simpleFactory;

import vip.ericchen.study.designpatterns.commons.Iphone;
import vip.ericchen.study.designpatterns.commons.Product;

/**
 * 简单工厂模式代码示例
 *
 * @author EricChen 2019/12/31 21:06
 */
public class SimpleFactoryExample {

    public static void main(String[] args) {
        doWithoutPatterns();
        doWithPatterns();
        doWithPatterns2();
        doWithPatterns3();
    }


    public static void doWithoutPatterns() {
        Iphone iphone = new Iphone();
        System.out.println("最简单的构建了一个手机" + iphone);
    }

    private static void doWithPatterns() {
        SimpleProductFactory simpleProductFactory = new SimpleProductFactory();
        Product product = simpleProductFactory.create(SimpleProductFactory.IPHONE);
        System.out.println("通过工厂方法构建了一个手机" + product);
    }

    private static void doWithPatterns2(){
        SimpleProductFactory simpleProductFactory = new SimpleProductFactory();
        Product withPath = simpleProductFactory.createWithPath("vip.ericchen.study.designpatterns.commons.Iphone");
        System.out.println("通过工厂方法创建一个一个手机"+withPath);
    }

    private static void doWithPatterns3(){
        SimpleProductFactory simpleProductFactory = new SimpleProductFactory();
        Product withPath = simpleProductFactory.create(Iphone.class);
        System.out.println("通过工厂方法创建一个一个手机"+withPath);
    }

}
