package vip.ericchen.study.sc.bus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/05/15 18:59
 */
@EnableConfigServer
@SpringBootApplication
public class ConfigBusServer {
    public static void main(String[] args) {
        SpringApplication.run(ConfigBusServer.class, args);
    }

}
