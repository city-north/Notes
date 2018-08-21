package com.eric.designpattern.StructuralPatterns.DP;

import com.eric.designpattern.StructuralPatterns.DP.beverage.Beverage;
import com.eric.designpattern.StructuralPatterns.DP.beverage.Espresso;
import com.eric.designpattern.StructuralPatterns.DP.condiment.Milk;
import com.eric.designpattern.StructuralPatterns.DP.condiment.Mocha;
import com.eric.designpattern.StructuralPatterns.DP.condiment.Whip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class Test {
    public static void main(String[] args) {
        Beverage beverage = new Espresso();
        beverage = new Milk(beverage);
        beverage = new Whip(beverage);
        beverage = new Mocha(beverage);
        System.out.println("Description:" + beverage.getDescription());
        System.out.println("Cost:" + beverage.cost());

        List<Beverage> beverages = Collections.synchronizedList(new ArrayList<Beverage>());
    }
}
