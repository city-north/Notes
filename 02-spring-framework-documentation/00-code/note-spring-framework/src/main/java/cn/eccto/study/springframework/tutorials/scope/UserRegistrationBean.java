package cn.eccto.study.springframework.tutorials.scope;

import java.util.Map;

/**
 * description
 *
 * @author JonathanChen 2019/11/14 20:02
 */
public interface UserRegistrationBean {
    public static String KEY_EMAIL = "email";
    public static String KEY_PASSWORD = "password";

    /**
     * @param userInfo
     */
    void setUserInfo(UserInfo userInfo);

    /**
     * @return list of validation errors otherwise null
     */
    Map<String, String> validate();

    /**
     * Perform registration for the new user.
     */
    void register();
}
