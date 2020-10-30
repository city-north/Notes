# JavaBean类型转换

- [JavaBean中的类型转换](#JavaBean中的类型转换)



## JavaBean中的类型转换

有属性赋值，必然就会有类型转换。说白了我们从配置文件读取的数据是字符串，与属性进行参数绑定的过程中势必会有类型转换，`java.beans` 中提供了相应的 API：

- ```
  PropertyEditor
  ```

  - 属性编辑器顶层接口

- ```
  PropertyEditorSupport
  ```

  - 属性编辑器实现类

- ```
  PropertyEditorManager
  ```

  - 属性编辑器管理器
  - 在 Spring 中提供了一个 `PropertyEditorRegistrar`

先看一个例子：

User 类增加 Date 属性：

```java
public class User {

    private String username;

    private Integer age;
  
    private Date createTime;

    // getter/setter
    // toString

}
```

日期转换器：

```java
/**
 * 日期属性编辑器
 */
public class DatePropertyEditor extends PropertyEditorSupport {
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

在之前的例子中内省设置属性值都是直接通过 `PropertyDescriptor` 获取属性的写方法通过反射去赋值，而如果需要对值进行类型转换，则需要通过 `PropertyEditorSupport#setAsText` 调用 `setValue` 方法，然后 `setValue` 方法触发属性属性修改事件：

```java
public class PropertyEditorSupport implements PropertyEditor {

    public void setValue(Object value) {
        this.value = value;
        firePropertyChange();
    }
}
```

要注意这里的 `value` 实际上是临时存储在 `PropertyEditorSupport` 中，`PropertyEditorSupport` 则作为事件源，从而得到类型转换后的 `value`，再通过 `PropertyDescriptor` 获取属性的写方法通过反射去赋值。

```java
@Test
public void test6() throws IntrospectionException, FileNotFoundException {
   Map<String,Object> properties = ImmutableMap.of("age",1,"username","zhangsan","createTime","2020-01-01");
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
            propertyDescriptor.setPropertyEditorClass(DatPropertyEditor.class);
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
private void setPropertyValue(User user, PropertyDescriptor propertyDescriptor, Object value1) {
    try {
        propertyDescriptor.getWriteMethod().invoke(user, value1);
    } catch (IllegalAccessException | InvocationTargetException ignored) {
    }
}
```

输出结果：

```
User{username='zhangsan', age=1, createTime=2020-1-1 0:00:00}
```