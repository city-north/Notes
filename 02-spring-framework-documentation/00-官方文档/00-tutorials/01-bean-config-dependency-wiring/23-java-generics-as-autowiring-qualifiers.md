# Java 泛型(Generics) 注入

> 版权声明
>
> 此篇文章参考于[logicbig](https://www.logicbig.com/),引用其文字/代码/图片仅用于学习笔记,不用于商业用途

Spring 4.1 可以使用 Java 泛型作为依赖注入的隐式条件,我们不需要使用`@Qualifier`

## 示例

配置文件

```java
@Configuration
public class Config {

    @Bean
    private RateFormatter<Integer> integerRateFormatter() {
        return new RateFormatter<Integer>();
    }

    @Bean
    private RateFormatter<BigDecimal> bigDecimalRateFormatter() {
        return new RateFormatter<BigDecimal>();
    }

    @Bean
    private RateCalculator rateCalculator() {
        return new RateCalculator();
    }

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(Config.class);
        RateCalculator bean = context.getBean(RateCalculator.class);
        bean.calculate();
    }
}
```

注入类型包含泛型

```java
public class RateCalculator {

    @Autowired
    private RateFormatter<BigDecimal> formatter;

    public void calculate() {
        BigDecimal rate = new BigDecimal(Math.random() * 100);
        System.out.println(formatter.format(rate));
    }
}
```

```java
public class RateFormatter<T extends Number> {

    public String format(T number){
        NumberFormat format = NumberFormat.getInstance();
        if(number instanceof Integer){
            format.setMinimumIntegerDigits(0);
        }else if(number instanceof BigDecimal){
            format.setMinimumIntegerDigits(2);
            format.setMaximumFractionDigits(2);
        }//others

        return format.format(number);
    }
}
```

