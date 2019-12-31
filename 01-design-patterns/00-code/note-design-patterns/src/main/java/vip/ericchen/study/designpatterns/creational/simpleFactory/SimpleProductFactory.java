package vip.ericchen.study.designpatterns.creational.simpleFactory;

import vip.ericchen.study.designpatterns.commons.Iphone;
import vip.ericchen.study.designpatterns.commons.Product;

/**
 * Simple factory example
 *
 * @author EricChen 2019/12/31 21:37
 */
class SimpleProductFactory {
    public static final String IPHONE = "iphone";

    public Product create(String type) {
        if (IPHONE.equalsIgnoreCase(type)) {
            return new Iphone();
        }
        return null;
    }

    /**
     * create instance by full-qualified name
     *
     * @param path full qualified name
     * @return the instance of Product
     */
    public Product createWithPath(String path) {
        try {
            if (path != null && !"".equals(path)) {
                return (Product) Class.forName(path).newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * create instance by Class
     */
    public Product create(Class clazz) {
        try {
            if (clazz != null) {
                return (Product) clazz.newInstance();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
