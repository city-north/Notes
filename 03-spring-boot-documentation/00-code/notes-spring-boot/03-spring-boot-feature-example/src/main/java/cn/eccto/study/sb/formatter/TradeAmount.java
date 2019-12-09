package cn.eccto.study.sb.formatter;

import java.math.BigDecimal;
import java.util.Currency;

/**
 * description
 *
 * @author EricChen 2019/12/09 22:20
 */
public class TradeAmount {
    private Currency currency;
    private BigDecimal amount;

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
