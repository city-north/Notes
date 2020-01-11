package cn.eccto.study.springframework.tutorials.aop.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * HelloAspectService
 *
 * @author EricChen 2020/01/10 14:35
 */
@Service
public class HelloAspectService {

    private final static Logger log = LoggerFactory.getLogger(HelloAspectService.class);

    public String hello() {
        log.info("hello");
        return "eric";
    }

}
