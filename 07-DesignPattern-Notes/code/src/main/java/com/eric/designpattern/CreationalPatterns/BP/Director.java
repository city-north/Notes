package com.eric.designpattern.CreationalPatterns.BP;

import java.util.Date;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class Director {


    //指挥装机人员组装电脑
    public Milk buildMilk(MilkBuilder milkBuilder){
        return (Milk) milkBuilder.name("蒙牛早餐奶").brand("蒙牛").productionDate(new Date()).price(12.3).build();
    }

    public Iphone buildIphone(IphoneBuilder productBuilder){
         return productBuilder.brand("Apple").name("iphone8").productionDate(new Date()).price(5699).series("8").build();
    }

}
