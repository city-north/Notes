package vip.ericchen.spring.annotation;

import java.lang.annotation.*;

/**
 *
 * Annotation which indicates that a method parameter should bound to a web request parameter
 *
 * @author EricChen 2020/01/04 21:19
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestParam {


    String value() default "";


}
