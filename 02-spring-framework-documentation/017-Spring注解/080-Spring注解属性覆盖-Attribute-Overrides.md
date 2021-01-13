# 080-Spring注解属性覆盖-Attribute-Overrides.md

[toc]

## 什么是属性注解覆盖

An ***attribute override*** is an annotation attribute that *overrides* (or *shadows*) an annotation attribute in a meta-annotation. Attribute overrides can be categorized as follows.

1. **Implicit Overrides**: given attribute `A` in annotation `@One` and attribute `A` in annotation `@Two`, if `@One` is meta-annotated with `@Two`, then attribute `A` in annotation `@One` is an *implicit override* for attribute `A` in annotation `@Two` based solely on a naming convention (i.e., both attributes are named `A`).
2. **Explicit Overrides**: if attribute `A` is declared as an alias for attribute `B` in a meta-annotation via `@AliasFor`, then `A` is an *explicit override* for `B`.
3. **Transitive Explicit Overrides**: if attribute `A` in annotation `@One` is an explicit override for attribute `B` in annotation `@Two` and `B` is an explicit override for attribute `C` in annotation `@Three`, then `A` is a *transitive explicit override* for `C` following the [law of transitivity](https://en.wikipedia.org/wiki/Transitive_relation).



```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@ComponentScan
public @interface MyComponentScan {

    @AliasFor(annotation = ComponentScan.class, attribute = "value") // 隐性别名
    String[] scanBasePackages() default {"#"};// "多态"，子注解提供新的属性方法引用"父"（元）注解中的属性方法
}
```



## 隐式覆盖

注解与元注解出现方法名称一样的情况下, 注解的属性会覆盖元注解的属性

当子注解包含与父注解相同的属性时,可以隐式地覆盖父注解的属性

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@MyComponentScan
public @interface MyComponentScan2 {

    /**
     * 与元注解 @MyComponentScan 同名属性
     */
    String[] scanBasePackages() default {};

}

```

## 显性覆盖

packages 

- 覆盖了 scanBasePackages 

- 覆盖了元注解 scanBasePackages

```java
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@MyComponentScan
public @interface MyComponentScan2 {

    /**
     * 与元注解 @MyComponentScan 同名属性
     */
    String[] scanBasePackages() default {};


    @AliasFor("scanBasePackages")
    String[] packages() default {}; // packages 覆盖了 scanBasePackages 覆盖了元注解 scanBasePackages


}



```

