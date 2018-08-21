package com.eric.designpattern.StructuralPatterns.DP.beverage;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class HouseBlend extends Beverage{

    public HouseBlend() {
        this.description = "HouseBlend";
    }

    @Override
    public double cost() {
        return .89;
    }

}
