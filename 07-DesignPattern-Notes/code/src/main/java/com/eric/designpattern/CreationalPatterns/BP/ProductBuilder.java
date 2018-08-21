package com.eric.designpattern.CreationalPatterns.BP;

import java.util.Date;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public abstract class ProductBuilder<T extends Product> implements AbstractBuilder<T> {

    protected T product;

    public ProductBuilder(Class<T> clazz) {
        try {
            product = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public ProductBuilder name(String name) {
        product.setName(name);
        return this;
    }

    public ProductBuilder price(double price) {
        product.setPrice(price);
        return this;
    }

    public ProductBuilder brand(String brand) {
        product.setBrand(brand);
        return this;
    }

    public ProductBuilder productionDate(Date productionDate) {
        product.setProductionDate(productionDate);
        return this;
    }

    public T build() {
        return product;
    }
}
