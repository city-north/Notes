package vip.ericchen.study.spring.framework.stereotype;

import java.lang.annotation.*;

/**
 * description
 *
 * @author EricChen 2020/01/09 20:30
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Service {
}
