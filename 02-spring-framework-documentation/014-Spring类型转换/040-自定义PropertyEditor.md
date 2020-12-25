# 040-自定义PropertyEditor

[TOC]

## 拓展模式

- 拓展 java.beans.PropertyEditorSupport 类或者实现 PropertyEditor接口

- 实现 org.springframework.beans.PropertyEditorRegistrar
  - 调用`void registerCustomEditors(PropertyEditorRegistry registry);` 方法
  - `PropertyEditorRegistrar`实现注册为SpringBean

- 向 `org.springframework.beans.PropertyEditorRegistry` 注册自定义 PropertyEditor实现
  - 通用类型`void registerCustomEditor(Class<?> requiredType, PropertyEditor propertyEditor);`
  - JavaBean属性类型实现`void registerCustomEditor(@Nullable Class<?> requiredType, @Nullable String propertyPath, PropertyEditor propertyEditor);`

## 代码实现

#### 第一步，实现自己的PropertyEditor

例子里时自己实现了一个String->Properties 的类型的PropertyEditor

```java
public class StringToPropertiesPropertyEditor extends PropertyEditorSupport {
    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        Properties properties = new Properties();
        try {
            properties.load(new StringReader(text));
        } catch (Exception e) {
            throw new IllegalArgumentException("");
        }

        //3.临时存储 Properties 对象
        setValue(properties);
    }
}
```

#### 第二步，自定义注册器PropertyEditorRegistrar

注册器关注的是类型和编辑器，“context” 代表属性名路径

```java
/**
 * <p>
 * 自定义 {@link PropertyEditorRegistrar} 的实现
 * </p>
 */
public class CustomizedPropertyEditorRegistrar implements PropertyEditorRegistrar {

    @Override
    public void registerCustomEditors(PropertyEditorRegistry registry) {
        // 1. 通用类型转换
        // 2. Java Bean 属性类型转换
        registry.registerCustomEditor(User.class,"context",new StringToPropertiesPropertyEditor());
    }
}

```

#### 测试代码

```java
public class SpringCustomizedPropertyEditorDemo {
    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:/META-INF/property-editors-context.xml");
        User user = applicationContext.getBean("user", User.class);
        System.out.println(user);
    }
}
```