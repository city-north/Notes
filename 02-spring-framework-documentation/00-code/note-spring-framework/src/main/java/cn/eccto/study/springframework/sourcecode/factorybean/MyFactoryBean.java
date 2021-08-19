package cn.eccto.study.springframework.sourcecode.factorybean;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.stereotype.Component;

/**
 * description
 *
 * @author JonathanChen 2020/01/08 10:39
 */
public class MyFactoryBean implements FactoryBean<FactoryBeanService> {

    @Override
    public FactoryBeanService getObject() throws Exception {
        return new FactoryBeanServiceImpl();
    }

    @Override
    public Class<?> getObjectType() {
        return FactoryBeanService.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
