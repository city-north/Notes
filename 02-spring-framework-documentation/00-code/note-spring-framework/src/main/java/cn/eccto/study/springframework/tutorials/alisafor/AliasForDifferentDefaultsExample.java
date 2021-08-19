package cn.eccto.study.springframework.tutorials.alisafor;

import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.annotation.AnnotationConfigurationException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 示例: 被 {@link AliasFor} 注解标注的属性默认值必须相同,不然会抛出异常 {@link AnnotationConfigurationException}
 *
 * @author JonathanChen 2019/11/28 19:56
 */
public class AliasForDifferentDefaultsExample {
    public static void main(String[] args) {
        AnnotationAttributes aa = AnnotatedElementUtils.getMergedAnnotationAttributes(MyObject3.class, AccessRole2.class);
        System.out.println("Attributes of AccessRole3 used on MyObject3: " + aa);
    }


    @AccessRole2("super-user")
    public class MyObject3 {
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AccessRole2 {

        @AliasFor("accessType")
        String value() default "visitor";

        @AliasFor("value")
        String accessType() default "admin";

        String module() default "gui";
    }
}
