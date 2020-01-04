package vip.ericchen.spring.annotation;

import java.lang.annotation.*;

/**
 * description
 *
 * @author EricChen 2020/01/04 21:20
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
    String value() default  "";
}
