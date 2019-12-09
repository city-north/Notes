package cn.eccto.study.sb.autoconfigure;

import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;

/**
 * 使用 {@link AutoConfigurationPackages} 来获取所有扫描的包
 *
 * @author EricChen 2019/12/06 12:19
 */
@SpringBootApplication
public class AutoConfigurationPackagesExample {

    public static void main(String[] args) {
        String[] appArgs = {"--debug"};

        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder(AutoConfigurationPackagesExample.class)
                .bannerMode(Banner.Mode.OFF)
                .logStartupInfo(false);
        SpringApplication build = springApplicationBuilder.build(appArgs);
        ConfigurableApplicationContext run = build.run(appArgs);
        List<String> strings = AutoConfigurationPackages.get(run);
        strings.forEach(System.out::println);
    }
}

