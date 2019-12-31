# 基于注解的字段格式化

-  [05-spring-field-formatting.md](../../03-validation-data-binding-type-conversion/05-spring-field-formatting.md)  属性格式化
-  [05-spring-field-formatting(anno).md](../../03-validation-data-binding-type-conversion/05-spring-field-formatting(anno).md) 基于注解的属性格式化

Spring 提供了一个声明式配置方法,通过注解,指定字段的格式化规则

## 使用 Spring 提供的注解

spring 提供的格式化注解支持在包[org.springframework.format.annotation](http://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/format/annotation/package-summary.html)

下面是使用方法:

#### 使用格式化相关联的注解去创建一个 bean

```java
public class Order{
    @NumberFormat(style = NumberFormat.Style.PERCENT)
    private Double price;

    @DateTimeFormat(pattern = "yyyy")
    private Date date;

   //getters and setters and toString() methods

}
```

#### 使用 DataBinder 来填充 bean 字段

- 创建一个`ConversionService`
- 使用`AnnotationFormatterFactory`实例绑定`DateTimeFormat`和`NumberFormat`
- 使用 DataBinder 去设置目标 bean 的字段

订单测试类,注意注解

```java
public class Order{
    @NumberFormat(style = NumberFormat.Style.PERCENT)
    private Double price;

    @DateTimeFormat(pattern = "yyyy")
    private Date date;
}
```

例子

```java
public class SpringFormatAnnotationExample {
    public static void main(String[] args) {
        DefaultFormattingConversionService conversionService =
                new DefaultFormattingConversionService(false);
        conversionService.addFormatterForFieldAnnotation(new NumberFormatAnnotationFormatterFactory());
        conversionService.addFormatterForFieldAnnotation(new DateTimeFormatAnnotationFormatterFactory());
        Order order = new Order();
        DataBinder dataBinder = new DataBinder(order);
        dataBinder.setConversionService(conversionService);
        MutablePropertyValues mpv = new MutablePropertyValues();
        mpv.add("price", "2.7%");
        mpv.add("date", "2016");

        dataBinder.bind(mpv);
        dataBinder.getBindingResult()
                .getModel()
                .entrySet()
                .forEach(System.out::println);
        System.out.println("设置后的 date 字段" + order.getDate());
        System.out.println("设置后的 price 字段" + order.getPrice());

    }

}
```

输出

```java
target=cn.eccto.study.spring_framework.formatter.Order@6a472554
org.springframework.validation.BindingResult.target=org.springframework.validation.BeanPropertyBindingResult: 0 errors
设置后的 date 字段Fri Jan 01 00:00:00 CST 2016
设置后的 price 字段0.027
```

## 自定义注解

```
cn.eccto.study.spring_framework.formatter.CustomFormatAnnotationExample
```

```java
/**
 * 自定义注解示例
 * <p>
 * 1. 声明自定义注解 {@link LocaleFormat}, style() 方法指定 {@link LocaleStyle}
 * 2. 创建一个自定义的 ${@link LocaleFormatter},实现了{@link Formatter}接口
 * 3. 使用 formatter 去绑定注解 {@link LocaleFormatAnnotationFormatterFactory}
 * 4. 使用自定义注解 {@link LocaleFormat} 去标注 {@link MyBean} 内的属性
 *
 * @author ec 2019/11/04 15:44
 */
public class CustomFormatAnnotationExample {

    public static void main(String[] args) {
        DefaultFormattingConversionService service =
                new DefaultFormattingConversionService();
        service.addFormatterForFieldAnnotation(
                new LocaleFormatAnnotationFormatterFactory());

        MyBean bean = new MyBean();
        DataBinder dataBinder = new DataBinder(bean);
        dataBinder.setConversionService(service);

        //将属性值设置为"zh_CN"
        MutablePropertyValues mpv = new MutablePropertyValues();
        mpv.add("myLocale", "zh_CN");


        dataBinder.bind(mpv);
//        dataBinder.getBindingResult()
//                .getModel()
//                .entrySet()
//                .forEach(System.out::println);
        System.out.println(bean.getMyLocale());
    }
```

#### 声明自定义注解 {@link LocaleFormat}, style() 方法指定 {@link LocaleStyle}

```java
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface LocaleFormat {

    LocaleStyle style () default LocaleStyle.CountryDisplayName;
}
```

```java
public enum LocaleStyle {
    /**
     * 展示国家名称
     */
    CountryDisplayName,
    /**
     * 展示国家简码
     */
    ISO3Country,
    /**
     * 展示语言
     */
    ISO3Language;
}
```

2. #### 创建一个自定义的 ${@link LocaleFormatter},实现了{@link Formatter}接口

```java
public class LocaleFormatter implements Formatter<Locale> {
    private LocaleStyle localeStyle;

    public LocaleStyle getLocaleStyle() {
        return localeStyle;
    }

    public void setLocaleStyle(
            LocaleStyle localeStyle) {
        this.localeStyle = localeStyle;
    }

    @Override
    public Locale parse(String text, Locale locale) throws ParseException {
        Optional<Locale> o = Arrays.stream(Locale.getAvailableLocales()).parallel()
                .filter(l -> this.localeByStylePredicate(l, text))
                .findAny();
        if (o.isPresent()) {
            return o.get();
        }
        return null;
    }

    @Override
    public String print(Locale object, Locale locale) {
        switch (localeStyle) {
            case CountryDisplayName:
                return object.getDisplayCountry();
            case ISO3Country:
                return object.getISO3Country();
            case ISO3Language:
                return object.getISO3Language();
            default:
                return object.toString();
        }
    }

    private boolean localeByStylePredicate(Locale locale, String text) {
        try {
            switch (localeStyle) {
                case CountryDisplayName:
                    return locale.getDisplayCountry().equalsIgnoreCase(text);
                case ISO3Country:
                    return locale.getISO3Country().equalsIgnoreCase(text);
                case ISO3Language:
                    return locale.getISO3Language().equalsIgnoreCase(text);
                default:
                    return false;
            }
        } catch (MissingResourceException e) {
            //ignore;
        }
        return false;
    }
}
```

3. #### 使用 formatter 去绑定注解 {@link LocaleFormatAnnotationFormatterFactory}

```java
public class LocaleFormatAnnotationFormatterFactory implements
        AnnotationFormatterFactory<LocaleFormat> {

    @Override
    public Set<Class<?>> getFieldTypes () {
        return new HashSet<>(Arrays.asList(Locale.class));
    }

    @Override
    public Printer<?> getPrinter (LocaleFormat annotation,
                                  Class<?> fieldType) {
        return getLocaleFormatter(annotation, fieldType);
    }

    @Override
    public Parser<?> getParser (LocaleFormat annotation,
                                Class<?> fieldType) {
        return getLocaleFormatter(annotation, fieldType);
    }

    private Formatter<?> getLocaleFormatter (LocaleFormat annotation,
                                             Class<?> fieldType) {
        LocaleFormatter lf = new LocaleFormatter();
        lf.setLocaleStyle(annotation.style());
        return lf;
    }
}
```

####   4.使用自定义注解 {@link LocaleFormat} 去标注 {@link MyBean} 内的属性

```java
public class MyBean {
    @LocaleFormat(style = LocaleStyle.ISO3Language)
    private Locale myLocale;
    //getters and setters

    public Locale getMyLocale() {
        return myLocale;
    }

    public void setMyLocale(Locale myLocale) {
        this.myLocale = myLocale;
    }
}
```

