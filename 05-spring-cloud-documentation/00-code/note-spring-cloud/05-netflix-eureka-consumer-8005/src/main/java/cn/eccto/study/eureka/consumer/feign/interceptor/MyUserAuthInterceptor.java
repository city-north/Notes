package cn.eccto.study.eureka.consumer.feign.interceptor;

import feign.RequestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;

/**
 * 自定义简单的拦截器,将 userName 和 password 写入到 header中
 *
 * @author EricChen 2019/12/26 21:11
 */
@Component
public class MyUserAuthInterceptor implements MyFeignRequestInterceptor {

    private static final Logger LOGGER = LoggerFactory.getLogger(MyUserAuthInterceptor.class);


    @Override
    public void apply(RequestTemplate template) {
        Map<String, Collection<String>> queries = template.queries();
        Collection<String> username = queries.get("username");
        Collection<String> password = queries.get("password");
        template.header("username",username);
        template.header("password",password);
    }
}
