package chapter02.proxy.staticProxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 代理对象
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class ProxyObject implements Action {
    private RealObject realObject;
    private Logger logger = LoggerFactory.getLogger(ProxyObject.class);

    public ProxyObject(RealObject realObject) {
        this.realObject = realObject;
    }

    public void doSomething() {
        logger.info("ProxyObject doSomething");
        realObject.doSomething();
    }
}
