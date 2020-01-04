package vip.ericchen.study.designpatterns.structural.decorator;

/**
 * 奶装饰器
 *
 * @author EricChen 2020/01/04 13:36
 */
public class MilkDecorator implements Goods {
    private Goods goods;

    public MilkDecorator(Goods goods) {
        this.goods = goods;
    }

    @Override
    public String getName() {
        return goods.getName() + "加1份奶";
    }

    @Override
    public Double getPrice() {
        return goods.getPrice() + 5.0;
    }
}
