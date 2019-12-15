package cn.eccto.study.eureka.consumer.controller;

import cn.eccto.study.eureka.consumer.config.ConfigClientProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 配置中心测试
 *
 * @author EricChen 2019/12/15 16:39
 */
@RestController
public class ConfigClientExampleController {

    @Value("${eccto.config-server:no data}")
    String value;

    @Autowired
    private ConfigClientProperties configClientProperties;

    @GetMapping("/config")
    public String getConfigFromConfigServer() {
        return value;
    }

    @GetMapping("/config/prop")
    public String getConfigClientProperties() {
        return configClientProperties.getConfigServer();
    }

}
