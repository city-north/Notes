package vip.ericchen.study.orm.ecbatis.v2.annotation;

import java.lang.annotation.*;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/02/25 23:06
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Intercepts {
    String value();
}
