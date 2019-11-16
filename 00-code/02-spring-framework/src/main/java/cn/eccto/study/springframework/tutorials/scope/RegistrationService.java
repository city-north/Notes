package cn.eccto.study.springframework.tutorials.scope;

/**
 * description
 *
 * @author EricChen 2019/11/14 15:03
 */
public interface RegistrationService {

    /**
     * In real application there should be a async call back
     *
     * @param userInfo
     */
    void register(UserInfo userInfo);
}