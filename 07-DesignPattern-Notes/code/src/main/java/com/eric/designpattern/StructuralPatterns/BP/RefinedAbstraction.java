package com.eric.designpattern.StructuralPatterns.BP;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class RefinedAbstraction extends Abstraction {
    @Override
    protected void operation() {
        super.getImplementor().operation();
    }
}
