package cn.eccto.study.springframework.tutorials.conditional;

import cn.eccto.study.springframework.tutorials.conditional.serivce.LocaleService;
import cn.eccto.study.springframework.tutorials.conditional.serivce.impl.LocaleCanadaServiceImpl;
import cn.eccto.study.springframework.tutorials.conditional.serivce.impl.LocaleChinaServiceImpl;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;

/**
 * description
 *
 * @author EricChen 2019/11/15 20:52
 */
@Configuration
public class ConditionalConfiguration {


    @Bean
    @Conditional(LocaleConditionChina.class)
    public LocaleService localeChinaService() {
        return new LocaleChinaServiceImpl();
    }


    @Bean
    @Conditional(LocaleConditionCanada.class)
    public LocaleService localeCanadaService() {
        return new LocaleCanadaServiceImpl();
    }

}
