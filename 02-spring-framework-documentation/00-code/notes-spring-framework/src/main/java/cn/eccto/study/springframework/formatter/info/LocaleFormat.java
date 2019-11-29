package cn.eccto.study.springframework.formatter.info;

import org.springframework.format.Formatter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解示例
 * <p>
 * 1. 声明自定义注解 {@link LocaleFormat}, style() 方法指定 {@link LocaleStyle}
 * 2. 创建一个自定义的 ${@link LocaleFormatter},实现了{@link Formatter}接口
 * 3. 使用 formatter 去绑定注解 {@link LocaleFormatAnnotationFormatterFactory}
 * 4. 使用自定义注解 {@link LocaleFormat} 去标注 {@link MyBean} 内的属性
 *
 * @author EricChen 2019/11/04 20:40
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface LocaleFormat {

    LocaleStyle style() default LocaleStyle.CountryDisplayName;
}
