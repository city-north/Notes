package cn.eccto.study.springframework.constraints;

import javax.validation.Constraint;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * description
 *
 * @author EricChen 2019/11/04 22:55
 */
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = MobileValidator.class)
public @interface VaildMobile {

    boolean required() default true;

    String message() default "手机号格式不正确";

}
