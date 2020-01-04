package vip.ericchen.spring.annotation;

import java.lang.annotation.*;

/**
 * EricChen's Autowired
 *
 * @author EricChen 2020/01/04 21:10
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Autowired {

}
