package cn.eccto.study.sb.formatter;

import org.springframework.format.Formatter;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 自定义 Formatter
 *
 * @author EricChen 2019/12/09 22:20
 */
public class TradeAmountFormatter implements Formatter<TradeAmount> {
    private static final Pattern amountPattern = Pattern.compile("(\\d+)(\\w+)");

    @Override
    public TradeAmount parse(String text, Locale locale) throws ParseException {
        Matcher matcher = amountPattern.matcher(text);
        if (matcher.find()) {
            try {
                TradeAmount ta = new TradeAmount();
                ta.setAmount(new BigDecimal(Integer.parseInt(matcher.group(1))));
                ta.setCurrency(Currency.getInstance(matcher.group(2)));
                return ta;
            } catch (Exception e) {
            }
        }
        return new TradeAmount();
    }

    @Override
    public String print(TradeAmount tradeAmount, Locale locale) {
        return tradeAmount.getAmount().toPlainString() + tradeAmount.getCurrency().getSymbol(locale);
    }
}
