# 010-自定义PropertyEditor编辑属性.md

[TOC]

## 需求

通过自定义日期解析的PropertyEditor，将`2020-01-01`格式的日期设置到User的CreateTime属性中

总体思路：

1. 通过自省获取到User里的所有方法
2. 自定义属性编辑器
3. 如果是createTime属性则设置自定义的日期属性编辑器（DataPropertyEditor)
4. 其他属性使用默认编辑器

## 代码实现

### 第一步，通过自省获取到User中的所有方法

```java
Map<String, Object> properties = ImmutableMap.of("age", 1, "username", "zhangsan", "createTime", "2020-01-01");
User user = new User();
//获取 User Bean 信息，排除 Object
BeanInfo userBeanInfo = Introspector.getBeanInfo(User.class, Object.class);
//属性描述
PropertyDescriptor[] propertyDescriptors = userBeanInfo.getPropertyDescriptors();
```

获取到JavaBean中的属性描述符列表

### 第二步，获得属性编辑器

```java
/**
 * 日期属性编辑器
 */
class DatePropertyEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) {
        try {
            setValue((text == null) ? null : new SimpleDateFormat("yyyy-MM-dd").parse(text));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
```

### 第三步，循环遍历属性并添加监听器

```java
Stream.of(propertyDescriptors).forEach(propertyDescriptor -> {
    //获取属性名称
    String property = propertyDescriptor.getName();
    //值
    Object value = properties.get(property);
    if (Objects.equals("createTime", property)) {
        //设置属性编辑器
        propertyDescriptor.setPropertyEditorClass(DatePropertyEditor.class);
        //创建属性编辑器
        PropertyEditor propertyEditor = propertyDescriptor.createPropertyEditor(user);
        //添加监听器
        propertyEditor.addPropertyChangeListener(evt -> {
            //获取转换后的value
            Object value1 = propertyEditor.getValue();
            setPropertyValue(user, propertyDescriptor, value1);
        });
        propertyEditor.setAsText(String.valueOf(value));
        return;
    }
    setPropertyValue(user, propertyDescriptor, value);
});

```

### 第四步，设置属性值

```java
/**
     * 设置属性值,实际上是获取到writable方法后调用
     */
private static void setPropertyValue(User user, PropertyDescriptor propertyDescriptor, Object value1) {
    try {
        Method writeMethod = propertyDescriptor.getWriteMethod();
        if (writeMethod != null){
            writeMethod.invoke(user, value1);
        }
    } catch (IllegalAccessException | InvocationTargetException ignored) {
    }
}
```

## 代码总体

```java
package cn.eccto.study.java.javabeans;

import com.google.common.collect.ImmutableMap;

import java.beans.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * <p>
 * 自定义PropertyEditor方式进行属性设置
 * </p>
 */
public class PropertyEditorExample {
    public static void main(String[] args) throws IntrospectionException {
        testPropertyEditor2();
    }

    private static void testPropertyEditor() throws IntrospectionException {
        Map<String, Object> properties = ImmutableMap.of("age", 1, "username", "zhangsan", "createTime", "2020-01-01");
        User user = new User();
        //获取 User Bean 信息，排除 Object
        BeanInfo userBeanInfo = Introspector.getBeanInfo(User.class, Object.class);
        //属性描述
        PropertyDescriptor[] propertyDescriptors = userBeanInfo.getPropertyDescriptors();
        Stream.of(propertyDescriptors).forEach(propertyDescriptor -> {
            //获取属性名称
            String property = propertyDescriptor.getName();
            //值
            Object value = properties.get(property);
            if (Objects.equals("createTime", property)) {
                //设置属性编辑器
                propertyDescriptor.setPropertyEditorClass(DatePropertyEditor.class);
                //创建属性编辑器
                PropertyEditor propertyEditor = propertyDescriptor.createPropertyEditor(user);
                //添加监听器
                propertyEditor.addPropertyChangeListener(evt -> {
                    //获取转换后的value
                    Object value1 = propertyEditor.getValue();
                    setPropertyValue(user, propertyDescriptor, value1);
                });
                propertyEditor.setAsText(String.valueOf(value));
                return;
            }
            setPropertyValue(user, propertyDescriptor, value);
        });
        System.out.println(user);
    }

    /**
     * 设置属性值
     */
    private static void setPropertyValue(User user, PropertyDescriptor propertyDescriptor, Object value1) {
        try {
            Method writeMethod = propertyDescriptor.getWriteMethod();
            if (writeMethod != null){
                writeMethod.invoke(user, value1);
            }

        } catch (IllegalAccessException | InvocationTargetException ignored) {
        }
    }
}

/**
 * 日期属性编辑器
 */
class DatePropertyEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) {
        try {
            setValue((text == null) ? null : new SimpleDateFormat("yyyy-MM-dd").parse(text));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
```

