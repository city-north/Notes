package cn.eccto.study.springframework.tutorials.bean;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;

/**
 * 将 {@link MyOtherBean} 注入到 {@link MyBean}中
 *
 * @author JonathanChen 2019/11/27 17:17
 */
public class InjectingOtherBeans {
    public static void main(String[] args) {
        DefaultListableBeanFactory context = new DefaultListableBeanFactory();

        //define and register MyOtherBean
        GenericBeanDefinition beanOtherDef = new GenericBeanDefinition();
        beanOtherDef.setBeanClass(MyOtherBean.class);
        context.registerBeanDefinition("other", beanOtherDef);

        //definine and register myBean
        GenericBeanDefinition beanDef = new GenericBeanDefinition();
        beanDef.setBeanClass(MyBean.class);
        MutablePropertyValues mpv = new MutablePropertyValues();
        mpv.addPropertyValue("otherBean", context.getBean("other"));
        beanDef.setPropertyValues(mpv);
        context.registerBeanDefinition("myBean", beanDef);

        //using MyBean instance
        MyBean bean = context.getBean(MyBean.class);
        bean.doSomething();
    }

    private static class MyBean {
        private MyOtherBean otherBean;

        public void setOtherBean(MyOtherBean otherBean) {
            this.otherBean = otherBean;
        }

        public void doSomething() {
            otherBean.doSomething();
        }
    }

    private static class MyOtherBean {

        public void doSomething() {
            System.out.println("from other bean ");
        }
    }
}
