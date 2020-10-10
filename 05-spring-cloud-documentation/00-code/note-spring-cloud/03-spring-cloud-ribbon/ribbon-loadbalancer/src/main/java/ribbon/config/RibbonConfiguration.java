package ribbon.config;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AvailabilityFilteringRule;
import com.netflix.loadbalancer.IPing;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.PingUrl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/10/10 19:55
 */
public class RibbonConfiguration {
    @Autowired
    IClientConfig ribbonClientConfig;

    //替换掉默认的ping策略 NoOpPing
    @Bean
    public IPing ribbonPing(IClientConfig config) {
        return new PingUrl();
    }

    //替换掉默认的负载均衡策略 ZoneAvoidanceRule
    @Bean
    public IRule ribbonRule(IClientConfig config) {
        return new AvailabilityFilteringRule();
    }
}