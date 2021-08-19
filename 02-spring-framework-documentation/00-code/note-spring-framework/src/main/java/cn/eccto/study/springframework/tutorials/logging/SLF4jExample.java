package cn.eccto.study.springframework.tutorials.logging;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * description
 *
 * @author JonathanChen 2019/11/29 23:55
 */
public class SLF4jExample {
    private static Logger log = LoggerFactory.getLogger(SLF4jExample.class);

    public static void main(String[] args) {
        log.error("info");
    }

}
