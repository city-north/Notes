package com.eric.designpattern.CreationalPatterns.AFP.factory;

import com.eric.designpattern.CreationalPatterns.AFP.charger.Charger;
import com.eric.designpattern.CreationalPatterns.AFP.charger.DellCharger;
import com.eric.designpattern.CreationalPatterns.AFP.keybo.DellKeybo;
import com.eric.designpattern.CreationalPatterns.AFP.keybo.Keybo;
import com.eric.designpattern.CreationalPatterns.AFP.mouse.DellMouse;
import com.eric.designpattern.CreationalPatterns.AFP.mouse.Mouse;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class DellFactory extends PcFactory {
    public Mouse createMouse() {
        return new DellMouse();
    }

    public Keybo createKeybo() {
        return new DellKeybo();
    }
    //可以选择支持充电器或不支持充电器
    @Override
    public Charger createCharger() {
        return new DellCharger();
    }
}
