package cn.eccto.study.sb.customconverter;


import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 自定义 {@link Converter} 实现类,通过 {@link ConfigurationPropertiesBinding} 注解注册到 Spring
 *
 * @author EricChen 2019/12/12 20:20
 */
@Component
@ConfigurationPropertiesBinding
public class LocalDateConverter implements Converter<String, LocalDate> {

    @Override
    public LocalDate convert(String source) {
        if (source == null) {
            return null;
        }
        return LocalDate.parse(source, DateTimeFormatter.ofPattern("MM-dd-yyyy"));
    }


}
