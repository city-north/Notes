package vip.ericchen.study.designpatterns.structural.proxy.demo.staticproxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.ericchen.study.designpatterns.structural.proxy.demo.common.IOrderService;


/**
 * 通过静态代理的方式切换数据源
 *
 * @author EricChen 2020/01/01 15:07
 */
public class OrderServiceStaticProxy implements IOrderService {

    private String dataSource;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderServiceStaticProxy.class);

    private IOrderService orderServiceProxy;

    public OrderServiceStaticProxy(IOrderService proxyTarget) {
        this.orderServiceProxy = proxyTarget;
    }

    @Override
    public void createOrder() {
        long currentTimeMillis = System.currentTimeMillis();
        String s = String.valueOf(currentTimeMillis);
        String substring = s.substring(s.length() - 1);
        LOGGER.debug("当前时间为{},切换数据源为{}", s, substring);
        orderServiceProxy.setDataSource(substring);
        orderServiceProxy.createOrder();
    }

    @Override
    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }
}
