package cn.eccto.study.sb.staticpages;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

/**
 * description
 *
 * @author EricChen 2019/12/04 11:31
 */
@EnableAutoConfiguration
public class WebStaticPagesExample {
    public static void main (String[] args) {
        SpringApplication app = new SpringApplication(WebStaticPagesExample.class);
        app.run(args);
    }
}
