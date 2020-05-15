package vip.ericchen.study.sc.config.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import vip.ericchen.study.sc.config.ConfigInfoProperties;

/**
 * <p>
 * description
 * </p>
 *
 * @author qiang.chen04@hand-china.com 2020/05/13 23:08
 */
@RestController
public class TestConfigController {

    @Autowired
    private ConfigInfoProperties configInfoProperties;

    @GetMapping("/test-config")
    public String getConfig() {
        return configInfoProperties.getConfig();
    }
}
