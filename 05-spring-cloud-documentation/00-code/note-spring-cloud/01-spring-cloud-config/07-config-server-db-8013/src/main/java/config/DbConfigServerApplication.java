package config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * <p>
 * description
 * </p>
 *
 * @author EricChen 2020/05/16 08:50
 */
@EnableConfigServer
@SpringBootApplication
public class DbConfigServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(DbConfigServerApplication.class, args);
    }
}
