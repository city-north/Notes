package cn.eccto.study.springframework.tutorials.alisafor;

import org.springframework.core.annotation.AliasFor;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationAttributes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 实例: 在注解的属性上引用其他注解的属性
 * <p>
 * 我们可以获得@AdminAccess和@AccessRole的属性，尽管我们只在MyObject2上指定了@AdminAccess。重要的是，元注释的属性被目标注释覆盖，这是Spring元注释编程模型的一个非常有用的特性。
 *
 * @author JonathanChen 2019/11/28 20:00
 */
public class AliasForMetaAnnotationExample {
    public static void main(String[] args) {
        AnnotationAttributes aa = AnnotatedElementUtils.getMergedAnnotationAttributes(MyObject2.class, AdminAccess.class);
        System.out.println("attributes of AdminAccess used on MyObject2 " + aa);
        aa = AnnotatedElementUtils.getMergedAnnotationAttributes(MyObject2.class, AccessRole.class);
        System.out.println("attributes of AccessRole used on MyObject2 " + aa);
    }


    @AdminAccess
    public class MyObject2 {
    }

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @AccessRole("admin")
    public @interface AdminAccess {
        @AliasFor(annotation = AccessRole.class, attribute = "module")
        String value() default "service";
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
