package cn.eccto.study.sb.messageConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import java.util.Arrays;

/**
 * 通过 {@link HttpMessageConverters} 来注册一个 {@link StringHttpMessageConverter} 的实现类
 *
 * @author EricChen 2019/12/06 19:24
 */
@SpringBootApplication
public class MessageConverterExample {

    /**
     * 在原有默认的基础上添加一个新的 TheCustomConverter
     */
//    @Bean
//    public HttpMessageConverters addMessageConverter(){
//        return new HttpMessageConverters(new TheCustomConverter());
//    }

    /**
     * 替换掉默认的 HttpMessageConverters
     */
//    @Bean
//    public HttpMessageConverters addMessageConverter() {
//        return new HttpMessageConverters(
//                false, Arrays.asList(new TheCustomConverter()));
//    }

    /**
     * 直接注册为 Bean
     */
    @Bean
    public HttpMessageConverter<?> messageConverter(){
        return new TheCustomConverter();
    }


    public static void main(String[] args) {
        SpringApplication.run(MessageConverterExample.class);
    }


}

class TheCustomConverter extends StringHttpMessageConverter {

}


@Component
class RefreshListener {
    @Autowired
    private RequestMappingHandlerAdapter handlerAdapter;

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        handlerAdapter.getMessageConverters()
                .stream()
                .forEach(System.out::println);
    }
}