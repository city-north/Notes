# Locale机制详解

https://docs.oracle.com/javase/tutorial/i18n/TOC.html

https://docs.oracle.com/javase/tutorial/i18n/locale/create.html

## 目录

- [Locale存在的意义](#Locale存在的意义)
- [创建Locale对象的四种方式](#创建Locale对象的四种方式)
- [Locale-Sensitive Services SPI](#Locale-Sensitive Services SPI)

## Locale存在的意义

在不同的国家或者地区Java代码如何区分? 使用Locale类

## 创建Locale对象的四种方式

- [LocaleBuilder类](#LocaleBuilder类)
- [Locale构造器](#Locale构造器)
- [Locale的forLanguageTag工厂方法](#Locale的forLanguageTag工厂方法)
- [Locale常量](#Locale常量)

## LocaleBuilder类

The Locale.Builder utility class can be used to construct a Locale object that conforms to the IETF BCP 47 syntax. For example, to specify the French language and the country of Canada, you could invoke the Locale.Builder constructor and then chain the setter methods as follows:

```java
Locale aLocale = new Locale.Builder().setLanguage("fr").setRegion("CA").build();
//The next example creates Locale objects for the English language in the United States and Great Britain:
Locale bLocale = new Locale.Builder().setLanguage("en").setRegion("US").build();
Locale cLocale = new Locale.Builder().setLanguage("en").setRegion("GB").build();
//The final example creates a Locale object for the Russian language:
Locale dLocale = new Locale.Builder().setLanguage("ru").setScript("Cyrl").build();
```

## Locale构造器

There are three constructors available in the `Locale` class for creating a `Locale` object:

- [`Locale(String language)`](https://docs.oracle.com/javase/8/docs/api/java/util/Locale.html#Locale-java.lang.String-)
- [`Locale(String language, String country)`](https://docs.oracle.com/javase/8/docs/api/java/util/Locale.html#Locale-java.lang.String-java.lang.String-)
- [`Locale(String language, String country, String variant)`](https://docs.oracle.com/javase/8/docs/api/java/util/Locale.html#Locale-java.lang.String-java.lang.String-java.lang.String-)

The following examples create `Locale` objects for the French language in Canada, the English language in the U.S. and Great Britain, and the Russian language.

```
aLocale = new Locale("fr", "CA");
bLocale = new Locale("en", "US");
cLocale = new Locale("en", "GB");
dLocale = new Locale("ru");
```

It is not possible to set a script code on a `Locale` object in a release earlier than JDK 7.

## Locale的forLanguageTag工厂方法

If you have a language tag string that conforms to the IETF BCP 47 standard, you can use the [`forLanguageTag(String)`](https://docs.oracle.com/javase/8/docs/api/java/util/Locale.html#forLanguageTag-java.lang.String-) factory method, which was introduced in the Java SE 7 release. For example:

```
Locale aLocale = Locale.forLanguageTag("en-US");
Locale bLocale = Locale.forLanguageTag("ja-JP-u-ca-japanese");
```

## Locale常量

For your convenience the `Locale` class provides [constants](https://docs.oracle.com/javase/8/docs/api/java/util/Locale.html#field_summary) for some languages and countries. For example:

```
cLocale = Locale.JAPAN;
dLocale = Locale.CANADA_FRENCH;
```

When you specify a language constant, the region portion of the `Locale` is undefined. The next three statements create equivalent `Locale` objects:

```
j1Locale = Locale.JAPANESE;
j2Locale = new Locale.Builder().setLanguage("ja").build();
j3Locale = new Locale("ja");
```

The `Locale` objects created by the following three statements are also equivalent:

```
j4Locale = Locale.JAPAN;
j5Locale = new Locale.Builder().setLanguage("ja").setRegion("JP").build();
j6Locale = new Locale("ja", "JP");
```

## Locale-Sensitive Services SPI

使用spi的时候要注意

This feature enables the plug-in of locale-dependent data and services. In this way, third parties are able to provide implementations of most locale-sensitive classes in the `java.text` and `java.util` packages.

The implementation of SPIs *(Service Provider Interface)* is based on abstract classes and Java interfaces that are implemented by the service provider. At runtime the Java class loading mechanism is used to dynamically locate and load classes that implement the SPI.

You can use the locale-sensitive services SPI to provide the following locale sensitive implementations:

- `BreakIterator` objects
- `Collator` objects
- Language code, Country code, and Variant name for the `Locale` class
- Time Zone names
- Currency symbols
- `DateFormat` objects
- `DateFormatSymbol` objects
- `NumberFormat` objects
- `DecimalFormatSymbols` objects

The corresponding SPIs are contained both in `java.text.spi` and in `java.util.spi` packages:

| `java.util.spi`                                              | `java.text.spi`                                              |
| ------------------------------------------------------------ | ------------------------------------------------------------ |
| `CurrencyNameProvider``LocaleServiceProvider``TimeZoneNameProvider``CalendarDataProvider` | `BreakIteratorProvider``CollatorProvider``DateFormatProvider``DateFormatSymbolsProvider``DecimalFormatSymbolsProvider``NumberFormatProvider` |

For example, if you would like to provide a `NumberFormat` object for a new locale, you have to implement the `java.text.spi.NumberFormatProvider` class. You need to extend this class and implement its methods:

- `getCurrencyInstance(Locale locale)`
- `getIntegerInstance(Locale locale)`
- `getNumberInstance(Locale locale)`
- `getPercentInstance(Locale locale)`

```
Locale loc = new Locale("da", "DK");
NumberFormat nf = NumberFormatProvider.getNumberInstance(loc);
```

These methods first check whether the Java runtime environment supports the requested locale; if so, they use that support. Otherwise, the methods call the `getAvailableLocales()` methods of installed providers for the appropriate interface to find a provider that supports the requested locale.