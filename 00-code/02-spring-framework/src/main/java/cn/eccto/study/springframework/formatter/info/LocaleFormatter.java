package cn.eccto.study.springframework.formatter.info;

import org.springframework.format.Formatter;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Optional;

/**
 * 自定义注解示例
 * <p>
 * 1. 声明自定义注解 {@link LocaleFormat}, style() 方法指定 {@link LocaleStyle}
 * 2. 创建一个自定义的 ${@link LocaleFormatter},实现了{@link Formatter}接口
 * 3. 使用 formatter 去绑定注解 {@link LocaleFormatAnnotationFormatterFactory}
 * 4. 使用自定义注解 {@link LocaleFormat} 去标注 {@link MyBean} 内的属性
 *
 * @author EricChen 2019/11/04 15:41
 */
public class LocaleFormatter implements Formatter<Locale> {
    private LocaleStyle localeStyle;

    public LocaleStyle getLocaleStyle() {
        return localeStyle;
    }

    public void setLocaleStyle(
            LocaleStyle localeStyle) {
        this.localeStyle = localeStyle;
    }

    @Override
    public Locale parse(String text, Locale locale) throws ParseException {
        Optional<Locale> o = Arrays.stream(Locale.getAvailableLocales()).parallel()
                .filter(l -> this.localeByStylePredicate(l, text))
                .findAny();
        if (o.isPresent()) {
            return o.get();
        }
        return null;
    }

    @Override
    public String print(Locale object, Locale locale) {
        switch (localeStyle) {
            case CountryDisplayName:
                return object.getDisplayCountry();
            case ISO3Country:
                return object.getISO3Country();
            case ISO3Language:
                return object.getISO3Language();
            default:
                return object.toString();
        }
    }

    private boolean localeByStylePredicate(Locale locale, String text) {
        try {
            switch (localeStyle) {
                case CountryDisplayName:
                    return locale.getDisplayCountry().equalsIgnoreCase(text);
                case ISO3Country:
                    return locale.getISO3Country().equalsIgnoreCase(text);
                case ISO3Language:
                    return locale.getISO3Language().equalsIgnoreCase(text);
                default:
                    return false;
            }
        } catch (MissingResourceException e) {
            //ignore;
        }
        return false;
    }
}