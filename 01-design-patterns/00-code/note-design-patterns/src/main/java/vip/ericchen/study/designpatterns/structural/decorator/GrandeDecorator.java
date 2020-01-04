package vip.ericchen.study.designpatterns.structural.decorator;

/**
 * 大杯装饰器
 *
 * @author EricChen 2020/01/04 13:36
 */
public class GrandeDecorator implements Goods {
    private Goods goods;

    public GrandeDecorator(Goods goods) {
        this.goods = goods;
    }

    @Override
    public String getName() {
        return goods.getName() + "升大杯";
    }

    @Override
    public Double getPrice() {
        return goods.getPrice() + 5.0;
    }
}
