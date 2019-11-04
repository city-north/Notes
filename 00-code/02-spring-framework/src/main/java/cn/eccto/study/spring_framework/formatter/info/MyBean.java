package cn.eccto.study.spring_framework.formatter.info;

import org.springframework.format.Formatter;

import java.util.Locale;

/**
 * 自定义注解示例
 * <p>
 * 1. 声明自定义注解 {@link LocaleFormat}, style() 方法指定 {@link LocaleStyle}
 * 2. 创建一个自定义的 ${@link LocaleFormatter},实现了{@link Formatter}接口
 * 3. 使用 formatter 去绑定注解 {@link LocaleFormatAnnotationFormatterFactory}
 * 4. 使用自定义注解 {@link LocaleFormat} 去标注 {@link MyBean} 内的属性
 *
 * @author EricChen 2019/11/04 15:44
 */
public class MyBean {
    @LocaleFormat(style = LocaleStyle.ISO3Language)
    private Locale myLocale;
    //getters and setters

    public Locale getMyLocale() {
        return myLocale;
    }

    public void setMyLocale(Locale myLocale) {
        this.myLocale = myLocale;
    }
}
