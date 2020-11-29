# ResourceBoundle详解

## 什么是ResouceBoundle

概念上, ResourceBoundle 是一系列关联的子类,他们关联相同的base name

例如

`名称_语言_地区_操作系统`

```
ButtonLabel
ButtonLabel_de
ButtonLabel_en_GB
ButtonLabel_fr_CA_UNIX
```

那么如何获取指定的ResourceBoundle呢

```
ButtonLabel_fr_CA_UNIX
```

获取方法

```
Locale currentLocale = new Locale("fr", "CA", "UNIX");
ResourceBundle introLabels = ResourceBundle.getBundle("ButtonLabel", currentLocale);
```

如果我们没有指定Locale , 那么 getBundle 方法会努力获取最合适的

例如我们想获取

```
ButtonLabel_fr_CA_UNIX 且 默认的Locale 是 en_US
```

那么getBundle 会找到下面的顺序

```
ButtonLabel_fr_CA_UNIX
ButtonLabel_fr_CA
ButtonLabel_fr
ButtonLabel_en_US
ButtonLabel_en
ButtonLabel  -- 兜底
```

如果找不到,会抛出 MissingResourceException 为了避免抛出这些异常,一定要定义一个base class

## ResouceBoundle两个子类

ResouceBoundle 是一个抽象类, 拥有两个子类

- [PropertyResourceBundle](#PropertyResourceBundle) 
- [ListResourceBundle](#ListResourceBundle)

### PropertyResourceBundle

PropertyResourceBundle 是由Properties文件支持的 , 仅支持String类型的数据, 如果你想让他支持其他类型的数据,要使用 ListResourceBundle 

#### 获取

```
ResourceBundle labels = ResourceBundle.getBundle("LabelsBundle", currentLocale);
String value = labels.getString(key);
```



#### 遍历

```
ResourceBundle labels = ResourceBundle.getBundle("LabelsBundle", currentLocale);
Enumeration bundleKeys = labels.getKeys();

while (bundleKeys.hasMoreElements()) {
    String key = (String)bundleKeys.nextElement();
    String value = labels.getString(key);
    System.out.println("key = " + key + ", " + "value = " + value);
}
```





### ListResourceBundle

ListSourceBundle 通过一个便捷的list管理资源, 么一个 ListSourceBundle 是又一个class file 支持的, 你可以保存任何 指定的Locale读取方式,

## ResouceBoundle非常的灵活

如果你的Locale刚开始使用的是String,后来你想使用 ListResourceBundle , 那么不需要修改代码,例如你使用getBundle方法会自动扎到合适的Locale

```
ResourceBundle introLabels = ResourceBundle.getBundle("ButtonLabel", currentLocale);
```

如果你想让你的程序支持三种类型,那么在calsspath下得有三个class文件

```
StatsBundle_en_CA.class
StatsBundle_fr_FR.class
StatsBundle_ja_JP.class
```

#### StatsBundle_ja_JP

```java
import java.util.*;
public class StatsBundle_ja_JP extends ListResourceBundle {
    public Object[][] getContents() {
        return contents;
    }

    private Object[][] contents = {
        { "GDP", new Integer(21300) },
        { "Population", new Integer(125449703) },
        { "Literacy", new Double(0.99) },
    };
}

```

```
Locale[] supportedLocales = {
    new Locale("en", "CA"),
    new Locale("ja", "JP"),
    new Locale("fr", "FR")
};
ResourceBundle stats = ResourceBundle.getBundle("StatsBundle", currentLocale);
Double lit = (Double)stats.getObject("Literacy");

```

```
Locale = en_CA
GDP = 24400
Population = 28802671
Literacy = 0.97

Locale = ja_JP
GDP = 21300
Population = 125449703
Literacy = 0.99

Locale = fr_FR
GDP = 20200
Population = 58317450
Literacy = 0.99
```

## 自定义ResourceBundle的加载

ResourceBundle.Control 可以指定如何定位和初始化resouce bundle ,它定义了一个ResouceBundle.getBundle 工厂方法

```
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

