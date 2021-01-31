# 020-基于JavaBeans接口的类型转换

[TOC]

## 核心职责

- 将Spring类型的内容转换为目标类型的对象

## 拓展原理

1. Spring框架将文本内容传递到PropertyEditor实现的setAsTest(String)方法
2. PropertyEditor#setAsTest(String) 方法将String类型转化为目标类型的对象
3. 将目标类型的对象传入PropertyEditor#setValue(Object)方法
4. PropertyEditor#setValue(Object)方法实现需要临时存储传入对象
5. Spring框架将通过PropertyEditor#getValue()获取类型转换后的对象

## 代码实例

```java
public class StringToPropertiesPropertyEditor extends PropertyEditorSupport {

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
      //1. Spring框架将文本内容传递到PropertyEditor实现的setAsTest(String)方法
        Properties properties = new Properties();
        try {
          //2. PropertyEditor#setAsTest(String) 方法将String类型转化为目标类型的对象
            properties.load(new StringReader(text));
        } catch (Exception e) {
            throw new IllegalArgumentException("");
        }
        //3.临时存储 Properties 对象 将目标类型的对象传入PropertyEditor#setValue(Object)方法
        setValue(properties);
    }
}

```

```Java
public static void main(String[] args) {
  // 模拟 Spring Framework 操作
  String text = "name=EricChen";
  StringToPropertiesPropertyEditor stringToPropertiesPropertyEditor = new StringToPropertiesPropertyEditor();
  stringToPropertiesPropertyEditor.setAsText(text);
  final Object value = stringToPropertiesPropertyEditor.getValue();
  System.out.println(value);
}
```