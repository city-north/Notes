package vip.ericchen.study.spring.framework.web.bind.annotation;

import java.lang.annotation.*;

/**
 * Annotation for mapping web requests onto methods in a request-handling classes
 * with flexible method signatures
 *
 * @author EricChen 2020/01/11 22:23
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {
    String value() default "";
}
