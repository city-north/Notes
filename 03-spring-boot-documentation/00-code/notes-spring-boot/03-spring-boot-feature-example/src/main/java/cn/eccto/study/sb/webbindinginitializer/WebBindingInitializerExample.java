package cn.eccto.study.sb.webbindinginitializer;

import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.web.bind.support.ConfigurableWebBindingInitializer;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 自定义 ConfigurableWebBindingInitializer
 *
 * @author EricChen 2019/12/09 22:42
 */
@SpringBootApplication
public class WebBindingInitializerExample {
    @Bean
    public ConfigurableWebBindingInitializer getConfigurableWebBindingInitializer() {
        ConfigurableWebBindingInitializer initializer = new ConfigurableWebBindingInitializer();
        FormattingConversionService conversionService = new DefaultFormattingConversionService();
        //we can add our custom converters and formatters
        //conversionService.addConverter(...);
        //conversionService.addFormatter(...);
        initializer.setConversionService(conversionService);
        //we can set our custom validator
        //initializer.setValidator(....);

        //here we are setting a custom PropertyEditor
        initializer.setPropertyEditorRegistrar(propertyEditorRegistry -> {
            SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
            propertyEditorRegistry.registerCustomEditor(Date.class,
                    new CustomDateEditor(dateFormatter, true));
        });
        return initializer;
    }

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(WebBindingInitializerExample.class, args);
    }
}
