package cn.eccto.study.eureka.consumer.feign.interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 组合自定义的拦截器
 *
 * @author EricChen 2019/12/26 21:00
 */
public class CompositeRequestInterceptor implements RequestInterceptor {

    private List<MyFeignRequestInterceptor> feignRequestInterceptors;

    public CompositeRequestInterceptor(List<MyFeignRequestInterceptor> myFeignRequestInterceptors) {
        feignRequestInterceptors  = Optional.ofNullable(myFeignRequestInterceptors).orElse(new ArrayList<>());

    }


    @Override
    public void apply(RequestTemplate requestTemplate) {
        feignRequestInterceptors.forEach(myFeignRequestInterceptor -> myFeignRequestInterceptor.apply(requestTemplate));
    }
}
