package chapter02.proxy.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author EricChen 2018-5-16
 * @email qiang.chen04@hand-china.com
 */
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);
    public void save() {
        logger.info("正在调用UserServiceImpl#save");
    }

    public void delete() {
        logger.info("正在调用UserServiceImpl#delete");
    }
}
