# 使用 Spring 的`Validator`接口进行数据校验

Spring 提供了一个`Validator`接口对对象进行校验,`Validator`接口

例如我们想校验下面的实体类:

```java
public class Person {

    private String name;
    private int age;

    // the usual getters and setters...
}
```

写一个`Person`类的校验器,通过实现`org.springframework.validation.Validator`接口

两个方法:

- `supports(Class)`: 是否支持校验这个类
- `validate(Object, org.springframework.validation.Errors)`: 校验给定的对象,如果出现校验问题,注册一个相应的`Errors` 对象.

```java
public class PersonValidator implements Validator {

    /**
     * This Validator validates only Person instances
     */
    public boolean supports(Class clazz) {
        return Person.class.equals(clazz);
    }

    public void validate(Object obj, Errors e) {
        ValidationUtils.rejectIfEmpty(e, "name", "name.empty");
        Person p = (Person) obj;
        if (p.getAge() < 0) {
            e.rejectValue("age", "negativevalue");
        } else if (p.getAge() > 110) {
            e.rejectValue("age", "too.darn.old");
        }
    }
}
```

- 其中的`rejectIfEmpty(..)`静态方法如果是 null 或者 空串会进行拒绝

#### 可以在`CustomerValidator`中组合使用其他的`Validator`达到代码的复用

```java
public class CustomerValidator implements Validator {

    private final Validator addressValidator;

    public CustomerValidator(Validator addressValidator) {
        if (addressValidator == null) {
            throw new IllegalArgumentException("The supplied [Validator] is " +
                "required and must not be null.");
        }
        if (!addressValidator.supports(Address.class)) {
            throw new IllegalArgumentException("The supplied [Validator] must " +
                "support the validation of [Address] instances.");
        }
        this.addressValidator = addressValidator;
    }

    /**
     * This Validator validates Customer instances, and any subclasses of Customer too
     */
    public boolean supports(Class clazz) {
        return Customer.class.isAssignableFrom(clazz);
    }

    public void validate(Object target, Errors errors) {
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "surname", "field.required");
        Customer customer = (Customer) target;
        try {
            errors.pushNestedPath("address");
            ValidationUtils.invokeValidator(this.addressValidator, customer.getAddress(), errors);
        } finally {
            errors.popNestedPath();
        }
    }
}
```

校验的报告会被校验器解析到`Errors`对象中取,如果是 SpringMVC, 你可以是 yoghurt`<spring:bind/>`标签去指定错误消息,你也可以自己检查`Errors`对象中的错误报告

## 总结

本章主要是介绍了 Spring中提供的`Validator`接口,这个接口提供了两个方法,一个是判断是否支持,另一个是进行校验,校验的相关信息会存入形参传入的 `Errors`对象里