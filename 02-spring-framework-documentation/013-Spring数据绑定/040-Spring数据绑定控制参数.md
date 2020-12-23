# 040-Spring数据绑定控制参数

[TOC]

## DataBinder绑定特殊场景分析

- 当PropertyValues 中包含x的PropertyValue, 目标对象B中不存在X属性,当bind方法执行的时候会?
- 当PropertyValues 中包含x的PropertyValue, 目标对象B中存在X属性,当bind方法执行的时候会?
- 当PropertyValues 中包含`x.y`的PropertyValue, 目标对象B中存在X属性(嵌套Y属性),当bind方法执行的时候会?

## DataBinder特性一 : 忽略未知的属性

```java
public class DataBinderDemo {

    public static void main(String[] args) {

        // 创建空白对象
        User user = new User();
        // 1. 创建 DataBinder
        DataBinder binder = new DataBinder(user, "");

        // 2. 创建 PropertyValues
        Map<String, Object> source = new HashMap<>();
        source.put("id", 1);
        source.put("name", "小马哥");

        // a. PropertyValues 存在 User 中不存在属性值
        // DataBinder 特性一 : 忽略未知的属性
        source.put("age", 18);

        // b. PropertyValues 存在一个嵌套属性，比如 company.name
        // DataBinder 特性二：支持嵌套属性
        // Company company = new Company();
        // company.setName("geekbang");
        // user.setCompany(compay)

//        source.put("company", new Company());
        source.put("company.name", "geekbang");

        PropertyValues propertyValues = new MutablePropertyValues(source);

        // 1. 调整 IgnoreUnknownFields true（默认） -> false（抛出异常，age 字段不存在于 User 类）
        // binder.setIgnoreUnknownFields(false);

        // 2. 调整自动增加嵌套路径 true（默认） —> false
        binder.setAutoGrowNestedPaths(false);

        // 3. 调整 ignoreInvalidFields false(默认） -> true（默认情况调整不变化，需要调增 AutoGrowNestedPaths 为 false）
        binder.setIgnoreInvalidFields(true);

        binder.setRequiredFields("id", "name", "city");

        binder.bind(propertyValues);

        // 3. 输出 User 内容
        System.out.println(user);

        // 4. 获取绑定结果（结果包含错误文案 code，不会抛出异常）
        BindingResult result = binder.getBindingResult();
        System.out.println(result);
    }

}


```

## DataBinder 特性二：支持嵌套属性

```java
DataBinder binder = new DataBinder(user, "");
source.put("company.name", "geekbang");
PropertyValues propertyValues = new MutablePropertyValues(source);
binder.bind(propertyValues);

// 3. 输出 User 内容
System.out.println(user);

// 4. 获取绑定结果（结果包含错误文案 code，不会抛出异常）
BindingResult result = binder.getBindingResult();
System.out.println(result);

```

## User

```java
public class User implements BeanNameAware {

    private Long id;

    private String name;

    private Company company;

}
public class Company {

    String name;
}
```



```java

```

