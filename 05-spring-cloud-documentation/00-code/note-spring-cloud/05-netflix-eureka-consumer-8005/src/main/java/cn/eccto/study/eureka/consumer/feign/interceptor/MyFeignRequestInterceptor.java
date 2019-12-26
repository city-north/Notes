package cn.eccto.study.eureka.consumer.feign.interceptor;

import feign.RequestTemplate;

/**
 * description
 *
 * @author EricChen 2019/12/26 21:01
 */
public interface MyFeignRequestInterceptor {
    void apply(RequestTemplate template);
}
