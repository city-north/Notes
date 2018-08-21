package com.eric.designpattern.StructuralPatterns.DP.beverage;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public abstract class Beverage {
    String description = "unknown Beverage!";
    public abstract double cost();

    public String getDescription() {
        return description;
    }
}
