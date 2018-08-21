package com.eric.designpattern.CreationalPatterns.AFP.factory;

import com.eric.designpattern.CreationalPatterns.AFP.charger.Charger;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public abstract class PcFactory implements AbstractFactory {

    //拓展一个充电器产品
    public Charger createCharger() {
        //默认使用Hp充电器
        return new HpFactory().createCharger();
    }
}
