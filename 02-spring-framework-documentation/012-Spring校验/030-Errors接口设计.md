# 030-Errors接口设计

[TOC]

## Errors文案生成步骤

- 选择Errors实现(如 :org.springframework.validation.BeanPropertyBindingResult)

- 调用reject或者rejectValue
- 获取Errors对象中的ObjectError或者FieldError
- 将ObjectError或者FieldError中的code 和args ,关联到 MessageSource实现(如: ResourceBundleMessageSource)

## 代码实例

```java
public class ErrorsMessageDemo {

    public static void main(String[] args) {

        // 0. 创建 User 对象
        User user = new User();
        user.setName("小马哥");
        // 1. 选择 Errors - BeanPropertyBindingResult
        Errors errors = new BeanPropertyBindingResult(user, "user");
        // 2. 调用 reject 或 rejectValue
        // reject 生成 ObjectError
        // reject 生成 FieldError
        errors.reject("user.properties.not.null");
        // user.name = user.getName()
        errors.rejectValue("name", "name.required");

        // 3. 获取 Errors 中 ObjectError 和 FieldError
        // FieldError is ObjectError
        List<ObjectError> globalErrors = errors.getGlobalErrors();
        List<FieldError> fieldErrors = errors.getFieldErrors();
        List<ObjectError> allErrors = errors.getAllErrors();

        // 4. 通过 ObjectError 和 FieldError 中的 code 和 args 来关联 MessageSource 实现
        MessageSource messageSource = createMessageSource();

        for (ObjectError error : allErrors) {
            String message = messageSource.getMessage(error.getCode(), error.getArguments(), Locale.getDefault());
            System.out.println(message);
        }
    }

    static MessageSource createMessageSource() {
        StaticMessageSource messageSource = new StaticMessageSource();
        messageSource.addMessage("user.properties.not.null", Locale.getDefault(), "User 所有属性不能为空");
        messageSource.addMessage("id.required", Locale.getDefault(), "the id of User must not be null.");
        messageSource.addMessage("name.required", Locale.getDefault(), "the name of User must not be null.");
        return messageSource;
    }
}
```

可以看出需要遍历Errors,然后逐个遍历