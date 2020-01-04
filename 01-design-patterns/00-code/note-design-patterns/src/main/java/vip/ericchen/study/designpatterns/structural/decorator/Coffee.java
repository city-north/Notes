package vip.ericchen.study.designpatterns.structural.decorator;

/**
 * description
 *
 * @author EricChen 2020/01/04 13:40
 */
public class Coffee implements Goods {

    @Override
    public String getName() {
        return "咖啡1杯";
    }

    @Override
    public Double getPrice() {
        return 30D;
    }
}
