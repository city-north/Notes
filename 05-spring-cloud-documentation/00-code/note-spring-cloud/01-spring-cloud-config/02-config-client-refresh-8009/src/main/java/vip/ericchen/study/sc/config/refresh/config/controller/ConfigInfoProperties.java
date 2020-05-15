package vip.ericchen.study.sc.config.refresh.config.controller;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/05/13 23:05
 */
@Component
@RefreshScope
@ConfigurationProperties("vip.ericchen.study")
public class ConfigInfoProperties {

    private String config;

    public String getConfig() {
        return config;
    }

    public ConfigInfoProperties setConfig(String config) {
        this.config = config;
        return this;
    }
}
