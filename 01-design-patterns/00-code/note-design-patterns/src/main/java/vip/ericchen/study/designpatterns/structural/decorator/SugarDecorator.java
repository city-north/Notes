package vip.ericchen.study.designpatterns.structural.decorator;

/**
 * 糖装饰器
 *
 * @author EricChen 2020/01/04 13:36
 */
public class SugarDecorator implements Goods {
    private Goods goods;

    public SugarDecorator(Goods goods) {
        this.goods = goods;
    }

    @Override
    public String getName() {
        return goods.getName() + "加1份糖";
    }

    @Override
    public Double getPrice() {
        return goods.getPrice() + 2.0;
    }
}
