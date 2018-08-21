package com.eric.designpattern.CreationalPatterns.AFP.expand;

import com.eric.designpattern.CreationalPatterns.AFP.factory.PcFactory;
import com.eric.designpattern.CreationalPatterns.AFP.keybo.Keybo;
import com.eric.designpattern.CreationalPatterns.AFP.mouse.Mouse;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class AsusFactory extends PcFactory {
    public Mouse createMouse() {
        return new AsusMouse();
    }

    public Keybo createKeybo() {
        return new AsusKeybo();
    }
}
