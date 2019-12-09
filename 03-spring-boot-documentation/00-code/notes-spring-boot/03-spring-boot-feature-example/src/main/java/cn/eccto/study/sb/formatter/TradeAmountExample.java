package cn.eccto.study.sb.formatter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.Formatter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 自定义 {@link Formatter} 并注册
 *
 * @author EricChen 2019/12/09 22:22
 */
@SpringBootApplication
public class TradeAmountExample {
    public static void main(String[] args) {
        SpringApplication.run(TradeAmountExample.class, args);

    }

    @Configuration
    static class MyConfig implements WebMvcConfigurer {
        @Override
        public void addFormatters(FormatterRegistry registry) {
            registry.addFormatter(new TradeAmountFormatter());
        }
    }
}
