package vip.ericchen.study.designpatterns.behavioral.strategy;

import java.util.function.DoubleToIntFunction;

/**
 * description
 *
 * @author EricChen 2020/01/02 22:15
 */
public class AlipayStrategy implements PayStrategy {

    @Override
    public void pay(String account, int paymentAmount) {
        System.out.println("使用支付宝付款");
        System.out.println("账户:" + account + " ,支付金额:" + paymentAmount);
    }
}
