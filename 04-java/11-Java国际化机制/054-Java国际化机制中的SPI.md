# 054-Java国际化机制中的SPI

[TOC]

## 简介

国际化中的Service Provider 是一种可以对 **基于Locale的服务或者数据** 的可插拔机制

通过这个SPI机制,第三方可以提供一些实现.主要包是

- java.text
- java.util

## 通用写法

一般情况下再java.text 包或者java.util 包下有SPI 接口 和相关抽象类,通常情况我们实现接口,继承抽象类

## Locale-敏感的API

- [`BreakIterator`](https://docs.oracle.com/javase/8/docs/api/java/text/BreakIterator.html) objects
- [`Collator`](https://docs.oracle.com/javase/8/docs/api/java/text/Collator.html) objects
- Language code, country code, and variant name for the [`Locale`](https://docs.oracle.com/javase/8/docs/api/java/util/Locale.html) class
- Time zone names
- Currency symbols
- [`DateFormat`](https://docs.oracle.com/javase/8/docs/api/java/text/DateFormat.html) objects
- [`DateFormatSymbols`](https://docs.oracle.com/javase/8/docs/api/java/text/DateFormatSymbols.html) objects
- [`NumberFormat`](https://docs.oracle.com/javase/8/docs/api/java/text/NumberFormat.html) objects
- [`DecimalFormatSymbols`](https://docs.oracle.com/javase/8/docs/api/java/text/DecimalFormatSymbols.html) objects

The corresponding SPIs are contained both in `java.text.spi` and in `java.util.spi` packages:

#### java.util.spi

- [`CurrencyNameProvider`](https://docs.oracle.com/javase/8/docs/api/java/util/spi/CurrencyNameProvider.html)
- [`LocaleServiceProvider`](https://docs.oracle.com/javase/8/docs/api/java/util/spi/LocaleServiceProvider.html)
- [`TimeZoneNameProvider`](https://docs.oracle.com/javase/8/docs/api/java/util/spi/TimeZoneNameProvider.html)

#### java.text.spi

- [`BreakIteratorProvider`](https://docs.oracle.com/javase/8/docs/api/java/text/spi/BreakIteratorProvider.html)
- [`CollatorProvider`](https://docs.oracle.com/javase/8/docs/api/java/text/spi/CollatorProvider.html)
- [`DateFormatProvider`](https://docs.oracle.com/javase/8/docs/api/java/text/spi/DateFormatProvider.html)
- [`DateFormatSymbolsProvider`](https://docs.oracle.com/javase/8/docs/api/java/text/spi/DateFormatSymbolsProvider.html)
- [`DecimalFormatSymbolsProvider`](https://docs.oracle.com/javase/8/docs/api/java/text/spi/DecimalFormatSymbolsProvider.html)
- [`NumberFormatProvider`](https://docs.oracle.com/javase/8/docs/api/java/text/spi/NumberFormatProvider.html)

## 例子

如果你要提供一个 新的Locale对象的 NumberFormat实现

- 实现 java.text.spi.NumberFormatProvider
- 实现下面方法
  - `getCurrencyInstance(Locale locale)`
  - `getIntegerInstance(Locale locale)`
  - `getNumberInstance(Locale locale)`
  - `getPercentInstance(Locale locale)`

```java
Locale loc = new Locale("da", "DK");
NumberFormat nf = NumberFormatProvider.getNumberInstance(loc);
```

这些方法首先会在Java运行环境中检测是否存在支持的Locale, 如果不存在则调用getAvailableLocales 方法回去查找是否有支持的Provider

For an in-depth example of how to use service providers for internationalization, see [Installing a Custom Resource Bundle as an Extension](https://docs.oracle.com/javase/tutorial/i18n/serviceproviders/resourcebundlecontrolprovider.html). This section shows you how to implement the [`ResourceBundleControlProvider`](https://docs.oracle.com/javase/8/docs/api/java/util/spi/ResourceBundleControlProvider.html) interface, which enables you to use any custom `ResourceBundle.Control` classes without any additional changes to the source code of your application.