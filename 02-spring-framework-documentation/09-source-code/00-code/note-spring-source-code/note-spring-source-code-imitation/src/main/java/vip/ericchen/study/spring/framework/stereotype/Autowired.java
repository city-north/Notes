package vip.ericchen.study.spring.framework.stereotype;

import java.lang.annotation.*;

/**
 * description
 *
 * @author EricChen 2020/01/09 20:57
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {

    String value() default "";
}
