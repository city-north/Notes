# 052-Java数字-货币-百分号格式化

[TOC]

## 核心接口

java.text.NumberFormat

## 使用预定义的Formats

通过调用[' NumberFormat '](https://docs.oracle.com/javase/8/docs/api/java/text/NumberFormat.html)类提供的方法，您可以根据[' Locale '](https://docs.oracle.com/javase/8/docs/api/java/util/Locale.html)对数字、货币和百分比进行格式化。下面的内容用一个名为[' NumberFormatDemo.java '](https://docs.oracle.com/javase/tutorial/i18n/format/examples/NumberFormatDemo.java)的示例程序演示了格式化技术。

## 数字类型

使用NumberFormat.getNumberInstance(Locale) 方法获取number格式化器

```java
static public void displayNumber(Locale currentLocale) {

    Integer quantity = new Integer(123456);
    Double amount = new Double(345987.246);
    NumberFormat numberFormatter;
    String quantityOut;
    String amountOut;

    numberFormatter = NumberFormat.getNumberInstance(currentLocale);
    quantityOut = numberFormatter.format(quantity);
    amountOut = numberFormatter.format(amount);
    System.out.println(quantityOut + "   " + currentLocale.toString());
    System.out.println(amountOut + "   " + currentLocale.toString());
}
```

输出,当传入的Locale不同时,展示页不同

```
123 456   fr_FR
345 987,246   fr_FR
123.456   de_DE
345.987,246   de_DE
123,456   en_US
345,987.246   en_US
```

## 货币Currencies

```java
static public void displayCurrency( Locale currentLocale) {

    Double currencyAmount = new Double(9876543.21);
    Currency currentCurrency = Currency.getInstance(currentLocale);
    NumberFormat currencyFormatter = 
        NumberFormat.getCurrencyInstance(currentLocale);

    System.out.println(
        currentLocale.getDisplayName() + ", " +
        currentCurrency.getDisplayName() + ": " +
        currencyFormatter.format(currencyAmount));
}
```

输出:

```
French (France), Euro: 9 876 543,21 €
German (Germany), Euro: 9.876.543,21 €
English (United States), US Dollar: $9,876,543.21
```

At first glance, this output may look wrong to you because the numeric values are all the same. Of course, 9 876 543,21 € is 

### 百分比Percentages

You can also use the methods of the [`NumberFormat`](https://docs.oracle.com/javase/8/docs/api/java/text/NumberFormat.html) class to format percentages. To get the locale-specific formatter, invoke the [`getPercentInstance`](https://docs.oracle.com/javase/8/docs/api/java/text/NumberFormat.html#getPercentInstance-java.util.Locale-) method. With this formatter, a decimal fraction such as 0.75 is displayed as 75%.

The following code sample shows how to format a percentage.

```java
static public void displayPercent(Locale currentLocale) {

    Double percent = new Double(0.75);
    NumberFormat percentFormatter;
    String percentOut;

    percentFormatter = NumberFormat.getPercentInstance(currentLocale);
    percentOut = percentFormatter.format(percent);
    System.out.println(percentOut + "   " + currentLocale.toString());
}
```

This sample prints the following:

```
75 %   fr_FR
75%   de_DE
75%   en_US
```