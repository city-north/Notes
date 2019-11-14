package cn.eccto.study.springframework.tutorials.scope;

import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * description
 *
 * @author qiang.chen04@hand-china.com 2019/11/14 15:04
 */
public class UserRegistrationBeanImpl implements UserRegistrationBean {

    @Autowired
    private RegistrationService registrationService;
    @Autowired
    private UserRegistrationValidator validator;

    private UserInfo userInfo;

    @PostConstruct
    private void initialize () {
        System.out.println("initializing: " + System.identityHashCode(this));
    }

    @Override
    public void setUserInfo (UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public Map<String, String> validate () {
        if (userInfo == null) {
            throw new RuntimeException("UserInfo must be set before calling validate method");
        }

        Map<String, String> errors = new LinkedHashMap<>();

        String errorMessage = validator.validateEmail(userInfo.getEmail());
        if (errorMessage != null) {
            errors.put(KEY_EMAIL, errorMessage);
        }

        errorMessage = validator.validatePassword(userInfo.getPassword());
        if (errorMessage != null) {
            errors.put(KEY_PASSWORD, errorMessage);
        }

        return errors;
    }

    @Override
    public void register () {
        registrationService.register(userInfo);
    }
}