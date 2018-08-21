package com.eric.designpattern.StructuralPatterns.DP.beverage;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class Espresso extends Beverage {

    public Espresso() {
        this.description = "Espresso";
    }

    @Override
    public double cost() {
        return 10;
    }
}
