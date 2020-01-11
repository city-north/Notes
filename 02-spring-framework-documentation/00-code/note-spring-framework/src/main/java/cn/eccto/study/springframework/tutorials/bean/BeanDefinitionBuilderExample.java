package cn.eccto.study.springframework.tutorials.bean;

import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;

/**
 * 代码实例: 根据{@link BeanDefinitionBuilder} 建造者模式来构建一个啊 bean 并注册到 {@link DefaultListableBeanFactory}
 *
 * @author EricChen 2019/11/27 19:09
 */
public class BeanDefinitionBuilderExample {

    public static void main(String[] args) {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder
                .rootBeanDefinition(MyBean.class)
                .addPropertyValue("str", "hello")
                .setScope(ConfigurableBeanFactory.SCOPE_PROTOTYPE);
        factory.registerBeanDefinition("myBean", beanDefinitionBuilder.getBeanDefinition());

        MyBean bean = factory.getBean(MyBean.class);
        bean.doSomething();
    }

    public static class MyBean {
        private String str;

        public void setStr(String str) {
            this.str = str;
        }

        public void doSomething() {
            System.out.println("from MyBean " + str);
        }
    }
}
