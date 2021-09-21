package cn.eccto.study.springframework.formatter;

import cn.eccto.study.springframework.formatter.info.*;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.format.Formatter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.validation.DataBinder;

/**
 * 自定义注解示例
 * <p>
 * 1. 声明自定义注解 {@link LocaleFormat}, style() 方法指定 {@link LocaleStyle}
 * 2. 创建一个自定义的 ${@link LocaleFormatter},实现了{@link Formatter}接口
 * 3. 使用 formatter 去绑定注解 {@link LocaleFormatAnnotationFormatterFactory}
 * 4. 使用自定义注解 {@link LocaleFormat} 去标注 {@link MyBean} 内的属性
 *
 * @author JonathanChen 2019/11/04 20:44
 */
public class CustomFormatAnnotationExample {

    public static void main(String[] args) {
        DefaultFormattingConversionService service =
                new DefaultFormattingConversionService();
        service.addFormatterForFieldAnnotation(
                new LocaleFormatAnnotationFormatterFactory());

        MyBean bean = new MyBean();
        DataBinder dataBinder = new DataBinder(bean);
        dataBinder.setConversionService(service);

        //将属性值设置为"zh_CN"
        MutablePropertyValues mpv = new MutablePropertyValues();
        mpv.add("myLocale", "zh_CN");


        dataBinder.bind(mpv);
        dataBinder.getBindingResult()
                .getModel()
                .entrySet()
                .forEach(System.out::println);
        System.out.println(bean.getMyLocale());
    }
}