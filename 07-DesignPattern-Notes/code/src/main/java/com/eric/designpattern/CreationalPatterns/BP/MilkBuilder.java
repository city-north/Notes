package com.eric.designpattern.CreationalPatterns.BP;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class MilkBuilder extends ProductBuilder<Milk>{

    public MilkBuilder() {
        super(Milk.class);
    }

    public MilkBuilder flavor(String flavor){
        product.setFlavor(flavor);
        return this;
    }

    @Override
    public Milk build() {
        return super.build();
    }
}
