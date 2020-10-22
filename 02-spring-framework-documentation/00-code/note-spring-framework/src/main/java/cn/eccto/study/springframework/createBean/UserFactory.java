package cn.eccto.study.springframework.createBean;

import cn.eccto.study.springframework.ioc.User;

/**
 * <p>
 * A Factory of User
 * </p>
 *
 * @author EricChen 2020/10/22 18:54
 */
public interface UserFactory {

    User createUser();
}
