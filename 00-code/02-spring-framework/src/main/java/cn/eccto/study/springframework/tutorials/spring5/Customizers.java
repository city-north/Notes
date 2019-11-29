package cn.eccto.study.springframework.tutorials.spring5;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

/**
 * description
 *
 * @author EricChen 2019/11/27 18:05
 */
public class Customizers {

    public static void prototypeScoped(BeanDefinition bd) {
        bd.setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE);
    }

    public static void lazy(BeanDefinition bd) {
        bd.setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE);
    }


    public static void defaultInitMethod(BeanDefinition bd) {
        bd.setInitMethodName("init");
    }

    public static void defaultDestroyMethod(BeanDefinition bd) {
        bd.setDestroyMethodName("destroy");
    }
}