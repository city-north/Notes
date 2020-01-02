package vip.ericchen.study.designpatterns.behavioral.strategy;

/**
 * description
 *
 * @author EricChen 2020/01/02 22:17
 */
public class WechatPayStrategy implements PayStrategy {

    @Override
    public void pay(String account, int paymentAmount) {
        System.out.println("使用微信支付付款");
        System.out.println("账户:" + account + " ,支付金额:" + paymentAmount);
    }
}
