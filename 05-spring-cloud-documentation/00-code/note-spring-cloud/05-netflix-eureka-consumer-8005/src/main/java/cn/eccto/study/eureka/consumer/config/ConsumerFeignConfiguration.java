package cn.eccto.study.eureka.consumer.config;

import cn.eccto.study.eureka.consumer.feign.interceptor.CompositeRequestInterceptor;
import cn.eccto.study.eureka.consumer.feign.interceptor.MyFeignRequestInterceptor;
import feign.Contract;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * description
 *
 * @author EricChen 2019/12/26 20:55
 */
@Configuration
public class ConsumerFeignConfiguration {
//    @Bean
//    public Contract feignContract() {
//        return new feign.Contract.Default();
//    }

    /**
     * 添加自定义拦截器,可以拦截一些权限的校验信息
     * @return
     */
    @Bean
    public CompositeRequestInterceptor basicAuthRequestInterceptor(List<MyFeignRequestInterceptor> myFeignRequestInterceptors) {
        return new CompositeRequestInterceptor(myFeignRequestInterceptors);
    }
}
