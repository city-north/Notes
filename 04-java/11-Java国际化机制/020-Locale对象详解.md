# Locale对象详解

- [Locale简介](#Locale简介)
- [什么是locale-sensitive](#什么是locale-sensitive)
- [locale-sensitive的SPI](#locale-sensitive的SPI)

一个国际化的项目如何知道自己应该用哪种合适的语言或地区呢? Locale对象

## Locale简介

一个Locale对象有两部分组成

- 语言(language)
- 地区(region)

## Locale对象

它描述了

- 一种语言
- 一个位置(通常包含)
- 一段脚本(可选, 自 JavaEE7 开始支持)
- 一个变体(可选)

比如

#### 在美国

Locale 对象包含

- Language=English
- location=United States

#### 在德国

- language=German
- location=Germary

#### 在瑞士

瑞士有四种官方语言, 德语, 法语, 意大利语, 力拓罗曼私语

一个说德语的瑞士人使用的Locale是

- language=German
- location-Switzerland

虽然这个Locale 和德国 的locale 很相似,但是 货币会被表示成瑞士法郎而不是欧元

如果只设置了语言, 比如

- language=German

那么Locale 就不能处理国家相关的问题, 比如货币

## Locale对象遵循的标准

- 本地语言Language 遵循的标准时ISO-639-1
- 国家代码由大写的两个字母的代码来标表示 , 它遵循 ISO-3166-1s

## 什么是locale-sensitive

如果一个程序,当你传入不同的Locale对象时,会展现出不同的行为时,我们就说他是 locale-sensitive

例如 , NumberFormat 就是一个locale-sensitive 的对象

- 902 300 (France)
- 902.300 (Germany)
- 902,300 (United States)

可以看出展示的方式不一样

## 创建一个Locale

四种方式

- [Locale.Builder](#Locale.Builder方式创建一个Locale)
- [Locale构造器](#Locale构造器)
- [Locale.forLanguageTag工厂方法](#Locale.forLanguageTag工厂方法)
- Locale常量

## Locale.Builder方式创建一个Locale

```
Locale bLocale = new Locale.Builder().setLanguage("en").setRegion("US").build();
```

注意这个"en"可不是乱填的吗, 都有标准 IETF BCP 47

## Locale构造器

```
aLocale = new Locale("fr", "CA");
bLocale = new Locale("en", "US");
cLocale = new Locale("en", "GB");
dLocale = new Locale("ru");
```

## Locale.forLanguageTag工厂方法

```java
Locale aLocale = Locale.forLanguageTag("en-US");
Locale bLocale = Locale.forLanguageTag("ja-JP-u-ca-japanese");
```

## Locale常量

```
cLocale = Locale.JAPAN;
dLocale = Locale.CANADA_FRENCH;
```

## 国际化编码

 http://www.loc.gov/standards/iso639-2/php/code_list.php

| Language Code | Description |
| ------------- | ----------- |
| `de`          | German      |
| `en`          | English     |
| `fr`          | French      |
| `ru`          | Russian     |
| `ja`          | Japanese    |
| `jv`          | Javanese    |
| `ko`          | Korean      |
| `zh`          | Chinese     |

### Script Code

http://unicode.org/iso15924/iso15924-codes.html.

| Script Code | Description |
| ----------- | ----------- |
| `Arab`      | Arabic      |
| `Cyrl`      | Cyrillic    |
| `Kana`      | Katakana    |
| `Latn`      | Latin       |

## 地区code

The region (country) code consists of either two or three uppercase letters that conform to the ISO 3166 standard, or three numbers that conform to the UN M.49 standard. A copy of the codes can be found at http://www.chemie.fu-berlin.de/diverse/doc/ISO_3166.html.

The following table contains several sample country and region codes.

| A-2 Code | A-3 Code | Numeric Code | Description        |
| -------- | -------- | ------------ | ------------------ |
| `AU`     | `AUS`    | `036`        | Australia          |
| `BR`     | `BRA`    | `076`        | Brazil             |
| `CA`     | `CAN`    | `124`        | Canada             |
| `CN`     | `CHN`    | `156`        | China              |
| `DE`     | `DEU`    | `276`        | Germany            |
| `FR`     | `FRA`    | `250`        | France             |
| `IN`     | `IND`    | `356`        | India              |
| `RU`     | `RUS`    | `643`        | Russian Federation |
| `US`     | `USA`    | `840`        | United States      |

### 变码 Variant Code

The optional `variant` code can be used to further distinguish your `Locale`. For example, the variant code can be used to indicate dialectical differences that are not covered by the region code.

------

**Version Note:** Prior to the Java SE 7 release, the variant code was sometimes used to identify differences that were not specific to the language or region. For example, it might have been used to identify differences between computing platforms, such as Windows or UNIX. Under the IETF BCP 47 standard, this use is discouraged.

To define non-language-specific variations relevant to your environment, use the extensions mechanism, as explained in [BCP 47 Extensions](https://docs.oracle.com/javase/tutorial/i18n/locale/extensions.html).

------

As of the Java SE 7 release, which conforms to the IETF BCP 47 standard, the variant code is used specifically to indicate additional variations that define a language or its dialects. The IETF BCP 47 standard imposes syntactic restrictions on the variant subtag. You can see a list of variant codes (search for *variant*) at http://www.iana.org/assignments/language-subtag-registry.

For example, Java SE uses the variant code to support the Thai language. By convention, a `NumberFormat` object for the `th` and `th_TH` locales will use common Arabic digit shapes, or Arabic numerals, to format Thai numbers. However, a `NumberFormat` for the `th_TH_TH` locale uses Thai digit shapes. The excerpt from [`ThaiDigits.java`](https://docs.oracle.com/javase/tutorial/i18n/locale/examples/ThaiDigits.java) demonstrates this:

```
String outputString = new String();
Locale[] thaiLocale = {
             new Locale("th"),
             new Locale("th", "TH"),
             new Locale("th", "TH", "TH")
         };

for (Locale locale : thaiLocale) {
    NumberFormat nf = NumberFormat.getNumberInstance(locale);
    outputString = outputString + locale.toString() + ": ";
    outputString = outputString + nf.format(573.34) + "\n";
}
```

The following is a screenshot of this sample:

## locale-sensitive的SPI

- `BreakIterator` objects
- `Collator` objects
- Language code, Country code, and Variant name for the `Locale` class
- Time Zone names
- Currency symbols
- `DateFormat` objects
- `DateFormatSymbol` objects
- `NumberFormat` objects
- `DecimalFormatSymbols` objects

| `java.util.spi`       | `java.text.spi`              |
| --------------------- | ---------------------------- |
| CurrencyNameProvider  | BreakIteratorProvider        |
| LocaleServiceProvider | CollatorProvider             |
| TimeZoneNameProvider  | DateFormatProvider           |
| CalendarDataProvider  | DecimalFormatSymbolsProvider |
|                       | NumberFormatProvider         |
|                       | DateFormatSymbolsProvider    |

例如, 如果你想通过SPI提供一个自定义的NumberFormat ,你得 实现 `java.text.spi.NumberFormatProvider`

并实现以下方法

```
getCurrencyInstance(Locale locale)
getIntegerInstance(Locale locale)
getNumberInstance(Locale locale)
getPercentInstance(Locale locale)
```

```
Locale loc = new Locale("da", "DK");
NumberFormat nf = NumberFormatProvider.getNumberInstance(loc);
```

