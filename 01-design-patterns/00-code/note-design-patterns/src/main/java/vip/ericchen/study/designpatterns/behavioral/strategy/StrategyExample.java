package vip.ericchen.study.designpatterns.behavioral.strategy;

/**
 * 策略模式测试代码
 *
 * @author EricChen 2020/01/02 22:23
 * @see Order
 * @see PayStrategy
 * @see AlipayStrategy
 * @see WechatPayStrategy
 * @see PayPalStrategy
 */
public class StrategyExample {


    public static void main(String[] args) {
        Order order = Order.builder()
                .account("EricChen")
                .isClosed(false)
                .totalCost(100)
                .build();

        order.processOrder(new AlipayStrategy());
    }

}
