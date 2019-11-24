package cn.eccto.study.springframework.tutorials.converter;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.support.DefaultConversionService;
import org.springframework.validation.DataBinder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
/**
 * ConversionServiceWithDataBinderExample
 *
 * @author EricChen 2019/11/24 14:20
 */
public class ConversionServiceWithDataBinderExample {
    public static void main (String[] args) {

        //  将当前时间设置进入
        MutablePropertyValues mpv = new MutablePropertyValues();
        mpv.add("date", new Date());

        DataBinder dataBinder = new DataBinder(new MyObject());
        DefaultConversionService service = new DefaultConversionService();
        //添加转换器
        service.addConverter(new DateToLocalDateTimeConverter());
        //commenting the following line will not populate date field
        //为 dataBinder 设置转换服务
        dataBinder.setConversionService(service);

        dataBinder.bind(mpv);
        dataBinder.getBindingResult()
                .getModel()
                .entrySet()
                .forEach(System.out::println);
    }

    private static class MyObject {
        private LocalDateTime date;

        public LocalDateTime getDate () {
            return date;
        }

        public void setDate (LocalDateTime date) {
            this.date = date;
        }

        @Override
        public String toString () {
            return "MyObject{" +
                    "date=" + date +
                    '}';
        }
    }

    private static class DateToLocalDateTimeConverter implements Converter<Date, LocalDateTime> {

        @Override
        public LocalDateTime convert (Date source) {
            return LocalDateTime.ofInstant(source.toInstant(), ZoneId.systemDefault());
        }
    }
}
