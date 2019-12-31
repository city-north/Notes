package vip.ericchen.study.designpatterns.creational.factoryMethod;

import vip.ericchen.study.designpatterns.commons.Iphone;
import vip.ericchen.study.designpatterns.commons.Product;

/**
 * factory method factory
 *
 * @author EricChen 2019/12/31 21:59
 */
class IphoneFactory implements IProductFactory {

    public Product create() {
        return new Iphone();
    }
}
