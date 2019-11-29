# `ComponentScan` 自定义过滤

> 版权声明
>
> 此篇文章参考于[logicbig](https://www.logicbig.com/),引用其文字/代码/图片仅用于学习笔记,不用于商业用途

`ComponentScan`注解默认扫描:

- @Component
-  @Repository
- @Service
- @Controller
- 其他关联了 @Component的注解

我们可以使用以下方式关闭 这些默认的 filter:

```java
@ComponentScan(useDefaultFilters = false)
```

我们可以自定义 "引入过滤器"

```java
@ComponentScan(useDefaultFilters = false/true,  includeFilters = {@ComponentScan.Filter{ ... })
```

或者包含一个"排除过滤器"

```java
@ComponentScan(useDefaultFilters = false/true,  excludeFilters = {@ComponentScan.Filter{ ... })
```

## @ComponentScan.Filter annotation

`ComponentScan `内部类`@Filter`如下:

```java
package org.springframework.context.annotation;
....
public @interface ComponentScan {
    ....
    Filter[] includeFilters() default {};
    Filter[] excludeFilters() default {};
    ....

    @Retention(RetentionPolicy.RUNTIME)
    @Target({})
    @interface Filter {
        FilterType type() default FilterType.ANNOTATION;

        @AliasFor("classes")
        Class<?>[] value() default {};

        @AliasFor("value")
        Class<?>[] classes() default {};

        String[] pattern() default {};
    }
}
```

比较重要的是`FilterType` 枚举,有五个值:

- ANNOTATION
- ASSIGNABLE_TYPE
- ASPECTJ
- REGEX 
- CUSTOM

下面逐一介绍

## `FilterType.ANNOTATION`

指定标注注解`@MyAnnotation`的类,扫描成 bean

 [源码](../../../00-code/notes-spring-framework/src/main/java/cn/eccto/study/springframework/tutorials/FilterTypeAnnotationExample.java) 

首先声明一个注解

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface MyAnnotation {
}
```

声明一个被注解标注的类

```java
@MyAnnotation
public class MyBean5 {
}
```

测试类

```java
@Configuration
@ComponentScan(useDefaultFilters = false,
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION, classes = MyAnnotation.class)})
public class FilterTypeAnnotationExample {
    public static void main(String[] args) {
        ApplicationContext context =
                new AnnotationConfigApplicationContext(FilterTypeAnnotationExample.class);
        Util.printBeanNames(context);
    }
}
```

输出

```
filterTypeAnnotationExample
myBean5
```



## `FilterType.ASSIGNABLE_TYPE`

使用`FilterType.ASSIGNABLE_TYPE`和`Filter.classes`和`Filter.value`可以用于指定需要扫描的类

值得注意的是

被指定的类的子类也会被扫描

```
public class MyBean1 {
}
public class MyBean2 {
}
public class MyBean3 {
}
public class MyBean4 extends MyBean3{
}
```



测试类

```java
@Configuration
@ComponentScan(useDefaultFilters = false,
      includeFilters = {@ComponentScan.Filter(
              type = FilterType.ASSIGNABLE_TYPE, classes = {MyBean1.class, MyBean3.class})})
public class FilterTypeAssignableExample {
  public static void main(String[] args) {
      ApplicationContext context =
              new AnnotationConfigApplicationContext(FilterTypeAssignableExample.class);
      Util.printBeanNames(context);
  }
}
```

输出:

```
filterTypeAssignableExample
myBean1
myBean3
myBean4
```



## `FilterType.REGEX`

指定正则表达式:

```java
@Configuration
@ComponentScan(useDefaultFilters = false,
      includeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*[12]"),
)
public class FilterTypeRegexExample {

  public static void main(String[] args) {
      ApplicationContext context =
              new AnnotationConfigApplicationContext(FilterTypeRegexExample.class);
      Util.printBeanNames(context);
  }
}
```

输出

```java
filterTypeRegexExample
myBean1
myBean2
```



## `FilterType.CUSTOM`

自定义扫描 filter

#### 实现接口`TypeFilter`

```java
public class MyTypeFilter implements TypeFilter {
  private static final String RunnableName = Runnable.class.getName();

  @Override
  public boolean match(MetadataReader metadataReader,
                       MetadataReaderFactory metadataReaderFactory) throws IOException {
      ClassMetadata classMetadata = metadataReader.getClassMetadata();
      String[] interfaceNames = classMetadata.getInterfaceNames();
      if (Arrays.stream(interfaceNames).anyMatch(RunnableName::equals)) {
          return true;
      }
      return false;
  }
}
```

#### 测试 bean

```java
public class MyBean6 implements Runnable{
  @Override
  public void run() {
      //todo
  }
}
```

#### 测试类

```java
@Configuration
@ComponentScan(useDefaultFilters = false,
      includeFilters = @ComponentScan.Filter(type = FilterType.CUSTOM, classes = MyTypeFilter.class)
)
public class FilterTypeCustomExample {

  public static void main(String[] args) {
      ApplicationContext context =
              new AnnotationConfigApplicationContext(FilterTypeCustomExample.class);
      Util.printBeanNames(context);
  }
}
```

#### 输出

```
filterTypeCustomExample
myBean6
```