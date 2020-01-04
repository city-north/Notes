package vip.ericchen.spring.annotation;

import java.lang.annotation.*;

/**
 * Annotation for mapping a web request onto methods in request-handling class with flexible method signatures
 *
 * @author EricChen 2020/01/04 21:19
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {


    String value() default "";

}
