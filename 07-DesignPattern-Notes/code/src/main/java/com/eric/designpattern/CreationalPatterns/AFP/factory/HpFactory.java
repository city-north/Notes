package com.eric.designpattern.CreationalPatterns.AFP.factory;

import com.eric.designpattern.CreationalPatterns.AFP.charger.Charger;
import com.eric.designpattern.CreationalPatterns.AFP.charger.HpCharger;
import com.eric.designpattern.CreationalPatterns.AFP.keybo.HpKeybo;
import com.eric.designpattern.CreationalPatterns.AFP.keybo.Keybo;
import com.eric.designpattern.CreationalPatterns.AFP.mouse.HpMouse;
import com.eric.designpattern.CreationalPatterns.AFP.mouse.Mouse;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class HpFactory extends PcFactory {
    public Mouse createMouse() {
        return new HpMouse();
    }

    public Keybo createKeybo() {
        return new HpKeybo();
    }

    public Charger createCharger(){
        return new HpCharger();
    }
}
