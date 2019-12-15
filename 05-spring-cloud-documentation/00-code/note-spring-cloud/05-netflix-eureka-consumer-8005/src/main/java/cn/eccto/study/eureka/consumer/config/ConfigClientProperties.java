package cn.eccto.study.eureka.consumer.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * description
 *
 * @author EricChen 2019/12/15 16:46
 */
@Configuration("config")
@ConfigurationProperties("eccto")
public class ConfigClientProperties {

    private String configServer;

    public String getConfigServer() {
        return configServer;
    }

    public void setConfigServer(String configServer) {
        this.configServer = configServer;
    }
}
