package cn.eccto.study.springframework.tutorials.bean;

import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.support.GenericBeanDefinition;

import java.util.Date;

/**
 * 此案例展示了如何使用 {@link GenericBeanDefinition} 手动注册 bean 到 {@link DefaultListableBeanFactory}
 * 并且使用{@link MutablePropertyValues} 设置 bean 的属性 date
 *
 * @author JonathanChen 2019/11/27 19:03
 */
public class GenericBeanDefinitionExample {
    public static void main(String[] args) {
        DefaultListableBeanFactory context = new DefaultListableBeanFactory();

        GenericBeanDefinition gbd = new GenericBeanDefinition();
        gbd.setBeanClass(MyBean.class);

        MutablePropertyValues mpv = new MutablePropertyValues();
        mpv.add("date", new Date());

        //alternatively we can use:
        // gbd.getPropertyValues().addPropertyValue("date", new Date());
        gbd.setPropertyValues(mpv);

        context.registerBeanDefinition("myBeanName", gbd);

        MyBean bean = context.getBean(MyBean.class);
        bean.doSomething();
    }

    private static class MyBean {
        private Date date;

        public void doSomething() {
            System.out.println("from my bean, date: " + date);
        }

        public void setDate(Date date) {
            this.date = date;
        }
    }
}
