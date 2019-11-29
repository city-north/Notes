package cn.eccto.study.springframework.tutorials.converter;

import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

import java.util.Collection;
import java.util.Currency;

/**
 * 本实例演示了如何使用 {@link ConversionService} 的默认实现类 {@link DefaultConversionService}
 *
 * @author EricChen 2019/11/22 23:22
 */
public class ConversionServiceExample {
    public static void main(String[] args) {
        DefaultConversionService service = new DefaultConversionService();

        Currency currency = service.convert("USD", Currency.class);
        System.out.println(currency);

        Collection<String> list = service.convert("Deb, Mike, Kim", Collection.class);
        System.out.println(list);
    }
}
