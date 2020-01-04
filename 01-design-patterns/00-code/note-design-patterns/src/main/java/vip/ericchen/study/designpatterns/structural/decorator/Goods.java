package vip.ericchen.study.designpatterns.structural.decorator;

/**
 * description
 *
 * @author EricChen 2020/01/04 13:35
 */
public interface Goods {
    /**
     * 商品都有名称
     */
    String getName();

    /**
     * 商品都有价格
     */
    Double getPrice();
}
