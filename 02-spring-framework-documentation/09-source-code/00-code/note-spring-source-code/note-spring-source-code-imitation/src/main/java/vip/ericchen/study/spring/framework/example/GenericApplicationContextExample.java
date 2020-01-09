package vip.ericchen.study.spring.framework.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vip.ericchen.study.spring.framework.context.support.GenericApplicationContext;

/**
 * description
 *
 * @author EricChen 2020/01/09 19:49
 */
public class GenericApplicationContextExample {
    private static final Logger logger = LoggerFactory.getLogger(GenericApplicationContextExample.class);

    public static void main(String[] args) throws Exception {
        GenericApplicationContext context = new GenericApplicationContext("application.properties");
        HelloController bean = (HelloController)context.getBean("helloController");
        logger.debug(bean.helloWorld());
    }

}
