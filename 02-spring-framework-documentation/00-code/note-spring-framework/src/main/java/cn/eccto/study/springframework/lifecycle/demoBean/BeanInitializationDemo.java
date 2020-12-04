package cn.eccto.study.springframework.lifecycle.demoBean;

import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen 2020/12/04 09:54
 */
public class BeanInitializationDemo implements InitializingBean {

    private String name;


    @PostConstruct
    public void testPostConstruct() {
        System.out.println("---testPostConstruct---");
    }

    public void customInitMethod() {
        System.out.println("---customInitMethod---");
    }


    public String getName() {
        return name;
    }

    public BeanInitializationDemo setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public String toString() {
        return "BeanInitializationDemo{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("---afterPropertiesSet---");
    }
}
