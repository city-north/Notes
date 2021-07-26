package cn.eccto.study.springframework.createBean;


import cn.eccto.study.springframework.User;

/**
 * <p>
 * A Factory of User
 * </p>
 *
 * @author JonathanChen 2020/10/22 18:54
 */
public interface UserFactory {

    User createUser();
}
