package com.eric.designpattern.CreationalPatterns.AFP;

import com.eric.designpattern.CreationalPatterns.AFP.charger.Charger;
import com.eric.designpattern.CreationalPatterns.AFP.expand.AsusFactory;
import com.eric.designpattern.CreationalPatterns.AFP.factory.AbstractFactory;
import com.eric.designpattern.CreationalPatterns.AFP.factory.DellFactory;
import com.eric.designpattern.CreationalPatterns.AFP.factory.PcFactory;
import com.eric.designpattern.CreationalPatterns.AFP.keybo.Keybo;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class Test {
    public static void main(String[] args) {
        PcFactory factory = new DellFactory();
        Keybo dellKeybo = factory.createKeybo();
        dellKeybo.use();

        //拓展一个新工厂
        PcFactory asusFactory = new AsusFactory();
        Keybo asusKeybo = asusFactory.createKeybo();
        asusKeybo.use();



        //拓展一个商品，比如充电器
        PcFactory asusFactory1 = new AsusFactory();
        Charger charger = asusFactory1.createCharger();
        charger.use();//因为华硕工厂没有自定义充电器生产线，使用的是默认的，惠普的充电器
        PcFactory dellFactory2 = new DellFactory();
        Charger charger1 = dellFactory2.createCharger();
        charger1.use();
    }
}
