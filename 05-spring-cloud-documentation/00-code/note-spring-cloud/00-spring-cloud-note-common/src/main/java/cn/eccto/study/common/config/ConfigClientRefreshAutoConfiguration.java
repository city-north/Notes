package cn.eccto.study.common.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.cloud.endpoint.RefreshEndpoint;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/05/16 09:42
 */
@ConditionalOnClass(value = {RefreshEndpoint.class})
@ConditionalOnProperty("spring.cloud.config.refreshInterval")
@AutoConfigureAfter(RefreshAutoConfiguration.class)
@Configuration
public class ConfigClientRefreshAutoConfiguration implements SchedulingConfigurer {
    private static final Log logger = LogFactory.getLog(ConfigClientRefreshAutoConfiguration.class);

    /**
     * 间隔刷新时间
     */
    @Value("${spring.cloud.config.refreshInterval}")
    private long refreshInterval;

    /**
     * 刷新的端点
     */
    @Autowired
    private RefreshEndpoint refreshEndpoint;

    
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {

    }
}
