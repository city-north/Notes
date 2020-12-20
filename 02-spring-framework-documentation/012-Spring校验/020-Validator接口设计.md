# 020-Validator接口设计

[TOC]

## 接口职责

- Spring内部校验器接口,通过编程的方式校验目标对象

## 核心方法

- supports(Class): 校验目标类是否能够校验
- validate(Object, Errors): 校验目标对象,并将校验失败的内容输出到Errors对象

## 配套组件

- 错误收集器: org.springframework.validation.Errors
- Validator工具类 : org.springframework.validation.ValidationUtils

## 源码

```java
public interface Validator {

	boolean supports(Class<?> clazz);

	void validate(Object target, Errors errors);

}
```

supports方法校验这个类是否可以校验,实际上让校验的范围变得狭隘

validate 校验后,如果报错,则加入到Errors内

## 官方实例

```java
public class UserLoginValidator implements Validator {

  private static final int MINIMUM_PASSWORD_LENGTH = 6;

  public boolean supports(Class clazz) {
    return UserLogin.class.isAssignableFrom(clazz);
  }

  public void validate(Object target, Errors errors) {
    //如果是空,则插入到Errors
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userName", "field.required");
    ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "field.required");
    UserLogin login = (UserLogin) target;
    if (login.getPassword() != null
        && login.getPassword().trim().length() < MINIMUM_PASSWORD_LENGTH) { 
      errors.rejectValue("password", "field.min.length",
                         new Object[]{Integer.valueOf(MINIMUM_PASSWORD_LENGTH)},
                         "The password must be at least [" + MINIMUM_PASSWORD_LENGTH + "] characters in length.");
    }
  }
}
```

## ValidationUtils校验工具类

```java
public abstract class ValidationUtils {

   private static final Log logger = LogFactory.getLog(ValidationUtils.class);

//执行校验
   public static void invokeValidator(Validator validator, Object target, Errors errors) {
      invokeValidator(validator, target, errors, (Object[]) null);
   }

  
   public static void invokeValidator(
         Validator validator, Object target, Errors errors, @Nullable Object... validationHints) {

      Assert.notNull(validator, "Validator must not be null");
      Assert.notNull(errors, "Errors object must not be null");

      if (logger.isDebugEnabled()) {
         logger.debug("Invoking validator [" + validator + "]");
      }
      if (!validator.supports(target.getClass())) {
         throw new IllegalArgumentException(
               "Validator [" + validator.getClass() + "] does not support [" + target.getClass() + "]");
      }

      if (!ObjectUtils.isEmpty(validationHints) && validator instanceof SmartValidator) {
         ((SmartValidator) validator).validate(target, errors, validationHints);
      }
      else {
         validator.validate(target, errors);
      }

      if (logger.isDebugEnabled()) {
         if (errors.hasErrors()) {
            logger.debug("Validator found " + errors.getErrorCount() + " errors");
         }
         else {
            logger.debug("Validator found no errors");
         }
      }
   }


 
   public static void rejectIfEmpty(Errors errors, String field, String errorCode) {
      rejectIfEmpty(errors, field, errorCode, null, null);
   }

  
   public static void rejectIfEmpty(Errors errors, String field, String errorCode, String defaultMessage) {
      rejectIfEmpty(errors, field, errorCode, null, defaultMessage);
   }

 
   public static void rejectIfEmpty(Errors errors, String field, String errorCode, Object[] errorArgs) {
      rejectIfEmpty(errors, field, errorCode, errorArgs, null);
   }

 
   public static void rejectIfEmpty(Errors errors, String field, String errorCode,
         @Nullable Object[] errorArgs, @Nullable String defaultMessage) {

      Assert.notNull(errors, "Errors object must not be null");
      Object value = errors.getFieldValue(field);
      if (value == null || !StringUtils.hasLength(value.toString())) {
         errors.rejectValue(field, errorCode, errorArgs, defaultMessage);
      }
   }


   public static void rejectIfEmptyOrWhitespace(Errors errors, String field, String errorCode) {
      rejectIfEmptyOrWhitespace(errors, field, errorCode, null, null);
   }

 
   public static void rejectIfEmptyOrWhitespace(
         Errors errors, String field, String errorCode, String defaultMessage) {

      rejectIfEmptyOrWhitespace(errors, field, errorCode, null, defaultMessage);
   }

  
   public static void rejectIfEmptyOrWhitespace(
         Errors errors, String field, String errorCode, @Nullable Object[] errorArgs) {

      rejectIfEmptyOrWhitespace(errors, field, errorCode, errorArgs, null);
   }

   
   public static void rejectIfEmptyOrWhitespace(
         Errors errors, String field, String errorCode, @Nullable Object[] errorArgs, @Nullable String defaultMessage) {

      Assert.notNull(errors, "Errors object must not be null");
      Object value = errors.getFieldValue(field);
      if (value == null ||!StringUtils.hasText(value.toString())) {
         errors.rejectValue(field, errorCode, errorArgs, defaultMessage);
      }
   }

}
```