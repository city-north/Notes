# Registering a Custom Formatter in Spring Boot

注册一个自定义的 formatter



```java
/**
 * 自定义 {@link Formatter} 并注册
 *
 * @author EricChen 2019/12/09 22:22
 */
@SpringBootApplication
public class TradeAmountExample {
    public static void main(String[] args) {
        SpringApplication.run(TradeAmountExample.class, args);

    }

    @Configuration
    static class MyConfig implements WebMvcConfigurer {
        @Override
        public void addFormatters(FormatterRegistry registry) {
            registry.addFormatter(new TradeAmountFormatter());
        }
    }
}

```

#### TradeAmountFormatter

```java
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
```

#### TradeAmount

```java
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
```

```java
@Controller
@RequestMapping("/")
public class TradeController {
    @GetMapping("/trade")
    @ResponseBody
    public String handleRequest(@RequestParam TradeAmount amount) {
        return "Request param: " + amount;
    }
}

```

