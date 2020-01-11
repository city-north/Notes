package vip.ericchen.study.spring.framework.web.bind.annotation;

import java.lang.annotation.*;

/**
 * description
 *
 * @author EricChen 2020/01/11 22:38
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {

    String value() default "";

    boolean required() default true;

}
