package cn.eccto.study.springframework.formatter;

import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;

/**
 * 查看 {@link DefaultFormattingConversionService} 默认注册的Converter 以及 Formatter
 *
 * @author EricChen 2019/11/04 15:09
 */
class DefaultFormatterListExample {
    public static void main (String[] args) {

        ConversionService service =
                new DefaultFormattingConversionService();
        System.out.println(service);
    }
}
