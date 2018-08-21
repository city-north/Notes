package com.eric.designpattern.StructuralPatterns.DP.condiment;

import com.eric.designpattern.StructuralPatterns.DP.beverage.Beverage;
import com.eric.designpattern.StructuralPatterns.DP.beverage.CondimentDecorator;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class Mocha extends CondimentDecorator {
    Beverage beverage;
    private static final double price = 1;
    public Mocha(Beverage beverage) {
        this.beverage = beverage;
    }

    public double cost() {
        return beverage.cost()+price;
    }

    public String getDescription() {
        return beverage.getDescription()+",MoCha";
    }
}
