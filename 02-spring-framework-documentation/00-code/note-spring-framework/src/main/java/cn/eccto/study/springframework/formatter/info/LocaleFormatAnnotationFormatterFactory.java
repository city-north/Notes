package cn.eccto.study.springframework.formatter.info;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Formatter;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

/**
 * 自定义注解示例
 * <p>
 * 1. 声明自定义注解 {@link LocaleFormat}, style() 方法指定 {@link LocaleStyle}
 * 2. 创建一个自定义的 ${@link LocaleFormatter},实现了{@link Formatter}接口
 * 3. 使用 formatter 去绑定注解 {@link LocaleFormatAnnotationFormatterFactory}
 * 4. 使用自定义注解 {@link LocaleFormat} 去标注 {@link MyBean} 内的属性
 *
 * @author JonathanChen 2019/11/04 20:43
 */
public class LocaleFormatAnnotationFormatterFactory implements
        AnnotationFormatterFactory<LocaleFormat> {

    @Override
    public Set<Class<?>> getFieldTypes() {
        return new HashSet<>(Arrays.asList(Locale.class));
    }

    @Override
    public Printer<?> getPrinter(LocaleFormat annotation,
                                 Class<?> fieldType) {
        return getLocaleFormatter(annotation, fieldType);
    }

    @Override
    public Parser<?> getParser(LocaleFormat annotation,
                               Class<?> fieldType) {
        return getLocaleFormatter(annotation, fieldType);
    }

    private Formatter<?> getLocaleFormatter(LocaleFormat annotation,
                                            Class<?> fieldType) {
        LocaleFormatter lf = new LocaleFormatter();
        lf.setLocaleStyle(annotation.style());
        return lf;
    }
}