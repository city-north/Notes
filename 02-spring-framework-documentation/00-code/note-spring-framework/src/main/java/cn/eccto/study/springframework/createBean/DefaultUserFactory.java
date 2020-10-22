package cn.eccto.study.springframework.createBean;

import cn.eccto.study.springframework.ioc.User;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

/**
 * <p>
 * TODO
 * </p>
 *
 * @author EricChen 2020/10/22 18:56
 */
public class DefaultUserFactory implements UserFactory, InitializingBean {


    /**
     * 1. 基于 @PostConstruct 注解
     */
    @PostConstruct
    public void init() {
        System.out.println("@PostConstruct : UserFactory 初始化中...");
    }

    public void initUserFactory() {
        System.out.println("自定义初始化方法 initUserFactory() : UserFactory 初始化中...");
    }

    @Override
    public User createUser() {
        return new User().setName("EricChen").setAge(123);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("InitializingBean#afterPropertiesSet() : UserFactory 初始化中...");
    }

    @PreDestroy
    public void preDestroy() {
        System.out.println("@PreDestroy : UserFactory 销毁中...");
    }

    public void doDestroy() {
        System.out.println("自定义销毁方法 doDestroy() : UserFactory 销毁中...");
    }

    @Override
    public void finalize() throws Throwable {
        System.out.println("当前 DefaultUserFactory 对象正在被垃圾回收...");
    }
}
