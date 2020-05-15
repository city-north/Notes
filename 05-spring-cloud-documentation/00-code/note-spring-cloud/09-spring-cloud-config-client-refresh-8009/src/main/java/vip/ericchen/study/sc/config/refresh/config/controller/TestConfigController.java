package vip.ericchen.study.sc.config.refresh.config.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * description
 * </p>
 *
 * @author qiang.chen04@hand-china.com 2020/05/13 23:08
 */
@RestController
@RefreshScope
public class TestConfigController {

    @Autowired
    private ConfigInfoProperties configInfoProperties;

    @GetMapping("/test-config")
    public String getConfig() {
        return configInfoProperties.getConfig();
    }
}
