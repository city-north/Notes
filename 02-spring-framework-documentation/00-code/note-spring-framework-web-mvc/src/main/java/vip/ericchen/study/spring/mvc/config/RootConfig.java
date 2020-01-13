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
public class RootConfig {
    @Bean
    public String testStrInRootConfig() {
        return "testStrInRootConfig";
    }
}
