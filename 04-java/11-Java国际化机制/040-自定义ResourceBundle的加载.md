# 040-自定义ResourceBundle的加载

[TOC]

## 自定义ResourceBundle加载的方式

- 字符编码控制java.util.ResourceBundle.Control(@since 1.6)
- Control SPI拓展-java.util.spi.ResourceBundleControlProvider(@since 1.8)

当我们传入Properties时可以指定字符编码

## 字符编码控制java.util.ResourceBundle.Control(@since 1.6)

```java
public class RBControl {
  public static void main(String[] args) {
    test(Locale.CHINA);
    test(new Locale("zh", "HK"));
    test(Locale.TAIWAN);
    test(Locale.CANADA);
  }
  private static void test(Locale locale) {
    ResourceBundle rb = ResourceBundle.getBundle(
      "messages/RBControl",
      Locale.CHINA,
      new ResourceBundle.Control() {
        @Override
        public List<Locale> getCandidateLocales(String baseName, Locale locale) {
          if (baseName == null)
            throw new NullPointerException();

          if (locale.equals(new Locale("zh", "HK"))) {
            return Arrays.asList(
              locale,
              Locale.TAIWAN,
              // no Locale.CHINESE here
              Locale.ROOT);
          } else if (locale.equals(Locale.TAIWAN)) {
            return Arrays.asList(
              locale,
              // no Locale.CHINESE here
              Locale.ROOT);
          }
          return super.getCandidateLocales(baseName, locale);
        }
      }

    );
    System.out.println(rb.getString("region"));
  }
}
```

## Control SPI拓展

java.util.spi.ResourceBundleControlProvider(@since 1.8)

ResourceBundle.Control 可以指定如何定位和初始化resouce bundle ,它定义了一个ResouceBundle.getBundle 工厂方法

