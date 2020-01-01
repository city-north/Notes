package vip.ericchen.study.designpatterns.structural.proxy.demo.common;


/**
 * 默认的订单实现,被代理对象
 *
 * @author EricChen 2020/01/01 15:04
 */
public class OrderServiceImpl implements IOrderService {

    private String dataSource;

    @Override
    public void createOrder() {
        System.out.println("创建订单,使用数据源[" + dataSource + "]");

    }

    @Override
    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
}
