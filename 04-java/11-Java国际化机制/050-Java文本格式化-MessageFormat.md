# 050-Java文本格式化

[TOC]

## 核心接口

java.text.MessageFormat(线程不安全 )

## 基本用法

- 设置消息格式模式 - new MessageFormat(...)
- 格式化 - format(new Object [] {...})

## 消息格式模式

- 格式元素 {ArgumentIndex (,FormartType, (FormatStyle))}

- FormatType: 消息格式类型, 可选项 , 每种类型在以下选择

  - number
  - date
  - time
  - choice

- FormatStyle: 消息格式风格,可选项包括

  - short
  - medium
  - long
  - full
  - integer
  - currency
  - percent

  

## 代码示例

使用MessageFormatAPI 可以将占位符按照指定的输出格式输出

#### 重置消息格式模式

```java
public class MessageFormatDemo {

  public static void main(String[] args) {

    int planet = 7;
    String event = "a disturbance in the Force";
    String messageFormatPattern = "At {1,time,long} on {1,date,full}, there was {2} on planet {0,number,integer}.";
    MessageFormat messageFormat = new MessageFormat(messageFormatPattern);
    String result = messageFormat.format(new Object[]{planet, new Date(), event});
    System.out.println(result);
  }
}
```

输出

```
At 5:29:52 PM CST on Saturday, December 19, 2020, there was a disturbance in the Force on planet 7.
```

#### 当给定参数少于模板参数时

```java
public class MessageFormatDemo {

    public static void main(String[] args) {

        int planet = 7;
        String event = "a disturbance in the Force";

        String messageFormatPattern = "At {1,time,long} on {1,date,full}, there was {2} on planet {0,number,integer}.";
        MessageFormat messageFormat = new MessageFormat(messageFormatPattern);
        // 重置 MessageFormatPattern
        // applyPattern
        messageFormatPattern = "This is a text : {0}, {1}, {2}";
        messageFormat.applyPattern(messageFormatPattern);
        result = messageFormat.format(new Object[]{"Hello,World", "666"});
        System.out.println(result);

    }
}
```

输出

```
This is a text : Hello,World, 666, {2}
```

#### 重置 java.util.Locale

```java
public class MessageFormatDemo {

    public static void main(String[] args) {

        int planet = 7;
        String event = "a disturbance in the Force";

        String messageFormatPattern = "At {1,time,long} on {1,date,full}, there was {2} on planet {0,number,integer}.";
        MessageFormat messageFormat = new MessageFormat(messageFormatPattern);

        // 重置 Locale
        messageFormat.setLocale(Locale.ENGLISH);
        messageFormatPattern = "At {1,time,long} on {1,date,full}, there was {2} on planet {0,number,integer}.";
              // 重置 MessageFormatPattern   
        messageFormat.applyPattern(messageFormatPattern);
        result = messageFormat.format(new Object[]{planet, new Date(), event});
        System.out.println(result);
    }
}
```

输出

```
At 5:29:52 PM CST on Saturday, December 19, 2020, there was a disturbance in the Force on planet 7.
```

#### 指定日志的格式

```java
public class MessageFormatDemo {
    public static void main(String[] args) {
        int planet = 7;
        String event = "a disturbance in the Force";
        String messageFormatPattern = "At {1,time,long} on {1,date,full}, there was {2} on planet {0,number,integer}.";
        MessageFormat messageFormat = new MessageFormat(messageFormatPattern);
        // 重置 Format
        // 根据参数索引来设置 Pattern
        messageFormat.setFormat(1,new SimpleDateFormat("YYYY-MM-dd HH:mm:ss"));
        result = messageFormat.format(new Object[]{planet, new Date(), event});
        System.out.println(result);
    }
}
```

输出

```
At 5:29:52 PM CST on 2020-12-19 17:29:52, there was a disturbance in the Force on planet 7.
```

