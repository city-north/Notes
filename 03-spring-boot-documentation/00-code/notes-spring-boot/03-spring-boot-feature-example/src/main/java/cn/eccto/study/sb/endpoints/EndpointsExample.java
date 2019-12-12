package cn.eccto.study.sb.endpoints;

import cn.eccto.study.sb.h2.H2Example;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * description
 *
 * @author EricChen 2019/12/12 12:37
 */
@SpringBootApplication
public class EndpointsExample {
    public static void main(String[] args) {
        ConfigurableApplicationContext endpoints = new SpringApplicationBuilder(H2Example.class)
                .web(WebApplicationType.SERVLET)
                .profiles("endpoints")
                .run(args);

    }
}
