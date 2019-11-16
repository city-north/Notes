package cn.eccto.study.springframework.tutorials.scope;

import javax.annotation.PostConstruct;

/**
 * description
 *
 * @author EricChen 2019/11/14 15:03
 */
public class RegistrationServiceImpl implements RegistrationService {

    //Ideally we should be injecting some DAO here

    @PostConstruct
    public void init() {
        System.out.println("initializing: " + System.identityHashCode(this));
    }

    @Override
    public void register(UserInfo userInfo) {
        //in real app we should pass userInfo to DAO to create user in Database
        System.out.println("User has been registered successfully: "+userInfo);
    }
}