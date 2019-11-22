package cn.eccto.study.springframework.tutorials.scope;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * description
 *
 * @author EricChen 2019/11/14 20:05
 */
@Configuration
public class Config {

    @Bean
    public UserRegistrationValidator validator () {
        return new UserRegistrationValidator();
    }

    @Bean
    public RegistrationService registrationService () {
        return new RegistrationServiceImpl();
    }

    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    @Bean
    public UserRegistrationBean userRegistrationBean () {
        return new UserRegistrationBeanImpl();
    }
}