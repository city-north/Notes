package com.eric.designpattern.CreationalPatterns.AFP.factory;

import com.eric.designpattern.CreationalPatterns.AFP.keybo.Keybo;
import com.eric.designpattern.CreationalPatterns.AFP.mouse.Mouse;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public interface AbstractFactory {
    Mouse createMouse();
    Keybo createKeybo();
}
