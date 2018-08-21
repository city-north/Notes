package com.eric.designpattern.CreationalPatterns.BP;

import java.util.Date;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class IphoneBuilder extends ProductBuilder<Iphone> {

    public IphoneBuilder() {
        super(Iphone.class);
    }

    public IphoneBuilder series(String series){
        product.setSeries(series);
        return this;
    }

    @Override
    public IphoneBuilder name(String name) {
        product.setName(name);
        return this;
    }

    @Override
    public IphoneBuilder price(double price) {
        product.setPrice(price);
        return this;
    }

    @Override
    public IphoneBuilder brand(String brand) {
        product.setBrand(brand);
        return this;
    }

    @Override
    public IphoneBuilder productionDate(Date productionDate) {
        product.setProductionDate(productionDate);
        return this;
    }

    @Override
    public Iphone build() {
        return super.build();
    }
}
