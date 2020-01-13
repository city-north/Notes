package vip.ericchen.study.spring.mvc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * description
 *
 * @author EricChen 2020/01/13 17:27
 */
@Configuration
@ComponentScan(basePackages = "vip.ericchen.study.spring.mvc")
public class ServletConfig {


    @Bean
    public String testStrInServletConfig() {
        return "testStrInServletConfig";
    }


}
