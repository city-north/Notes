package cn.eccto.study.springframework.ioc;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.context.ApplicationContext;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen 2020/10/21 21:35
 */
public class UserRepository {

    ObjectFactory objectFactory;

    public ObjectFactory getObjectFactory() {
        return objectFactory;
    }

    public UserRepository setObjectFactory(ObjectFactory objectFactory) {
        this.objectFactory = objectFactory;
        return this;
    }
}
