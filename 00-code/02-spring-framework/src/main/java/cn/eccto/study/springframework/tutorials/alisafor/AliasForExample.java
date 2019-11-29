package cn.eccto.study.springframework.tutorials.alisafor;

import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 示例: 如何使用AliasFor 注解
 * 从输出结果中可以看出仅仅标注了 value 属性, accessType属性也得到了赋值
 *
 * @author EricChen 2019/11/28 19:52
 */
public class AliasForExample {
    public static void main(String[] args) {
        AnnotationAttributes aa = AnnotatedElementUtils.getMergedAnnotationAttributes(MyObject1.class, AccessRole.class);
        System.out.println("Attributes of AccessRole used on MyObject1: " + aa);
    }

    @AccessRole("super-user")
    public class MyObject1 {
    }


    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface AccessRole {

        @AliasFor("accessType")
        String value() default "visitor";

        @AliasFor("value")
        String accessType() default "visitor";

        String module() default "gui";
    }
}
