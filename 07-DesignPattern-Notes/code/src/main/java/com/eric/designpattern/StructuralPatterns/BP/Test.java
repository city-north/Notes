package com.eric.designpattern.StructuralPatterns.BP;

/**
 * 桥接模式测试
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class Test {

    public static void main(String[] args) {
        Abstraction abstraction1 = new RefinedAbstraction();
        abstraction1.setImplementor(new ConcreteImplementorA());
        abstraction1.operation();

        Abstraction abstraction2 = new RefinedAbstraction();
        abstraction2.setImplementor(new ConcreteImplementorB());
        abstraction2.operation();
    }
}
