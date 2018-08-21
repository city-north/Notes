package chapter02.proxy.staticProxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class RealObject implements Action {
    private Logger logger = LoggerFactory.getLogger(RealObject.class);

    public RealObject() {
        logger.info("RealObject init()");
    }

    public void doSomething() {
        logger.info("RealObject doSomething");
    }
}
