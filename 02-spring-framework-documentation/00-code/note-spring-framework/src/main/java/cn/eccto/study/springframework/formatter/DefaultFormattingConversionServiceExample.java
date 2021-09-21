package cn.eccto.study.springframework.formatter;

import org.springframework.core.convert.ConversionService;
import org.springframework.format.support.DefaultFormattingConversionService;

import java.time.Instant;
import java.time.LocalDate;


/**
 * 使用默认的 {@link DefaultFormattingConversionService}
 *
 * @author JonathanChen 2019/11/04 20:05
 */
class DefaultFormattingConversionServiceExample {
    public static void main(String[] args) {
        ConversionService service =
                new DefaultFormattingConversionService();

        //String to Instant
        Instant instant = service.convert("2016-11-15T01:12:05.695Z", Instant.class);
        System.out.println(instant);

        //Instant to String
        String convert = service.convert(instant, String.class);
        System.out.println(convert);

        LocalDate localDate = service.convert("11/13/16", LocalDate.class);
        System.out.println(localDate);
    }

}
