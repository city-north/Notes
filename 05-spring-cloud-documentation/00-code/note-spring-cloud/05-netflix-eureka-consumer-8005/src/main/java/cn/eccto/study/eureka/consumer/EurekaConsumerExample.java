package cn.eccto.study.eureka.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;

/**
 * description
 *
 * @author EricChen 2019/12/13 16:05
 */
@SpringCloudApplication
public class EurekaConsumerExample {
    public static void main(String[] args) {
        SpringApplication.run(EurekaConsumerExample.class, args);
    }
}
