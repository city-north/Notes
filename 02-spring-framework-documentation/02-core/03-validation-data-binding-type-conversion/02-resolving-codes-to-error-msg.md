# 解析错误代码成提示信息

在上一章的例子中:

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

我们可以看到这里,当`age>110`时,会校验失败并写入`too.darn.old`,如果你想通过 Spring 中的 `MessageSource`来完成 Code 到 message 的解析:

- `MessageCodesResolver`决定了`Errors`接口中的注册的错误 Code
- 默认情况下会使用实现类`DefaultMessageCodesResolver`