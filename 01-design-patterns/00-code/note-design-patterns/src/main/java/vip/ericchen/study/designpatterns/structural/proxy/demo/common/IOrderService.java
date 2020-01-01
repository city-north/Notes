package vip.ericchen.study.designpatterns.structural.proxy.demo.common;

import vip.ericchen.study.designpatterns.structural.proxy.demo.jdkdynamic.JdkOrderServiceProxy;
import vip.ericchen.study.designpatterns.structural.proxy.demo.staticproxy.OrderServiceStaticProxy;

/**
 * 案例: 使用不同方式代理 {@link IOrderService} 的默认实现类,使其支持多数据源
 *
 * @author EricChen 2020/01/01 15:03
 * @see OrderServiceImpl 默认的实现
 * @see OrderServiceStaticProxy 静态代理的实现方式
 * @see JdkOrderServiceProxy JDK 代理的方式
 * @see
 */
public interface IOrderService {

    /**
     * 创建订单
     */
    void createOrder();

    /**
     * 设置数据源
     *
     * @param dataSource 设置数据源
     */
    void setDataSource(String dataSource);

}
