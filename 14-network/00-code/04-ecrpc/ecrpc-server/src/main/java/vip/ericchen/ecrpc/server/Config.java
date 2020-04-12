package vip.ericchen.ecrpc.server;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

/**
 * <p>
 * description
 * </p>
 *
 * @author ericchen.vip@foxmail.com 2020/04/12 22:55
 */
@Configuration
@ComponentScan({"vip.ericchen.ecrpc.server"})
public class Config {

    @Bean
    public RpcServer rpcServer() {
        return new RpcServer(9090);
    }
}
