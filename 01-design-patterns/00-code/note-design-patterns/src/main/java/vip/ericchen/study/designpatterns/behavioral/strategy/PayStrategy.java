package vip.ericchen.study.designpatterns.behavioral.strategy;

/**
 * 支付策略
 *
 * @author EricChen 2020/01/02 22:01
 */
public interface PayStrategy {
    /**
     * 支付
     *
     * @param account       支付账号
     * @param paymentAmount 支付金额
     */
    void pay(String account, int paymentAmount);


}