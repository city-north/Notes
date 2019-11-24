# Field Formatting

-  [05-spring-field-formatting.md](../../03-validation-data-binding-type-conversion/05-spring-field-formatting.md)  属性格式化
-  [05-spring-field-formatting(anno).md](../../03-validation-data-binding-type-conversion/05-spring-field-formatting(anno).md) 基于注解的属性格式化

# Spring 字段格式化 (Spring Field Formatting)

上面文章说道,`core.convert`包是一个标准的类型转换系统,它提供了一个统一的 `ConversionService` API和强类型`Converter` SPI 去实现类型转换的逻辑,Spring 的容器会使用这个机制来绑定属性值,例如

当 SpEL ,当调用`expression.setValue(Object bean, Object value)`,需要强转一个`Short`到`Long`.这个时候`core.convert`系统解决这个问题

当你需要的类型转换在一个典型的客户端环境,例如`web`环境或者桌面应用环境,在这种环境下,你通常需要:

- 将 String 转换成需要的类型
- 将返回值转化成 String 类型以呈现视图

这个时候你往往需要本地化`String`的值,通用的`Converter` SPI不直接支持这种格式化需求 ,Spring3 提供了一个方便的`Formatter` SPI 提供了简单、健壮的`PropertyEditor`替换方案

总之:

- 当需要一般类型的类型转换逻辑时(例如将`java.util.Date`转换成`Long`时),你可以使用`Converter`SPI
- 当你在一个客户端环境(例如 Web application)且需要解析和打印本地化的属性值时,你可以使用`Formatter`SPI

---

> 我的理解:
>
> - Spring Formatting API 允许 UI/GUI 的应用解析 object 到字符串
> - 反过来,可以将字符串解析出 bean 进行处理

---

## The `Formatter` SPI

使用`Formatter` SPI 去实现字段格式化逻辑非常简单且强类型,它继承了两个接口`Printer`和`Parser`接口

```java
package org.springframework.format;

public interface Formatter<T> extends Printer<T>, Parser<T> {
}
```

---

> 我的理解:

> Formatter 接口能让我们将 一个 T 类型的 bean 解析成 String, 反过来 String 解析成 T 类型的 bean

---

#### Printer` 接口

```java
public interface Printer<T> {

    String print(T fieldValue, Locale locale);
}
```

#### `Parser`接口

```java
import java.text.ParseException;

public interface Parser<T> {

    T parse(String clientValue, Locale locale) throws ParseException;
}
```

在实现你自己的`Formatter`时需要注意

- `T`参数是你想要进行格式化的类型,例如(`java.util.Date`)

- 如果解析出错你应该抛出`IllegalArgumentException`
- 请确保你的视线类是线程安全的

### 内置的 Formatter 实现类

`format`子包下提供了一系列的`Formatter`的实现类

- `org.springframework.format.datetime`包,提供`java.util.Date`类型的转换(使用`java.text.DataFormat`去格式化一个`Date`类型)
- `org.springframework.format.datetime.joda`包提供了[Joda data and time][http://www.joda.org/joda-time/)]的解析
- `org.springframework.format.datetime.standard`包提供了标准 JSR-310 和 Java 8 `java.time`[类型](https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/time/package-summary.html)
- `org.springframework.format.number`包包含了`java.lang.Number`以及其子类型的转换,(使用`java.text.NumberFormat`)
  - `NumberStyleFormatter`
  - `CurrencyStyleFormatter`
  - `PercentStyleFormatter`
- `org.springframework.format.number.money`包包含了 JSR-354 `javax.money`

**根据我们使用的上下文，上面的格式化程序可以由Spring隐式地注册。**

下面是`DateFormatter`,一个`Formatter`实现类的例子

```java
package org.springframework.format.datetime;

public final class DateFormatter implements Formatter<Date> {

    private String pattern;

    public DateFormatter(String pattern) {
        this.pattern = pattern;
    }

    public String print(Date date, Locale locale) {
        if (date == null) {
            return "";
        }
        return getDateFormat(locale).format(date);
    }

    public Date parse(String formatted, Locale locale) throws ParseException {
        if (formatted.length() == 0) {
            return null;
        }
        return getDateFormat(locale).parse(formatted);
    }

    protected DateFormat getDateFormat(Locale locale) {
        DateFormat dateFormat = new SimpleDateFormat(this.pattern, locale);
        dateFormat.setLenient(false);
        return dateFormat;
    }
```

## 比较 Converter 和 Formatter

- Spring Converter API 提供了一个通用的类型转换机制,但是不处理自定义格式的字段值呈现,

- Formatter API 将自定义格式的字段值进行解析
- Formatter 还可以在格式化时本地化字符串值，例如，根据客户端的 locale 解析 Date/LocalDate 

## ConversionService 和 Formatter 的关系

- `ConversionService `除了调用 Converters ,还调用 Formatter
- `ConversionService `没有定义特殊的进行格式化相关的代码,
- `ConversionService#convert()`方法调用时,Formatter 有可能隐式地调用

在核心的 Spring 应用中,我们往往使用 ConversionService 实现类:

- `org.springframework.format.support.FormattingConversionService`

这个实现类默认情况下不注册任何格式化器

- `org.springframework.format.support.DefaultFormattingConversionService`

这个实现类默认注册大多数 Spring 提供的格式化器

这两个实现类都实现了`org.springframework.format.FormatterRegistry`接口,这个接口我们可以用来注册自定义的格式化器

#### 实例代码:

```java
import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;

import java.time.Instant;
import java.time.LocalDate;

public class DefaultFormattingConversionServiceExample {
    public static void main (String[] args) {

       ConversionService service =
                            new DefaultFormattingConversionService();

        //String to Instant
        Instant instant = service.convert("2016-11-15T01:12:05.695Z", Instant.class);
        System.out.println(instant);

        //Instant to String
        String convert = service.convert(instant, String.class);
        System.out.println(convert);

        LocalDate localDate = service.convert("11/13/16", LocalDate.class);
        System.out.println(localDate);
    }
}
```

输出:

```
2016-11-15T01:12:05.695Z
2016-11-15T01:12:05.695Z
2016-11-13
```

## ConversionService默认注册的converters/formatters

直接输出所有注册的实例

```
public class DefaultFormatterListExample {
    public static void main (String[] args) {

        ConversionService service =
                new DefaultFormattingConversionService();
        System.out.println(service);
    }
}
```

## 创建一个自定义的格式化器

[参考代码](https://www.logicbig.com/tutorials/spring-framework/spring-core/formatter.html)

```java
/**
 * 自定义{@link Formatter} 实例
 *
 * @author ec 2019/11/04 15:16
 */
public class CustomFormatterExample {
    public static void main(String[] args) {
        DefaultFormattingConversionService service =
                new DefaultFormattingConversionService();

        service.addFormatter(new EmployeeFormatter());

        Employee employee = new Employee("Joe", "IT", "123-456-7890");
        String string = service.convert(employee, String.class);
        System.out.println(string);

        Employee e = service.convert(string, Employee.class);
        System.out.println(e);

    }

    /**
     * 新建一个 Formatter 并实现 Formatter 接口
     */
    private static class EmployeeFormatter implements Formatter<Employee> {

        /**
         * 将字符串解析成为一个 {@link Employee}
         *
         * @param text   要解析的字符串
         * @param locale 本地对象
         * @return
         * @throws ParseException
         */
        @Override
        public Employee parse(String text,
                              Locale locale) throws ParseException {

            String[] split = text.split(",");
            if (split.length != 3) {
                throw new ParseException("The Employee string format " +
                        "should be in this format: Mike, Account, 111-111-1111",
                        split.length);
            }
            Employee employee = new Employee(split[0].trim(),
                    split[1].trim(), split[2].trim());
            return employee;
        }

        @Override
        public String print(Employee employee, Locale locale) {
            return new StringJoiner(", ")
                    .add(employee.getName())
                    .add(employee.getDept())
                    .add(employee.getPhoneNumber())
                    .toString();

        }
    }

    private static class Employee {
        private String name;
        private String dept;
        private String phoneNumber;

        public Employee(String name, String dept, String phoneNumber) {
            this.name = name;
            this.dept = dept;
            this.phoneNumber = phoneNumber;
        }

        public String getName() {
            return name;
        }

        public String getDept() {
            return dept;
        }


        public String getPhoneNumber() {
            return phoneNumber;
        }
    }
}
```

## `FormatterRegistery` SPI

`FormatterRegistry` SPI 用来注册格式化器与转换器

- 其实现类`FormattingConversionService`适用于大多数环境
- 你可以手动配置 Spring bean`FormattingConversionServiceFactoryBean`,来注册他们

#### FormatterRegistry

```java
package org.springframework.format;

public interface FormatterRegistry extends ConverterRegistry {

    void addFormatterForFieldType(Class<?> fieldType, Printer<?> printer, Parser<?> parser);

    void addFormatterForFieldType(Class<?> fieldType, Formatter<?> formatter);

    void addFormatterForFieldType(Formatter<?> formatter);

    void addFormatterForAnnotation(AnnotationFormatterFactory<?> factory);
}
```

使用`FormatterRegistry`配置,可以减少配置文件,集中配置转换的逻辑

## `FormatterRegistrar` SPI

`FormatterRegistrar` SPI 用来注册 formatter 和 converter ,还是通过方法形参`FormatterRegistry`来注册

```java
package org.springframework.format;

public interface FormatterRegistrar {

    void registerFormatters(FormatterRegistry registry);
}
```

- 注册多个相关联的 formatter 和 converter 时有用,例如日期格式化
- 在声明性注册不够用的情况下，它也很有用

## SpringMVC 中配置格式化器

See [Conversion and Formatting](https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-config-conversion) in the Spring MVC chapter.

Also, see 