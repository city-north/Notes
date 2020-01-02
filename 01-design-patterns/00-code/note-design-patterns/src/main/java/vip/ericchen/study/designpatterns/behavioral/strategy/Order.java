package vip.ericchen.study.designpatterns.behavioral.strategy;

import lombok.Builder;
import lombok.Data;

/**
 * description
 *
 * @author EricChen 2020/01/02 22:19
 */
@Data
@Builder
public class Order {
    private String account;
    private int totalCost = 0;
    private boolean isClosed = false;

    public void processOrder(PayStrategy strategy) {
        strategy.pay(account, totalCost);
    }
}
