package cn.eccto.study.gateway;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * description
 *
 * @author EricChen 2019/12/13 08:02
 */
@SpringCloudApplication
public class GateWayApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(GateWayApplication.class).web(WebApplicationType.REACTIVE).run(args);
//        ConfigurableApplicationContext run = SpringApplication.run(GateWayApplication.class, args);
    }


}
