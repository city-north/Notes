# @AliasFor

`@AliasFor`注解用于声明一个注解元素的别名, Spring框架使用内部经常使用这个注解

例如我们经常使用的@Bean 注解

```java
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {


	@AliasFor("name")
	String[] value() default {};


	@AliasFor("value")
	String[] name() default {};
	
  
  ...
}
```

甚至`@AliasFor`定义中自己也用到了自己

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface AliasFor {
	@AliasFor("attribute")
	String value() default "";
	@AliasFor("value")
	String attribute() default "";
	Class<? extends Annotation> annotation() default Annotation.class;
}
```

## 示例

```java
/**
 * 示例: 如何使用AliasFor 注解
 * 从输出结果中可以看出仅仅标注了 value 属性, accessType属性也得到了赋值
 *
 * @author EricChen 2019/11/28 19:52
 */
public class AliasForExample {
    public static void main(String[] args) {
        AnnotationAttributes aa = AnnotatedElementUtils.getMergedAnnotationAttributes(MyObject1.class, AccessRole.class);
        System.out.println("Attributes of AccessRole used on MyObject1: " + aa);
    }

    @AccessRole("super-user")
    public class MyObject1 {
    }


    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AccessRole {

        @AliasFor("accessType")
        String value() default "visitor";

        @AliasFor("value")
        String accessType() default "visitor";

        String module() default "gui";
    }
}

```

#### 如果是 Aliasfor 关联的两个属性,不能有不同的默认值

会抛出`Annotation Configuration Exception`

```java
/**
 * 示例: 被 {@link AliasFor} 注解标注的属性默认值必须相同,不然会抛出异常 {@link AnnotationConfigurationException}
 *
 * @author EricChen 2019/11/28 19:56
 */
public class AliasForDifferentDefaultsExample {
    public static void main(String[] args) {
        AnnotationAttributes aa = AnnotatedElementUtils.getMergedAnnotationAttributes(MyObject3.class, AccessRole2.class);
        System.out.println("Attributes of AccessRole3 used on MyObject3: " + aa);
    }


    @AccessRole2("super-user")
    public class MyObject3 {
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AccessRole2 {

        @AliasFor("accessType")
        String value() default "visitor";

        @AliasFor("value")
        String accessType() default "admin";

        String module() default "gui";
    }
}

```

## 从元注释中别名化属性

用现有注释注释新的注释定义称为元注释。就像我们可以为同一注释中的另一个元素指定别名一样，我们也可以为元注释指定别名，但是我们必须额外指定'annotation'，如下面的示例所示。



```java
/**
 * 实例: 在注解的属性上引用其他注解的属性
 *
 *  我们可以获得@AdminAccess和@AccessRole的属性，尽管我们只在MyObject2上指定了@AdminAccess。重要的是，元注释的属性被目标注释覆盖，这是Spring元注释编程模型的一个非常有用的特性。
 *
 * @author EricChen 2019/11/28 20:00
 */
public class AliasForMetaAnnotationExample {
    public static void main(String[] args) {
        AnnotationAttributes aa = AnnotatedElementUtils.getMergedAnnotationAttributes(MyObject2.class, AdminAccess.class);
        System.out.println("attributes of AdminAccess used on MyObject2 " + aa);
        aa = AnnotatedElementUtils.getMergedAnnotationAttributes(MyObject2.class, AccessRole.class);
        System.out.println("attributes of AccessRole used on MyObject2 " + aa);
    }


    @AdminAccess
    public class MyObject2 {
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @AccessRole("admin")
    public @interface AdminAccess {
        @AliasFor(annotation = AccessRole.class, attribute = "module")
        String value() default "service";
    }


    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AccessRole {

        @AliasFor("accessType")
        String value() default "visitor";

        @AliasFor("value")
        String accessType() default "visitor";

        String module() default "gui";
    }
}
```







