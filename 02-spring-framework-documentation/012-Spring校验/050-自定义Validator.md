# 050-自定义Validator

[TOC]

## 步骤

- 实现接口org.springframework.validation.Validator
  - 实现supports方法
  - 说下validate方法
    - 通过Errors对象收集错误
      - ObjectError对象(Bean)错误
      - FieldError 对象 属性(Property)错误
    - 通过ObjectError和FieldError关联MessageSource来获得文案

## 代码实例

#### 实现接口

```java
public class UserValidator implements Validator {

@Override
public boolean supports(Class<?> clazz) {
	return User.class.isAssignableFrom(clazz);
}

@Override
public void validate(Object target, Errors errors) {
	User user = (User) target;
	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "id", "id.required");
	ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.required");
	String userName = user.getName();
	// ...
}
}
```

#### 创建消息源

```java
static MessageSource createMessageSource() {
  StaticMessageSource messageSource = new StaticMessageSource();
  messageSource.addMessage("user.properties.not.null", Locale.getDefault(), "User 所有属性不能为空");
  messageSource.addMessage("id.required", Locale.getDefault(), "the id of User must not be null.");
  messageSource.addMessage("name.required", Locale.getDefault(), "the name of User must not be null.");
  return messageSource;
}
```



```java
/**
 * 自定义 Spring {@link Validator} 示例
 *
 * @see Validator
 * @since
 */
public class ValidatorDemo {

    public static void main(String[] args) {
        // 1. 创建 Validator
        Validator validator = new UserValidator();
        // 2. 判断是否支持目标对象的类型
        User user = new User();
        System.out.println("user 对象是否被 UserValidator 支持检验：" + validator.supports(user.getClass()));
        // 3. 创建 Errors 对象
        Errors errors = new BeanPropertyBindingResult(user, "user");
        validator.validate(user, errors);

        // 4. 获取 MessageSource 对象
        MessageSource messageSource = createMessageSource();

        // 5. 输出所有的错误文案
        for (ObjectError error : errors.getAllErrors()) {
            String message = messageSource.getMessage(error.getCode(), error.getArguments(), Locale.getDefault());
            System.out.println(message);
        }
    }
}
```

